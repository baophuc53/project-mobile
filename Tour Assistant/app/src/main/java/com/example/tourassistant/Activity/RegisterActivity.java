package com.example.tourassistant.Activity;

import android.os.Bundle;
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

    private void userRegister() {
        EditText name = findViewById(R.id.edit_register_name);
        EditText email = findViewById(R.id.edit_register_email);
        EditText phone = findViewById(R.id.edit_register_phone);
        EditText password = findViewById(R.id.edit_register_password);
//        name.setText("abc abc");
//        email.setText("testtest@sad.com");
//        phone.setText("0123956899");
//        password.setText("aagg");

        RegisterRequest request=new RegisterRequest();
        request.setEmail(email.getText().toString());
        request.setFullName(name.getText().toString());
        request.setPhone(phone.getText().toString());
        request.setPassword(password.getText().toString());
        UserService userService;
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);

        userService.register(request, new Callback<RegisterResponse>() {
            @Override
            public void success(RegisterResponse registerResponse, Response response) {
                Log.i("response", response.getBody().toString());
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 400)
                            Toast.makeText(RegisterActivity.this, "Email hoặc số điện thoại đã tồn tại hoặc sai định dạng", Toast.LENGTH_LONG).show();
                        else if(error.getResponse().getStatus() == 503)
                        Toast.makeText(RegisterActivity.this, "Dịch vụ tạm thời không hoạt đông", Toast.LENGTH_LONG).show();
                        break;
                    case NETWORK:
                    case UNEXPECTED:
                        Toast.makeText(RegisterActivity.this, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(RegisterActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
