package com.ygaps.travelapp.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.CoordinateSet;
import com.ygaps.travelapp.Object.Coord;
import com.ygaps.travelapp.Object.StopPoint;
import com.ygaps.travelapp.Object.SuggestStopPoint;
import com.ygaps.travelapp.model.StopPointRequest;
import com.ygaps.travelapp.model.StopPointResponse;
import com.ygaps.travelapp.model.SuggestStopPointRequest;
import com.ygaps.travelapp.model.SuggestStopPointResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
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
    final Calendar cSuggestArriveTime = Calendar.getInstance();
    final Calendar cSuggestLeaveTime = Calendar.getInstance();
    private boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    LatLng Point = new LatLng(1, 1);
    final List<LatLng> listPoints = new ArrayList<>();
    final List<LatLng> suggestPoints = new ArrayList<>();
    final List<Marker> markerList = new ArrayList<>();
    final List<StopPoint> stopPoints = new ArrayList<>();
    private BitmapDescriptor cultery = null, hotel = null, parking = null, other = null;
    private StopPoint stopPoint = null;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        getLocationPermission();
        cultery = bitmapDescriptorFromVector(MapsActivity.this , R.drawable.ic_cutlery);
        hotel = bitmapDescriptorFromVector(MapsActivity.this , R.drawable.ic_hotel);
        parking = bitmapDescriptorFromVector(MapsActivity.this , R.drawable.ic_parking);
        other = bitmapDescriptorFromVector(MapsActivity.this , R.drawable.ic_24_hours);
        if (mLocationPermissionsGranted) {
            initMap();
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Activity context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                List<Address> addresses = new ArrayList<>();
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                MarkerOptions markerOptions = new MarkerOptions().position(point);
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size()>0){
                    markerOptions.title(addresses.get(0).getAddressLine(0))
                            .snippet(addresses.get(0).getAddressLine(1));
                }
                Point = point;
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
                addStopPoints(marker);
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
//                            Intent intent1 = new Intent(MapsActivity.this, ListTourFragment.class);
//                            startActivity(intent1);
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                final SuggestStopPoint s = (SuggestStopPoint) marker.getTag();
                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.layout_suggest_stop_point);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button addStopPoint = dialog.findViewById(R.id.suggest_StopPointBtn);
                ImageButton cancel = dialog.findViewById(R.id.cancel_suggest_dialog);
                final EditText arriveTime = dialog.findViewById(R.id.edit_suggest_arrive_time);
                final EditText arriveDate = dialog.findViewById(R.id.edit_suggest_arrive_date);
                final EditText leaveTime = dialog.findViewById(R.id.edit_suggest_leave_time);
                final EditText leaveDate = dialog.findViewById(R.id.edit_suggest_leave_date);
                final EditText maxCost = dialog.findViewById(R.id.edit_suggest_max_cost);
                final EditText minCost = dialog.findViewById(R.id.edit_suggest_min_cost);
                TextView name = dialog.findViewById(R.id.suggest_name);
                TextView address = dialog.findViewById(R.id.edit_suggest_address);
                name.setText(s.getName());
                address.setText(s.getAddress());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                addStopPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean correct = true;

                        if (TextUtils.isEmpty(arriveDate.getText().toString())){
                            correct = false;
                            arriveDate.setError("Vui lòng chọn ngày đến");
                        }
                        if (TextUtils.isEmpty(leaveDate.getText().toString())) {
                            correct = false;
                            leaveDate.setError("Vui lòng chọn ngày đi");
                        }
                        if (TextUtils.isEmpty(minCost.getText().toString())) {
                            correct = false;
                            minCost.setError("");
                        }
                        if (TextUtils.isEmpty(maxCost.getText().toString())) {
                            correct = false;
                            minCost.setError("");
                        }

                        if(correct) {
                            LatLng suggestPoint = marker.getPosition();
                            listPoints.add(suggestPoint);
                            stopPoint = new StopPoint();
                            stopPoint.setLat(suggestPoint.latitude);
                            stopPoint.setLong(suggestPoint.longitude);
                            stopPoint.setArrivalAt(cSuggestArriveTime.getTimeInMillis());
                            stopPoint.setLeaveAt(cSuggestLeaveTime.getTimeInMillis());
                            stopPoint.setMinCost(Long.parseLong(minCost.getText().toString()));
                            stopPoint.setMaxCost(Long.parseLong(maxCost.getText().toString()));
                            stopPoint.setName(s.getName());
                            stopPoint.setAddress(s.getAddress());
                            stopPoint.setProvinceId(s.getProvinceId());
                            stopPoint.setServiceTypeId(s.getServiceTypeId());
                            stopPoints.add(stopPoint);
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                            dialog.cancel();
                        }
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
                                cSuggestArriveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cSuggestArriveTime.set(Calendar.MINUTE, minute);
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
                                cSuggestArriveTime.set(Calendar.YEAR, year);
                                cSuggestArriveTime.set(Calendar.MONTH, month);
                                cSuggestArriveTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                arriveDate.setText(String.valueOf(cDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                                        .concat(String.valueOf(cDate.get(Calendar.MONTH)+1)).concat("/")
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
                                cSuggestLeaveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cSuggestLeaveTime.set(Calendar.MINUTE, minute);
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
                                cSuggestLeaveTime.set(Calendar.YEAR, year);
                                cSuggestLeaveTime.set(Calendar.MONTH, month);
                                cSuggestLeaveTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                leaveDate.setText(String.valueOf(cDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                                        .concat(String.valueOf(cDate.get(Calendar.MONTH)+1)).concat("/")
                                        .concat(String.valueOf(cDate.get(Calendar.YEAR))));
                            }
                        };
                        new DatePickerDialog(MapsActivity.this, pickLeaveDate, cDate.get(Calendar.YEAR),
                                cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
            }
        });
    }

    private void addStopPoints(final Marker marker) {
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
        final Spinner service = dialog.findViewById(R.id.edit_service_type);
        final EditText address = dialog.findViewById(R.id.edit_address);
        final Spinner province = dialog.findViewById(R.id.edit_province);
        final EditText maxCost = dialog.findViewById(R.id.edit_max_cost);
        final EditText minCost = dialog.findViewById(R.id.edit_min_cost);

        address.setText(marker.getTitle());
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(MapsActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.services));
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        service.setAdapter(serviceAdapter);

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(MapsActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.provinces));
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        province.setAdapter(provinceAdapter);

        addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = true;
                if (TextUtils.isEmpty(name.getText().toString())){
                    correct = false;
                    name.setError("Vui lòng điền tên chuyến đi");
                }
                if (TextUtils.isEmpty(arriveDate.getText().toString())){
                    correct = false;
                    arriveDate.setError("Vui lòng chọn ngày đến");
                }
                if (TextUtils.isEmpty(leaveDate.getText().toString())){
                    correct = false;
                    leaveDate.setError("Vui lòng chọn ngày đi");
                }
                if (TextUtils.isEmpty(address.getText().toString())){
                    correct = false;
                    leaveDate.setError("Nhập địa chỉ");
                }
                if (TextUtils.isEmpty(minCost.getText().toString())) {
                    correct = false;
                    minCost.setError("");
                }
                if (TextUtils.isEmpty(maxCost.getText().toString())) {
                    correct = false;
                    minCost.setError("");
                }
                if (correct) {
                    listPoints.add(Point);
                    stopPoint = new StopPoint();
                    stopPoint.setLat(Point.latitude);
                    stopPoint.setLong(Point.longitude);
                    stopPoint.setArrivalAt(cArriveTime.getTimeInMillis());
                    stopPoint.setLeaveAt(cLeaveTime.getTimeInMillis());
                    stopPoint.setMinCost(Long.parseLong(minCost.getText().toString()));
                    stopPoint.setMaxCost(Long.parseLong(maxCost.getText().toString()));
                    stopPoint.setName(name.getText().toString());
                    stopPoint.setAddress(address.getText().toString());
                    stopPoint.setProvinceId(province.getSelectedItemPosition() + 1);
                    stopPoint.setServiceTypeId(service.getSelectedItemPosition() + 1);
                    stopPoints.add(stopPoint);
                    markerList.add(marker);
                    dialog.cancel();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
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
                                .concat(String.valueOf(cDate.get(Calendar.MONTH)+1)).concat("/")
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
                                .concat(String.valueOf(cDate.get(Calendar.MONTH)+1)).concat("/")
                                .concat(String.valueOf(cDate.get(Calendar.YEAR))));
                    }
                };
                new DatePickerDialog(MapsActivity.this, pickLeaveDate, cDate.get(Calendar.YEAR),
                        cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
}