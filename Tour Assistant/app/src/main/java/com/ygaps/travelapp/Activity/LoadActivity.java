package com.ygaps.travelapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ygaps.travelapp.Activity.Fragments.ListTourFragment;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.model.ListTourRequest;
import com.ygaps.travelapp.model.ListTourResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoadActivity extends AppCompatActivity {

    Tour tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity);

        getSupportActionBar().hide();
        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String token=sharedPreferences.getString("token","");
        if(token.isEmpty()) {
            Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(LoadActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
