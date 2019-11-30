package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        addSignOutEvent();
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
                        intent=new Intent(SettingActivity.this,UserListTour.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_map:
                        Toast.makeText(SettingActivity.this, "Map", Toast.LENGTH_SHORT).show();
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
