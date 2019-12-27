package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.model.SendCmtRequest;
import com.ygaps.travelapp.model.SendOTPRequest;
import com.ygaps.travelapp.model.SendOTPResponse;
import com.ygaps.travelapp.model.VerifyOTPtoChangePassRequest;
import com.ygaps.travelapp.model.VerifyOTPtoChangePassResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email, OTP, newPass, confPass;
    Button sendOTP, changePass;
    LinearLayout sendOTPLayout, changePasswordLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Forgot Password");
        addControll();
        addEvent();
    }

    private void addEvent() {
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSendOTPEvent();
            }
        });
    }

    private void addSendOTPEvent() {
        boolean check = false;
        SendOTPRequest sendOtpRequest = new SendOTPRequest();
        sendOtpRequest.setType("email");
        if (!TextUtils.isEmpty(email.getText().toString())){
            sendOtpRequest.setValue(email.getText().toString());
            check = true;
        }
        else
            email.setError("Vui lòng điền email.");

        if (check) {
            UserService userService;
            MyAPIClient.getInstance().setAccessToken("");
            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
            userService.sendOTP(sendOtpRequest, new Callback<SendOTPResponse>() {
                @Override
                public void success(SendOTPResponse sendOTPResponse, Response response) {
                    Toast.makeText(ForgotPasswordActivity.this, "Thàng công rồi", Toast.LENGTH_SHORT).show();
                    sendOTPLayout.setVisibility(View.GONE);
                    changePasswordLayout.setVisibility(View.VISIBLE);
                    changePasswordEvent(sendOTPResponse.getUserId());
                }

                @Override
                public void failure(RetrofitError error) {
                    switch (error.getKind()) {
                        case HTTP:
                            if (error.getResponse().getStatus() == 400)
                                Toast.makeText(ForgotPasswordActivity.this, "Sai type", Toast.LENGTH_LONG).show();
                            if (error.getResponse().getStatus() == 500)
                                Toast.makeText(ForgotPasswordActivity.this, "Lỗi khác", Toast.LENGTH_LONG).show();
                            if (error.getResponse().getStatus() == 404)
                                Toast.makeText(ForgotPasswordActivity.this, "EMAIL không tồn tại", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        }
    }

    private void changePasswordEvent(final Integer userId) {
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true, pass = false;
                VerifyOTPtoChangePassRequest verifyOTPtoChangePassRequest = new VerifyOTPtoChangePassRequest();
                verifyOTPtoChangePassRequest.setUserId(userId);
                if (TextUtils.isEmpty(OTP.getText().toString())){
                    OTP.setError("Vui lòng điền OTP");
                    check = false;
                }
                else
                    verifyOTPtoChangePassRequest.setVerifyCode(OTP.getText().toString());
                if (TextUtils.isEmpty(newPass.getText().toString())) {
                    OTP.setError("Vui lòng điền mật khẩu mới");
                    check =  false;
                }
                if (TextUtils.isEmpty(confPass.getText().toString())){
                    check = false;
                    OTP.setError("Vui lòng xác nhận mật khẩu mới");
                }
                if (newPass.getText().toString().compareTo(confPass.getText().toString()) == 0) {
                    pass = true;
                    verifyOTPtoChangePassRequest.setNewPassword(newPass.getText().toString());
                }

                    if (check && pass){
                    UserService userService;
                    MyAPIClient.getInstance().setAccessToken("");
                    userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                    userService.verifyOTP(verifyOTPtoChangePassRequest, new Callback<VerifyOTPtoChangePassResponse>() {
                        @Override
                        public void success(VerifyOTPtoChangePassResponse verifyOTPtoChangePassResponse, Response response) {
                            Toast.makeText(ForgotPasswordActivity.this, verifyOTPtoChangePassResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            switch (error.getKind()){
                                case HTTP:
                                    if (error.getResponse().getStatus() == 403)
                                        Toast.makeText(ForgotPasswordActivity.this, "OTP sai", Toast.LENGTH_SHORT).show();
                                    if (error.getResponse().getStatus() == 500)
                                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    private void addControll() {
        email = findViewById(R.id.edtEmail);
        sendOTP = findViewById(R.id.SendOTPButton);
        sendOTPLayout = findViewById(R.id.sendOTPLayout);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        OTP = findViewById(R.id.edtOTP);
        newPass = findViewById(R.id.edtNewPassword);
        confPass = findViewById(R.id.edtConfirmPassword);
        changePass = findViewById( R.id.ChangePassButton);
    }
}
