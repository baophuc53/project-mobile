package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.model.ChangePasswordRequest;
import com.ygaps.travelapp.model.ChangePasswordResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPass, newPass, confirmPass;
    Button changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Change Password");
        addControl();
        addEvent();
    }

    private void addEvent() {
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass, confirmpass;
                newpass = newPass.getText().toString();
                confirmpass = confirmPass.getText().toString();
                if (newpass.equals(confirmpass)) {
                    final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
                    String Token = sharedPreferences.getString("token", "");
                    String userId = sharedPreferences.getString("userId", "");

                    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                    changePasswordRequest.setCurrentPassword(currentPass.getText().toString());
                    changePasswordRequest.setUserId(Integer.parseInt(userId));
                    changePasswordRequest.setNewPassword(newpass);

                    UserService userService;
                    MyAPIClient.getInstance().setAccessToken(Token);
                    userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                    userService.changePassword(changePasswordRequest, new Callback<ChangePasswordResponse>() {
                        @Override
                        public void success(ChangePasswordResponse changePasswordResponse, Response response) {
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            switch (error.getKind()) {
                                case HTTP: {
                                    if (error.getResponse().getStatus() == 400)
                                        Toast.makeText(ChangePasswordActivity.this, "Password hiện tại sai", Toast.LENGTH_SHORT).show();
                                    if (error.getResponse().getStatus() == 404)
                                        Toast.makeText(ChangePasswordActivity.this, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                                    if (error.getResponse().getStatus() == 500)
                                        Toast.makeText(ChangePasswordActivity.this, "Lỗi khác", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                default:
                                    Toast.makeText(ChangePasswordActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    confirmPass.setError("Mật khẩu xác nhận không khớp");
                }
            }
        });
    }

    private void addControl() {
        currentPass = findViewById(R.id.change_current_pass);
        newPass = findViewById(R.id.change_new_password);
        confirmPass = findViewById(R.id.change_confirmPass);
        changePass = findViewById(R.id.ChangePassButton);
    }
}
