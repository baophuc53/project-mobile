package com.ygaps.travelapp.Activity.Fragments;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Activity.CreateTourActivity;
import com.ygaps.travelapp.Activity.DetailTourActivity;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.adapter.TourAdapters;
import com.ygaps.travelapp.model.ListTourRequest;
import com.ygaps.travelapp.model.ListTourResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserListTourFragment extends Fragment {

    Tour tour;
    TourAdapters tourAdapters;
    ListView lvTours;
    TextView totalTour;
    Button addTourbtn;
    SearchView search;
    Activity currentActivity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_tour, container, false);
        addControls(view);
        Show();
        addEventSearch();
        addEventClickTour();
        return view;
    }
    private void addEventSearch() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tourAdapters.filterTag(newText);
                return false;
            }
        });
    }



    private void addEventClickTour() {
        lvTours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(currentActivity, DetailTourActivity.class);
                intent.putExtra("tourId", tourAdapters.getItem(position).getId());
                intent.putExtra("isMyTour",true);
                startActivity(intent);
            }
        });
    }
    private void Show() {
        currentActivity=getActivity();
        final ProgressDialog progress = new ProgressDialog(currentActivity);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        tour = new Tour();
        ListTourRequest request=new ListTourRequest();
        request.setPageNum(1);
        request.setRowPerPage(10000);
        UserService userService;

        SharedPreferences sharedPreferences=currentActivity.getSharedPreferences("Data",0);
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
        addTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, CreateTourActivity.class);
                startActivity(intent);
            }
        });
    }



    private void addControls(View view) {
        lvTours = view.findViewById(R.id.listTour);
        totalTour =  view.findViewById(R.id.total_tour);
        addTourbtn = view.findViewById(R.id.button_add_tour);
        search = view.findViewById(R.id.search_tour);
    }
}
