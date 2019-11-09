package com.example.tourassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
<<<<<<< Updated upstream:Tour Assistant/app/src/main/java/com/example/tourassistant/MainActivity.java
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
=======
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

>>>>>>> Stashed changes:Tour Assistant/app/src/main/java/com/example/tourassistant/Activity/ListTourActivity.java

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.Tour;
import com.example.tourassistant.adapter.TourAdapters;
import com.example.tourassistant.model.ListTourRequest;
import com.example.tourassistant.model.ListTourResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.tourassistant.Activity.Constants.defaultToken;

public class MainActivity extends AppCompatActivity {

    Tour tour;
    ArrayList<Tour> toursList = new ArrayList<Tour>();
    TourAdapters tourAdapters;
    ListView lvTours;
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
<<<<<<< Updated upstream:Tour Assistant/app/src/main/java/com/example/tourassistant/MainActivity.java
        tour = new Tour("HCM - Nha Trang",  10, 15, 0, 1, 2, 3, false, 10, 5,10000, 50000, "https://cdnimg.vietnamplus.vn/uploaded/fsmsr/2018_12_03/31.jpg");
        toursList.add(tour);
        tourAdapters = new TourAdapters(MainActivity.this, R.layout.listview_tour_layout, toursList);
        lvTours.setAdapter(tourAdapters);
=======
        tour = new Tour();
        ListTourRequest request=new ListTourRequest();
        request.setPageNum(1);
        request.setRowPerPage(183);
        UserService userService;
        userService = MyAPIClient.getInstance(defaultToken).getAdapter().create(UserService.class);
        userService.getListTour(request.getRowPerPage(),
                request.getPageNum(),
                request.getOrderBy(),
                request.isDesc
                , new Callback<ListTourResponse>() {
            @Override
            public void success(ListTourResponse listTourResponse, Response response) {
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
>>>>>>> Stashed changes:Tour Assistant/app/src/main/java/com/example/tourassistant/Activity/ListTourActivity.java
    }

    private void addControls() {
        lvTours = (ListView) findViewById(R.id.listTour);
    }
}
