package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.Coord;
import com.example.tourassistant.Object.CoordinateSet;
import com.example.tourassistant.Object.SuggestStopPoint;
import com.example.tourassistant.model.SuggestStopPointRequest;
import com.example.tourassistant.model.SuggestStopPointResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Button btn_start, btn_stop;
    private ImageButton btn_gps;
    private BroadcastReceiver broadcastReceiver;
    private boolean mLocationPermissionsGranted = false;
    final List<LatLng> suggestPoints = new ArrayList<>();
    final List<Marker> markerList = new ArrayList<>();
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_maps);
        getLocationPermission();
        initMap();
        addActionBottomNavigationView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gps_map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        mMap = googleMap;
        LatLng HCM = new LatLng(10.743702, 106.676026);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HCM, 15.0f));
        mMap.setMyLocationEnabled(true);
        if (mLocationPermissionsGranted) {
            final SuggestStopPointRequest suggestStopPointRequest = new SuggestStopPointRequest();
            Coord point1 = new Coord();
            Coord point2 = new Coord();
            List<Coord> coords = new ArrayList<>();
            CoordinateSet coordinateSet = new CoordinateSet();
            List<CoordinateSet> coordList = new ArrayList<>();
            point1.setLat(23.392954);
            point1.setLong(101.284320);
            point2.setLat(8.305351);
            point2.setLong(110.502890);
            coords.add(point1);
            coords.add(point2);
            coordinateSet.setCoordinateSet(coords);
            coordList.add(coordinateSet);
            suggestStopPointRequest.setHasOneCoordinate(false);
            suggestStopPointRequest.setCoordList(coordList);
            final UserService userService;
            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
            userService.suggestStopPoint(suggestStopPointRequest, new Callback<SuggestStopPointResponse>() {
                @Override
                public void success(SuggestStopPointResponse suggestStopPointResponse, Response response) {
                    progress.dismiss();
                    for (SuggestStopPoint s : suggestStopPointResponse.getStopPoints()) {
                        LatLng p = new LatLng(Double.parseDouble(s.getLat()), Double.parseDouble(s.getLong()));
                        if (!suggestPoints.contains(p)) {
                            suggestPoints.add(p);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(p)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(s.getName())
                                    .snippet(s.getAddress()));
                            marker.setTag(s);
                            markerList.add(marker);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                }
            });
        }
    }

    private void addActionBottomNavigationView() {
        Intent intent;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        intent=new Intent(LocationMapsActivity.this, UserListTourActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        finish();
                        break;
                    case R.id.action_list_tour:
                        Intent intentMap =new Intent(LocationMapsActivity.this,ListTourActivity.class);
                        startActivity(intentMap);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        finish();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(LocationMapsActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        intent =new Intent(LocationMapsActivity.this,SettingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

}
