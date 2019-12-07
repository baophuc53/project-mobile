package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.model.UserInfoResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingActivity extends AppCompatActivity {

    ImageView avtUser;
    TextView nameUser, editProfile;
    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Setting");
        addControl();
        addActionBottomNavigationView();
        getUserInfor();
        addSignOutEvent();
        addUserInfo();
        addEEditProfileEvent();
    }

    private void addEEditProfileEvent() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserInfor() {
        final SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String Token =sharedPreferences.getString("token","");

        UserService userService;
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getUserInfo(new Callback<UserInfoResponse>() {
            @Override
            public void success(UserInfoResponse userInfoResponse, Response response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(userInfoResponse);
                editor.putString("userInfo", json);
                editor.commit();
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 401)
                            Toast.makeText(SettingActivity.this, "Không tìm thấy access token", Toast.LENGTH_SHORT).show();
                        if (error.getResponse().getStatus() == 503)
                            Toast.makeText(SettingActivity.this, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SettingActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUserInfo() {
        final SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        boolean loginByFB = sharedPreferences.getBoolean("LoginByFB", false);
        Gson gson= new Gson();
        String json = sharedPreferences.getString("userInfo", "");
        UserInfoResponse user = gson.fromJson(json, UserInfoResponse.class);
        try{
            Glide.with(SettingActivity.this)
                    .load(user.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avtUser);
            if (loginByFB == true)
                nameUser.setText(user.getFullNameFB());
            else
                nameUser.setText(user.getFullName());
        } catch(Exception e){}
    }

    private void addActionBottomNavigationView() {
        Intent intent;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_setting);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_list_tour:
                        intent=new Intent(SettingActivity.this,ListTourActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_recents:
                        intent=new Intent(SettingActivity.this, UserListTourActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_map:
                        Intent intentMap =new Intent(SettingActivity.this,LocationMapsActivity.class);
                        startActivity(intentMap);
                        finish();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(SettingActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void addSignOutEvent() {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                boolean loginByFB = sharedPreferences.getBoolean("LoginByFB", false);
                editor.remove("token");
                editor.remove("userId");
                editor.commit();
                if (loginByFB == true){
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                            .Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            LoginManager.getInstance().logOut();
                        }
                    }).executeAsync();
                }
                Intent intent=new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControl() {
        avtUser = findViewById(R.id.avtUser);
        nameUser = findViewById(R.id.nameUser);
        editProfile = findViewById(R.id.editProfile);
        signout = findViewById(R.id.SignOutButton);
    }
}
