package com.ygaps.travelapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ygaps.travelapp.Activity.Fragments.ListTourFragment;
import com.ygaps.travelapp.Activity.Fragments.LocationMapsFragment;
import com.ygaps.travelapp.Activity.Fragments.NotificationFragment;
import com.ygaps.travelapp.Activity.Fragments.SettingFragment;
import com.ygaps.travelapp.Activity.Fragments.UserListTourFragment;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.TokenRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    Fragment fragment,listTour,userListTour,notification,setting,mapFragment;
    TextView title;
    BottomNavigationView bottomNavigationView;
    ArrayList<Integer> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //title vao giua
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        title = findViewById(R.id.actionbar_textview);
        title.setText("Tour Assistant");

        listTour=new ListTourFragment();
        userListTour=new UserListTourFragment();
        notification=new NotificationFragment();
        setting=new SettingFragment();
        mapFragment=new LocationMapsFragment();
        init();
        Authorize();
        sendRegistrationToServer();
        addActionBottomNavigationView();
    }

    private void init() {
        Intent intent=getIntent();
        String flag=intent.getStringExtra("fragment");
        if(flag!=null)
            switch (flag)
            {
                case "UserListTour":
                    fragment=userListTour;
                    break;
                case "Notification":
                    fragment=notification;
                    break;
                case "Setting":
                    fragment=setting;
                    break;
                case "Map":
                    fragment=mapFragment;
                    break;
                default :
                    fragment=listTour;
            }
        else fragment=listTour;
        loadFragment(fragment);
    }

    @Override
    public  void onBackPressed(){
        if(list.size()>1) {
            bottomNavigationView.setSelectedItemId(list.get(list.size() - 2));
            list.remove(list.size() - 1);
        }else
        finish();
    }

    private void Authorize() {
        SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");


        MyAPIClient.getInstance().setAccessToken(Token);
    }


    private void addActionBottomNavigationView() {
        list=new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_list_tour);
        list.add(R.id.action_list_tour);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                list.add(R.id.action_list_tour);
                getSupportActionBar().show();
                switch (item.getItemId()) {
                    case R.id.action_list_tour:
                        fragment=listTour;
                        title.setText("Tour Assistant");
                        break;
                    case R.id.action_recents:
                        fragment=userListTour;
                        title.setText("My Tour");
                        break;
                    case R.id.action_map:
                        fragment=mapFragment;
                        getSupportActionBar().hide();
                        break;
                    case R.id.action_notifications:
                        fragment=notification;
                        title.setText("Notification");
                        break;
                    case R.id.action_setting:
                        fragment=setting;
                        title.setText("Setting");
                        break;
                }
                if(fragment!=null)
                    loadFragment(fragment);
                return true;
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void sendRegistrationToServer() {
        String token = FirebaseInstanceId.getInstance().getToken();
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setFcmToken(token);
        tokenRequest.setPlatform(1);
        tokenRequest.setAppVersion("1.0");

        String deviceId;
        deviceId = getDeviceId(this);
        tokenRequest.setDeviceId(deviceId);

        UserService userService= MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.RegisterToken(tokenRequest, new Callback<DefaultResponse>() {
            @Override
            public void success(DefaultResponse defaultResponse, Response response) {
                Log.i("register firebase", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("register firebase", "failure");
            }
        });
    }

    private String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return androidId;
    }
}
