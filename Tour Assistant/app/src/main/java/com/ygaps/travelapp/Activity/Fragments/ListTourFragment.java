package com.ygaps.travelapp.Activity.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ygaps.travelapp.Activity.CreateTourActivity;
import com.ygaps.travelapp.Activity.DetailTourActivity;
import com.ygaps.travelapp.Activity.LocationMapsActivity;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.adapter.TourAdapters;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.ListTourRequest;
import com.ygaps.travelapp.model.ListTourResponse;
import com.ygaps.travelapp.model.TokenRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ygaps.travelapp.Activity.Constants.PAGE_NUM;
import static com.ygaps.travelapp.Activity.Constants.ROW_PER_PAGE;

public class ListTourFragment extends Fragment {

    Tour tour;
    TourAdapters tourAdapters;
    ListView lvTours;
    TextView totalTour;
    Button addTourbtn;
    SearchView search;
    SwipeRefreshLayout pullToRefresh;
    Activity currentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_list_tour, container, false);
        addControls(view);
        GetData();
        addResfreshEvent();
        addEventSearch();
        addEventClickTour();
        return view;
    }

    private void GetData() {
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("Data", 0);
        String json = sharedPreferences.getString("listTour", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            ListTourResponse listTour = gson.fromJson(json, ListTourResponse.class);
            tourAdapters = new TourAdapters(currentActivity,
                    R.layout.items_listtour_layout, (ArrayList<Tour>) listTour.getTours());
            lvTours.setAdapter(tourAdapters);
            if (listTour.getTotal() == 1)
                totalTour.setText(listTour.getTotal().toString().concat(" trip"));
            else
                totalTour.setText(listTour.getTotal().toString().concat(" trips"));
        }
        else
            Show();
    }


    private void addResfreshEvent() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Show();
                pullToRefresh.setRefreshing(false);
            }
        });

        addTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, CreateTourActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addEventClickTour() {
        lvTours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(currentActivity, DetailTourActivity.class);
                intent.putExtra("tourId", tourAdapters.getItem(position).getId());

                SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("Data", 0);
                String userId = sharedPreferences.getString("userId", "");
                String hostID=""+tourAdapters.getItem(position).getId();
                if(userId==hostID)
                    intent.putExtra("isMyTour",true);
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


    private void Show() {

        final ProgressDialog progress = new ProgressDialog(currentActivity);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        tour = new Tour();
        ListTourRequest request = new ListTourRequest();
        request.setPageNum(PAGE_NUM);
        request.setRowPerPage(ROW_PER_PAGE);
        UserService userService;
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getListTour(request.getRowPerPage(),
                request.getPageNum(),
                request.getOrderBy(),
                request.isDesc
                , new Callback<ListTourResponse>() {
                    @Override
                    public void success(ListTourResponse listTourResponse, Response response) {  // To dismiss the dialog
                        progress.dismiss();

                        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("Data", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listTourResponse);
                        editor.putString("listTour", json);
                        editor.commit();

                        if (listTourResponse.getTotal() == 1)
                            totalTour.setText(listTourResponse.getTotal().toString().concat(" trip"));
                        else
                            totalTour.setText(listTourResponse.getTotal().toString().concat(" trips"));
                        tourAdapters = new TourAdapters(currentActivity,
                                R.layout.items_listtour_layout, (ArrayList<Tour>) listTourResponse.getTours());
                        lvTours.setAdapter(tourAdapters);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                        switch (error.getKind()) {
                            case HTTP:
                                if (error.getResponse().getStatus() == 500)
                                    Toast.makeText(currentActivity, "Lỗi server", Toast.LENGTH_LONG).show();
                                break;
                            case NETWORK:
                            case UNEXPECTED:
                                Toast.makeText(currentActivity, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(currentActivity, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void addControls(View view) {
        currentActivity=getActivity();
        lvTours = view.findViewById(R.id.listTour);
        totalTour = view.findViewById(R.id.total_tour);
        addTourbtn = view.findViewById(R.id.button_add_tour);
        search = view.findViewById(R.id.search_tour);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
    }

}
