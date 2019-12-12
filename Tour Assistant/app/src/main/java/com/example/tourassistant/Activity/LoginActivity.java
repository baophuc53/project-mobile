package com.example.tourassistant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.tourassistant.model.UserInfoResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

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
                        editor.putString("userId", loginByFaceResponse.getUserId());
                        editor.putBoolean("LoginByFB", true);
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
                        if (AccessToken.getCurrentAccessToken() == null) {
                            return; // already logged out
                        }

                        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                .Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {
                                LoginManager.getInstance().logOut();
                            }
                        }).executeAsync();
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
                editor.putString("userId", loginResponse.getUserId());
                editor.putBoolean("LoginByFB", false);
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
