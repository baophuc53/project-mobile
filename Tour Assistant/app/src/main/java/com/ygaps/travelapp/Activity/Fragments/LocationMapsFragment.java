package com.ygaps.travelapp.Activity.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ygaps.travelapp.Activity.PointInfoActivity;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Coord;
import com.ygaps.travelapp.Object.CoordinateSet;
import com.ygaps.travelapp.Object.SuggestStopPoint;
import com.ygaps.travelapp.model.SuggestStopPointRequest;
import com.ygaps.travelapp.model.SuggestStopPointResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocationMapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private boolean mLocationPermissionsGranted = false;
    final List<LatLng> suggestPoints = new ArrayList<>();
    final List<Marker> markerList = new ArrayList<>();
    private static List<SuggestStopPoint> suggestStopPoints = new ArrayList<>();
    private BitmapDescriptor cultery = null, hotel = null, parking = null, other = null;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Activity currentActivity;
    SearchView searchView;
    Address address;
    Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_location_maps, container, false);
        currentActivity=getActivity();
        getLocationPermission();
        cultery = bitmapDescriptorFromVector(currentActivity , R.drawable.ic_cutlery);
        hotel = bitmapDescriptorFromVector(currentActivity , R.drawable.ic_hotel);
        parking = bitmapDescriptorFromVector(currentActivity , R.drawable.ic_parking);
        other = bitmapDescriptorFromVector(currentActivity , R.drawable.ic_24_hours);
        if (mLocationPermissionsGranted) {
            initMap();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        }
        return view;
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Activity context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    private void initMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.gps_map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(currentActivity.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(currentActivity.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(currentActivity,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(currentActivity,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final ProgressDialog progress = new ProgressDialog(currentActivity);
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
                    suggestStopPoints = suggestStopPointResponse.getStopPoints();
                    for (SuggestStopPoint s : suggestStopPoints) {
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
                            else if (s.getServiceTypeId() == 2)
                                marker.setIcon(hotel);
                            else if (s.getServiceTypeId() == 3)
                                marker.setIcon(parking);
                            else if (s.getServiceTypeId() == 4)
                                marker.setIcon(other);
                            marker.setTag(s);
                            markerList.add(marker);
                        }
                    }
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                }
            });

        }
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                final SuggestStopPoint s = (SuggestStopPoint) marker.getTag();
                Intent intent = new Intent(currentActivity, PointInfoActivity.class);
                int id;
                id = s.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        searchView = currentActivity.findViewById(R.id.search_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = new ArrayList<>();
                Geocoder geocoder = new Geocoder(currentActivity);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size()>0) {
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(location)
                            .snippet(address.getAddressLine(0)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }
                else {
                    Toast.makeText(currentActivity, "Fail", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
