package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        getLocationPermission();
        initMap();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        Toast.makeText(MapsActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        LatLng HCM = new LatLng(10.743702, 106.676026);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HCM, 15.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                addStopPoint();
            }
        });
    }

    private void addStopPoint() {
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.layout_stop_point);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button addStopPoint = dialog.findViewById(R.id.StopPointBtn);
        ImageButton cancel = dialog.findViewById(R.id.cancel_dialog);
        final EditText arriveTime = dialog.findViewById(R.id.edit_arrive_time);
        final EditText arriveDate = dialog.findViewById(R.id.edit_arrive_date);
        final EditText leaveTime = dialog.findViewById(R.id.edit_leave_time);
        final EditText leaveDate = dialog.findViewById(R.id.edit_leave_date);

        addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        arriveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cTime = Calendar.getInstance();
                final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cTime.set(Calendar.MINUTE, minute);
                        arriveTime.setText(String.valueOf(cTime.get(Calendar.HOUR_OF_DAY)).concat(":")
                                .concat(String.valueOf(cTime.get(Calendar.MINUTE))));
                    }
                };
                new TimePickerDialog(MapsActivity.this, timeSetListener, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), true).show();
            }
        });

        arriveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cDate = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener pickArriveDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cDate.set(Calendar.YEAR, year);
                        cDate.set(Calendar.MONTH, month);
                        cDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        arriveDate.setText(String.valueOf(cDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                                .concat(String.valueOf(cDate.get(Calendar.MONTH))).concat("/")
                                .concat(String.valueOf(cDate.get(Calendar.YEAR))));
                    }
                };
                        new DatePickerDialog(MapsActivity.this, pickArriveDate, cDate.get(Calendar.YEAR),
                                cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        leaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cTime = Calendar.getInstance();
                final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cTime.set(Calendar.MINUTE, minute);
                        leaveTime.setText(String.valueOf(cTime.get(Calendar.HOUR_OF_DAY)).concat(":")
                                .concat(String.valueOf(cTime.get(Calendar.MINUTE))));
                    }
                };
                new TimePickerDialog(MapsActivity.this, timeSetListener, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), true).show();
            }
        });
        leaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cDate = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener pickLeaveDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cDate.set(Calendar.YEAR, year);
                        cDate.set(Calendar.MONTH, month);
                        cDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        leaveDate.setText(String.valueOf(cDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                                .concat(String.valueOf(cDate.get(Calendar.MONTH))).concat("/")
                                .concat(String.valueOf(cDate.get(Calendar.YEAR))));
                    }
                };
                new DatePickerDialog(MapsActivity.this, pickLeaveDate, cDate.get(Calendar.YEAR),
                        cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
}