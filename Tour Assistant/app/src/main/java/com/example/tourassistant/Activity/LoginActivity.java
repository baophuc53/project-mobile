package com.example.tourassistant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.model.LoginRequest;
import com.example.tourassistant.model.LoginResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    Button signinButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        signinButton=findViewById(R.id.SignInButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        addEventFacebookLogin();
    }

    private void userLogin() {
        EditText emailPhone = findViewById(R.id.edit_login_username);
        EditText password = findViewById(R.id.edit_login_pasword);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailPhone(emailPhone.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        UserService userService;
        MyAPIClient.getInstance().setAccessToken("");
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
                            Toast.makeText(LoginActivity.this, "Missing email/phone or password", Toast.LENGTH_LONG).show();
                        else if (error.getResponse().getStatus() == 404)
                            Toast.makeText(LoginActivity.this, "Wrong email/phone or password", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addEventFacebookLogin(){
       callbackManager = CallbackManager.Factory.create();
       LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebook);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();

                // save accessToken to SharedPreference
                Toast.makeText(LoginActivity.this, accessToken, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Lỗi lol xác định", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();

            }
            });
    }

    public void Signup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
