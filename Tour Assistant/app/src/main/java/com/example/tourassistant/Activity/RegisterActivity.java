package com.example.tourassistant.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.tourassistant.Activity.Constants.defaultToken;

public class RegisterActivity extends AppCompatActivity {

    Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Sign Up");
        signupButton=findViewById(R.id.SignUpButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void userRegister() {
        EditText name = findViewById(R.id.edit_register_name);
        EditText email = findViewById(R.id.edit_register_email);
        EditText phone = findViewById(R.id.edit_register_phone);
        EditText password = findViewById(R.id.edit_register_password);
        EditText confirm = findViewById(R.id.edit_register_confirmPass);
        boolean correct = true;
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Vui lòng điền thông tin");
            correct = false;
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Vui lòng điền thông tin");
            correct = false;
        } else if (!isValidEmail(email.getText().toString())) {
            email.setError("Email không hợp lệ");
            correct = false;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            phone.setError("Vui lòng điền thông tin");
            correct = false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Vui lòng điền thông tin");
            correct = false;
        }
        if (!confirm.getText().toString().equals(password.getText().toString())) {
            confirm.setError("Xác nhận mật khẩu không chính xác");
            correct = false;
        }
        if (correct) {
            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (false) {
            RegisterRequest request = new RegisterRequest();
            request.setEmail(email.getText().toString());
            request.setFullName(name.getText().toString());
            request.setPhone(phone.getText().toString());
            request.setPassword(password.getText().toString());
            request.setAddress("");
            request.setDob("1999-01-01");
            request.setGender(1);
            UserService userService;
            MyAPIClient.getInstance().setAccessToken("");
            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
            userService.register(request, new Callback<RegisterResponse>() {
                @Override
                public void success(RegisterResponse registerResponse, Response response) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    switch (error.getKind()) {
                        case HTTP:
                            if (error.getResponse().getStatus() == 400)
                                Toast.makeText(RegisterActivity.this, "Email hoặc số điện thoại đã tồn tại hoặc sai định dạng", Toast.LENGTH_LONG).show();
                            else if (error.getResponse().getStatus() == 503)
                                Toast.makeText(RegisterActivity.this, "Dịch vụ tạm thời không hoạt đông", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



    boolean isValidEmail(String email) {
            return email.contains("@");
        }

}
