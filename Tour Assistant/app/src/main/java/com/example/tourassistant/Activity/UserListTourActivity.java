package com.example.tourassistant.Activity;



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

public class UserListTourActivity extends AppCompatActivity {

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
        title.setText("My Tour");
        addControls();
        Show();
        addActionBottomNavigationView();
        addEventSearch();
        addEventClickTour();
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
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_recents);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_list_tour:
                        intent=new Intent(UserListTourActivity.this,ListTourActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        finish();
                        break;
                    case R.id.action_map:
                        Intent intentMap =new Intent(UserListTourActivity.this,LocationMapsActivity.class);
                        startActivity(intentMap);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(UserListTourActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        intent=new Intent(UserListTourActivity.this,SettingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void addEventClickTour() {
        lvTours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserListTourActivity.this, DetailTourActivity.class);
                intent.putExtra("tourId", tourAdapters.getItem(position).getId());
                startActivity(intent);
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
        ListTourRequest request=new ListTourRequest();
        request.setPageNum(1);
        request.setRowPerPage(10000);
        UserService userService;

        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String Token =sharedPreferences.getString("token","");


        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getUserTour(request.getPageNum(),
                request.getRowPerPage().toString()
                , new Callback<ListTourResponse>() {
                    @Override
                    public void success(ListTourResponse listTourResponse, Response response) {
                        progress.dismiss();
                        if (listTourResponse.getTotal() == 1)
                            totalTour.setText(listTourResponse.getTotal().toString().concat(" trip"));
                        else
                            totalTour.setText(listTourResponse.getTotal().toString().concat(" trips"));
                        tourAdapters = new TourAdapters(UserListTourActivity.this,
                                R.layout.items_listtour_layout, (ArrayList<Tour>) listTourResponse.getTours());
                        lvTours.setAdapter(tourAdapters);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                        switch (error.getKind()) {
                            case HTTP:
                                if (error.getResponse().getStatus() == 500)
                                    Toast.makeText(UserListTourActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                                break;
                            case NETWORK:
                            case UNEXPECTED:
                                Toast.makeText(UserListTourActivity.this, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(UserListTourActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(UserListTourActivity.this, CreateTourActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        lvTours = (ListView) findViewById(R.id.listTour);
        totalTour = (TextView) findViewById(R.id.total_tour);
        addTourbtn = (Button) findViewById(R.id.button_add_tour);
        search = (SearchView) findViewById(R.id.search_tour);
    }
}
