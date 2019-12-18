package com.ygaps.travelapp.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.adapter.TourAdapters;
import com.ygaps.travelapp.model.ListTourRequest;
import com.ygaps.travelapp.model.ListTourResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ygaps.travelapp.Activity.Constants.PAGE_NUM;
import static com.ygaps.travelapp.Activity.Constants.ROW_PER_PAGE;

public class ListTourActivity extends AppCompatActivity {

    Tour tour;
    ArrayList<Tour> toursList = new ArrayList<Tour>();
    TourAdapters tourAdapters;
    ListView lvTours;
    TextView totalTour;
    Button addTourbtn;
    SearchView search;
    SwipeRefreshLayout pullToRefresh;

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
        addEvent();
        addResfreshEvent();
        addActionBottomNavigationView();
        addEventSearch();
        addEventClickTour();
    }

    private void addEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String json = sharedPreferences.getString("listTour", "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            ListTourResponse listTour = gson.fromJson(json, ListTourResponse.class);
            tourAdapters = new TourAdapters(ListTourActivity.this,
                    R.layout.items_listtour_layout, (ArrayList<Tour>) listTour.getTours());
            lvTours.setAdapter(tourAdapters);
            if (listTour.getTotal() == 1)
                totalTour.setText(listTour.getTotal().toString().concat(" trip"));
            else
                totalTour.setText(listTour.getTotal().toString().concat(" trips"));
        }
    }


    private void addResfreshEvent() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Show();
                pullToRefresh.setRefreshing(false);
            }
        });
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
                        intent = new Intent(ListTourActivity.this, UserListTourActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                    case R.id.action_map:
                        Intent intentMap = new Intent(ListTourActivity.this, LocationMapsActivity.class);
                        startActivity(intentMap);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(ListTourActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        intent = new Intent(ListTourActivity.this, SettingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void Show() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        tour = new Tour();
        ListTourRequest request = new ListTourRequest();
        request.setPageNum(PAGE_NUM);
        request.setRowPerPage(ROW_PER_PAGE);
        UserService userService;

        SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");


        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getListTour(request.getRowPerPage(),
                request.getPageNum(),
                request.getOrderBy(),
                request.isDesc
                , new Callback<ListTourResponse>() {
                    @Override
                    public void success(ListTourResponse listTourResponse, Response response) {  // To dismiss the dialog
                        progress.dismiss();

                        SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listTourResponse);
                        editor.putString("listTour", json);
                        editor.commit();

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
                        progress.dismiss();
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
        pullToRefresh = findViewById(R.id.pullToRefresh);
    }
}
