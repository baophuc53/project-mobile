package com.example.tourassistant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.model.LoginByFaceRequest;
import com.example.tourassistant.model.LoginByFaceResponse;
import com.example.tourassistant.model.LoginRequest;
import com.example.tourassistant.model.LoginResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    private Button signinButton;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        callbackManager = CallbackManager.Factory.create();

        signinButton=findViewById(R.id.SignInButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.tourassistant.Activity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", "printHashKey()", e);
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", "printHashKey()", e);
        }

        loginButton = findViewById(R.id.login_button);
        addFbLoginEvent();
    }

    private void addFbLoginEvent() {
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginByFaceRequest loginByFaceRequest = new LoginByFaceRequest();
                loginByFaceRequest.setToken(loginResult.getAccessToken().getToken());
                UserService userService;
                userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                userService.loginByFacebook(loginByFaceRequest, new Callback<LoginByFaceResponse>() {
                    @Override
                    public void success(LoginByFaceResponse loginByFaceResponse, Response response) {
                        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("token", loginByFaceResponse.getToken());
                        editor.commit();
                        Intent intent=new Intent(LoginActivity.this,ListTourActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        switch (error.getKind()) {
                            case HTTP:
                                if (error.getResponse().getStatus() == 400)
                                    Toast.makeText(LoginActivity.this, "Đăng nhập bằng Facebook thất bại", Toast.LENGTH_LONG).show();
                                else if (error.getResponse().getStatus() == 500)
                                    Toast.makeText(LoginActivity.this, "Lỗi người dùng", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    private void getFbInfo() {
//        if (AccessToken.getCurrentAccessToken() != null) {
//            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                    new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(JSONObject object, GraphResponse response) {
//                            if (object != null) {
//                                Toast.makeText(LoginActivity.this, "Name: " + object.optString("name"), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(LoginActivity.this, "ID: " + object.optString("id"), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,link");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
//    }

    private void userLogin() {
        EditText emailPhone = findViewById(R.id.edit_login_username);
        EditText password = findViewById(R.id.edit_login_pasword);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailPhone(emailPhone.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        UserService userService;
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.login(loginRequest, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token",loginResponse.getToken());
                editor.commit();
                Intent intent=new Intent(LoginActivity.this,ListTourActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 400)
                            Toast.makeText(LoginActivity.this, "Thiếu tên đăng nhập", Toast.LENGTH_LONG).show();
                        else if (error.getResponse().getStatus() == 404)
                            Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void Signup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
