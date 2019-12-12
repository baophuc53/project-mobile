package com.example.tourassistant.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.Tour;
import com.example.tourassistant.adapter.TourAdapters;
import com.example.tourassistant.model.ListTourRequest;
import com.example.tourassistant.model.ListTourResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    Tour tour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportActionBar().hide();


        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String token=sharedPreferences.getString("token","");
        if(token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Show();
        }

    }

    private void Show() {

        tour = new Tour();
        ListTourRequest request=new ListTourRequest();
        request.setPageNum(1);
        request.setRowPerPage(10000);
        UserService userService;

        final SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String Token =sharedPreferences.getString("token","");


        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getListTour(request.getRowPerPage(),
                request.getPageNum(),
                request.getOrderBy(),
                request.isDesc
                , new Callback<ListTourResponse>() {
                    @Override
                    public void success(ListTourResponse listTourResponse, Response response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listTourResponse);
                        editor.putString("listTour", json);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, ListTourActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        switch (error.getKind()) {
                            case HTTP:
                                if (error.getResponse().getStatus() == 500)
                                    Toast.makeText(MainActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                                break;
                            case NETWORK:
                            case UNEXPECTED:
                                Toast.makeText(MainActivity.this, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
