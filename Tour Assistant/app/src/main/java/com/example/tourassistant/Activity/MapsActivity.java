package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.StopPoint;
import com.example.tourassistant.model.StopPointRequest;
import com.example.tourassistant.model.StopPointResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    final Calendar cArriveTime = Calendar.getInstance();
    final Calendar cLeaveTime = Calendar.getInstance();
    private boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    LatLng Point = new LatLng(1, 1);
    final List<LatLng> listPoints = new ArrayList<>();
    final List<StopPoint> stopPoints = new ArrayList<>();
    final StopPoint stopPoint = new StopPoint();
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
        LatLng HCM = new LatLng(10.743702, 106.676026);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HCM, 15.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Point = point;
                mMap.addMarker(new MarkerOptions().position(point));
                addStopPoints();
            }
        });
        ImageButton list = findViewById(R.id.list_stop_point_btn);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (LatLng p : listPoints) {
                    mMap.addMarker(new MarkerOptions().position(p));
                }
            }
        });

        ImageButton submit = findViewById(R.id.submit_stop_point_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPoints.size()>0) {
                    Intent intent = getIntent();
                    StopPointRequest stopPointRequest = new StopPointRequest();
                    stopPointRequest.setStopPoints(stopPoints);
                    int tourId = intent.getIntExtra("tourId", 0);
                    stopPointRequest.setTourId(tourId);
                    final UserService userService;
                    userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                    userService.addStopPoints(stopPointRequest, new Callback<StopPointResponse>() {
                        @Override
                        public void success(StopPointResponse stopPointResponse, Response response) {
                            Toast.makeText(MapsActivity.this, "ok", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(MapsActivity.this, ListTourActivity.class);
                            startActivity(intent1);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            switch (error.getKind()) {
                                case HTTP:
                                    if (error.getResponse().getStatus() == 404)
                                        Toast.makeText(MapsActivity.this, "Tour không hợp lệ", Toast.LENGTH_SHORT).show();
                                    else if (error.getResponse().getStatus() == 403)
                                        Toast.makeText(MapsActivity.this, "Không được phép thêm điểm dừng", Toast.LENGTH_SHORT).show();
                                    else if (error.getResponse().getStatus() == 500)
                                        Toast.makeText(MapsActivity.this, "Server không thể tạo điểm dừng", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MapsActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MapsActivity.this, "Chưa có điểm dừng nào được thiết lập", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addStopPoints() {
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
        final EditText name = dialog.findViewById(R.id.edit_stop_point_name);
        final EditText service = dialog.findViewById(R.id.edit_service_type);
        final EditText address = dialog.findViewById(R.id.edit_address);
        final EditText province = dialog.findViewById(R.id.edit_province);
        final EditText maxCost = dialog.findViewById(R.id.edit_max_cost);
        final EditText minCost = dialog.findViewById(R.id.edit_min_cost);


        addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPoints.add(Point);
                stopPoint.setLat(Point.latitude);
                stopPoint.setLong(Point.longitude);
                stopPoint.setArrivalAt(cArriveTime.getTimeInMillis());
                stopPoint.setLeaveAt(cLeaveTime.getTimeInMillis());
                stopPoint.setMinCost(Long.parseLong(minCost.getText().toString()));
                stopPoint.setMaxCost(Long.parseLong(maxCost.getText().toString()));
                stopPoint.setName(name.getText().toString());
                stopPoint.setServiceTypeId(1);
                stopPoints.add(stopPoint);
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
                        cArriveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cArriveTime.set(Calendar.MINUTE, minute);
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
                        cArriveTime.set(Calendar.YEAR, year);
                        cArriveTime.set(Calendar.MONTH, month);
                        cArriveTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
                        cLeaveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cLeaveTime.set(Calendar.MINUTE, minute);
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
                        cLeaveTime.set(Calendar.YEAR, year);
                        cLeaveTime.set(Calendar.MONTH, month);
                        cLeaveTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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