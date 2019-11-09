package com.example.tourassistant.Activity;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourassistant.Activity.R;
import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.Tour;
import com.example.tourassistant.adapter.TourAdapters;
import com.example.tourassistant.model.ListTourRequest;
import com.example.tourassistant.model.ListTourResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.tourassistant.Activity.Constants.defaultToken;

public class ListTourActivity extends AppCompatActivity {

    Tour tour;
    ArrayList<Tour> toursList = new ArrayList<Tour>();
    TourAdapters tourAdapters;
    ListView lvTours;
    TextView totalTour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour);
        //title vao giua
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Tour Assistant");
        addControls();
        Show();
        addActionBottomNavigationView();

    }

    private void addActionBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_list_tour:
                        Toast.makeText(ListTourActivity.this, "List Tours", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_recents:
                        Toast.makeText(ListTourActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                        Toast.makeText(ListTourActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(ListTourActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        Toast.makeText(ListTourActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void Show() {
        tour = new Tour();
        ListTourRequest request=new ListTourRequest();
        request.setPageNum(1);
        request.setRowPerPage(1000);
        UserService userService;

        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
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
                if (listTourResponse.getTotal() == 1)
                    totalTour.setText(listTourResponse.getTotal().toString().concat(" trip"));
                else
                    totalTour.setText(listTourResponse.getTotal().toString().concat(" trips"));
                tourAdapters = new TourAdapters(ListTourActivity.this,
                        R.layout.items_listtour_layout, (ArrayList<Tour>) listTourResponse.getTours());
                lvTours.setAdapter(tourAdapters);
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 500)
                            Toast.makeText(ListTourActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                        break;
                    case NETWORK:
                    case UNEXPECTED:
                        Toast.makeText(ListTourActivity.this, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(ListTourActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addControls() {
        lvTours = (ListView) findViewById(R.id.listTour);
        totalTour = (TextView) findViewById(R.id.total_tour);
    }
}
