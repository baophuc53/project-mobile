package com.example.tourassistant.Activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class ListTourActivity extends AppCompatActivity {

    Tour tour;
    ArrayList<Tour> toursList = new ArrayList<Tour>();
    TourAdapters tourAdapters;
    ListView lvTours;
    TextView totalTour;
    Button addTourbtn;
    SearchView search;
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
        addEventSearch();
        addEventClickTour();
    }

    private void addEventClickTour() {
        lvTours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListTourActivity.this, DetailTourActivity.class);
                intent.putExtra("tourId", tourAdapters.getItem(position).getId());
                
                startActivity(intent);
            }
        });
    }

    private void addEventSearch() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                tourAdapters.filterTag(text);
                return false;
            }
        });
    }

    private void addActionBottomNavigationView() {
        Intent intent;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_list_tour);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        intent=new Intent(ListTourActivity.this, UserListTourActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_map:
                        Toast.makeText(ListTourActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(ListTourActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        intent =new Intent(ListTourActivity.this,SettingActivity.class);
                        startActivity(intent);
                        finish();
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
        request.setRowPerPage(10000);
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
        addTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCreateTourActivity();
            }
        });
    }

    private void OpenCreateTourActivity() {
        Intent intent = new Intent(ListTourActivity.this, CreateTourActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        lvTours = (ListView) findViewById(R.id.listTour);
        totalTour = (TextView) findViewById(R.id.total_tour);
        addTourbtn = (Button) findViewById(R.id.button_add_tour);
        search = (SearchView) findViewById(R.id.search_tour);
    }
}
