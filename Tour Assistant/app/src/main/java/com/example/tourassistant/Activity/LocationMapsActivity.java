package com.example.tourassistant.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
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
    private Marker recentMarker;
    private BitmapDescriptor cultery = null, hotel = null, parking = null, other = null;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_maps);
        getLocationPermission();
        cultery = bitmapDescriptorFromVector(LocationMapsActivity.this , R.drawable.ic_cutlery);
        hotel = bitmapDescriptorFromVector(LocationMapsActivity.this , R.drawable.ic_hotel);
        parking = bitmapDescriptorFromVector(LocationMapsActivity.this , R.drawable.ic_parking);
        other = bitmapDescriptorFromVector(LocationMapsActivity.this , R.drawable.ic_24_hours);
        initMap();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Activity context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
                    for (SuggestStopPoint s : suggestStopPointResponse.getStopPoints()) {
                        LatLng p = new LatLng(Double.parseDouble(s.getLat()), Double.parseDouble(s.getLong()));
                        if (!suggestPoints.contains(p)) {
                            suggestPoints.add(p);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(p)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(s.getName())
                                    .snippet(s.getAddress()));

                            if (s.getServiceTypeId() == 1)
                                marker.setIcon(cultery);
                            else if(s.getServiceTypeId() == 2)
                                marker.setIcon(hotel);
                            else if(s.getServiceTypeId() == 3)
                                marker.setIcon(parking);
                            else if(s.getServiceTypeId() == 4)
                                marker.setIcon(other);
                            marker.setTag(s);
                            markerList.add(marker);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                final SuggestStopPoint s = (SuggestStopPoint) marker.getTag();
                Intent intent = new Intent(LocationMapsActivity.this, PointInfoActivity.class);
                int id;
                id = s.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (recentMarker != null)
                    recentMarker.remove();
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                List<Address> addresses = new ArrayList<>();
                Geocoder geocoder = new Geocoder(LocationMapsActivity.this);
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size()>0){
                    markerOptions.title(addresses.get(0).getAddressLine(0))
                                .snippet(addresses.get(0).getAddressLine(1));
                }
                recentMarker = mMap.addMarker(markerOptions);
            }
        });
    }
}
