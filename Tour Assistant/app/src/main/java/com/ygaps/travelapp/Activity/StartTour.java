package com.ygaps.travelapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.CoordMember;
import com.ygaps.travelapp.Object.Noti;
import com.ygaps.travelapp.Object.StopPoint;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.GPSServiceRequest;
import com.ygaps.travelapp.model.GetNotiResponse;
import com.ygaps.travelapp.model.ListReviewOfTourRequest;
import com.ygaps.travelapp.model.OnRoadNotification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class StartTour extends FragmentActivity implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    boolean mLocationPermissionsGranted = false;
    SearchView searchView;
    Address address;
    Marker marker;
    private FusedLocationProviderClient fusedLocationClient;
    Handler handler;
    Runnable getNoti;
    long tourId;
    String userId = "";
    UserService userService;
    private BitmapDescriptor cultery = null, hotel = null, parking = null, other = null;
    Handler GPS_Handler = new Handler();
    Runnable GPS_Runnable = null;
    List<StopPoint> stopPointList = new ArrayList<>();
    List<Marker> markers = new ArrayList<>();
    List<Marker> userMarkers = new ArrayList<>();
    ImageButton recordButton;
    MediaRecorder myAudioRecorder;
    MediaPlayer myAudioPlay;
    ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tour);
        getLocationPermission();
        cultery = bitmapDescriptorFromVector(StartTour.this , R.drawable.ic_cutlery);
        hotel = bitmapDescriptorFromVector(StartTour.this , R.drawable.ic_hotel);
        parking = bitmapDescriptorFromVector(StartTour.this , R.drawable.ic_parking);
        other = bitmapDescriptorFromVector(StartTour.this , R.drawable.ic_24_hours);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent =getIntent();
        tourId=intent.getLongExtra("Tour",0);
        userId=intent.getStringExtra("userId");
        stopPointList = (List<StopPoint>) intent.getSerializableExtra("StopPointList");
        addEvent();
        runThread();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Activity context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void runThread() {
        handler=new Handler();
        getNoti=new Runnable() {
            @Override
            public void run() {
                try{
                    final ListReviewOfTourRequest getNotiRequest=new ListReviewOfTourRequest();
                    getNotiRequest.setPageIndex(Constants.ROW_PER_PAGE);
                    getNotiRequest.setPageSize(Constants.PAGE_NUM);
                    getNotiRequest.setTourId(tourId);


                    userService.getNotiByTourId(getNotiRequest.getTourId(),
                            getNotiRequest.getPageIndex(),
                            getNotiRequest.getPageSize(), new Callback<GetNotiResponse>() {
                                @Override
                                public void success(GetNotiResponse getNotiResponses, Response response) {
                                    if(getNotiResponses.getNotifications()!=null)
                                    for (Noti notification:getNotiResponses.getNotifications())
                                    {
                                        MarkerOptions markerOptions=new MarkerOptions()
                                                .position(new LatLng(notification.getLat(),notification.getLong()))
                                                .anchor(0.5f, 0.5f);
                                        String title="",snippet="";
                                        float icon=0;
                                        switch(notification.getNotificationType())
                                        {
                                            case 1:
                                                title="Police";
                                                snippet="";
                                                icon= BitmapDescriptorFactory.HUE_YELLOW;
                                                break;
                                            case 2:
                                                title="Message";
                                                snippet=notification.getNote();
                                                icon=BitmapDescriptorFactory.HUE_BLUE;
                                                break;
                                            case 3:
                                                title="Limit Speed";
                                                snippet=notification.getSpeed().toString();
                                                icon=BitmapDescriptorFactory.HUE_RED;
                                                break;
                                        }

                                        markerOptions.title(title);
                                        markerOptions.snippet(snippet);
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(icon));
                                        mMap.addMarker(markerOptions);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(StartTour.this,"Có vấn đề khi lấy thông báo",Toast.LENGTH_LONG).show();
                                }
                            });
                }
                catch(Exception e)
                {
                    Log.e("", "run:",e );
                }
                finally {
                    handler.postDelayed(getNoti,10000);
                }
            }
        };

        getNoti.run();
    }

    private void addEvent() {
        Button sendNoti = findViewById(R.id.open_dialog);
        final View alertLayout = getLayoutInflater().inflate(R.layout.send_notification_dialog, null);
        final TextView Key = alertLayout.findViewById(R.id.key);
        final EditText Value = alertLayout.findViewById(R.id.value);
        final Dialog NotiDialog = new Dialog(this);
        final Button OKBtn = alertLayout.findViewById(R.id.OKBtn);
        final Button CancelBtn = alertLayout.findViewById(R.id.CancelBtn);

        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OnRoadNotification onRoadNotification = new OnRoadNotification();
                onRoadNotification.setTourId(tourId);
                SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
                onRoadNotification.setUserId(parseLong(sharedPreferences.getString("userId", "")));

                onRoadNotification.setNote(Value.getText().toString());
                try {
                    onRoadNotification.setSpeed(parseInt(Value.getText().toString()));
                } catch (Exception e) {
                    onRoadNotification.setSpeed(0);
                }
                switch (Key.getText().toString()) {
                    case "Gửi thông báo về điểm có cảnh sát giao thông?":
                        onRoadNotification.setNotificationType(1);
                        break;
                    case "Tin nhắn:":
                        onRoadNotification.setNotificationType(2);
                        break;
                    default:
                        onRoadNotification.setNotificationType(3);
                }

                final UserService userService;
                userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(StartTour.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    onRoadNotification.setLong(location.getLongitude());
                                    onRoadNotification.setLat(location.getLatitude());
                                    userService.notificationOnRoad(onRoadNotification, new Callback<DefaultResponse>() {
                                        @Override
                                        public void success(DefaultResponse defaultResponse, Response response) {
                                            Toast.makeText(StartTour.this, "Gửi thông báo thành công", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Toast.makeText(StartTour.this, "Thất bại", Toast.LENGTH_LONG).show();
                                            Log.e("Error:", error.toString());
                                        }
                                    });
                                }
                            }
                        });
                NotiDialog.cancel();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotiDialog.cancel();
            }
        });

        sendNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_start_tour, null);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StartTour.this);
                bottomSheetDialog.setContentView(dialogView);
                Button Police = dialogView.findViewById(R.id.PoliceBtn);
                Button Problem = dialogView.findViewById(R.id.MessageBtn);
                Button LimitSpeed = dialogView.findViewById(R.id.LimitSpeedBtn);

                Police.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        Key.setText("Gửi thông báo về điểm có cảnh sát giao thông?");
                        Value.setText("");
                        Value.setVisibility(View.INVISIBLE);
                        NotiDialog.setContentView(alertLayout);
                        NotiDialog.show();
                    }
                });
                Problem.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        Key.setText("Vấn đề bạn gặp phải:");
                        Value.setText("");
                        Value.setVisibility(View.VISIBLE);
                        Value.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                        NotiDialog.setContentView(alertLayout);
                        NotiDialog.show();
                    }
                });
                LimitSpeed.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        Key.setText("Tốc độ giới hạn:");
                        Value.setText("");
                        Value.setVisibility(View.VISIBLE);
                        Value.setInputType(InputType.TYPE_CLASS_NUMBER);
                        NotiDialog.setContentView(alertLayout);
                        NotiDialog.show();
                    }
                });
                bottomSheetDialog.show();
            }
        });
        if (ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartTour.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }
        if (ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartTour.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
        if (ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(StartTour.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        if(ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(StartTour.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(StartTour.this,"Chức năng yêu cầu quyền đọc ghi và ghi âm!!!",Toast.LENGTH_LONG).show();
            finish();
        }
        final String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tour Assistant/" + tourId + ".3gp";
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tour Assistant");
        if (!f.exists())
            f.mkdirs();
        myAudioPlay = new MediaPlayer();
        recordButton = findViewById(R.id.recordBtn);
        recordButton.setBackground(getDrawable(android.R.drawable.ic_media_play));
        recordButton.setTag("play");
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (recordButton.getTag().toString()) {
                    case "play":
                        try {
                            if (myAudioPlay.isPlaying())
                                myAudioPlay.stop();
                            myAudioRecorder = new MediaRecorder();
                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                            myAudioRecorder.setOutputFile(outputFile);
                            myAudioRecorder.prepare();
                            myAudioRecorder.start();
                        } catch (IOException e) {
                            Log.e("Error", "record: ", e);
                        }
                        recordButton.setBackground(getDrawable(android.R.drawable.ic_media_pause));

                        recordButton.setTag("stop");
                        break;
                    case "stop":
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        recordButton.setBackground(getDrawable(android.R.drawable.ic_media_play));
                        recordButton.setTag("play");
                        break;
                }
            }
        });

        playButton = findViewById(R.id.playBtn);
        playButton.setBackground(getDrawable(R.drawable.ic_rec_button));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordButton.getTag() == "stop") {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    recordButton.setBackground(getDrawable(android.R.drawable.ic_media_play));
                    recordButton.setTag("play");
                }
                try {
                    if (myAudioPlay.isPlaying())
                        myAudioPlay.stop();
                    myAudioPlay = new MediaPlayer();
                    myAudioPlay.setDataSource(outputFile);
                    myAudioPlay.prepare();
                    myAudioPlay.start();
                } catch (IOException e) {
                    Log.e("Error", "record: ", e);
                }
            }
        });
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng HCM = new LatLng(10.743702, 106.676026);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HCM, 15.0f));
        mMap.setMyLocationEnabled(true);
        searchView = findViewById(R.id.search_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = new ArrayList<>();
                Geocoder geocoder = new Geocoder(StartTour.this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size()>0) {
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(location)
                            .snippet(address.getAddressLine(0)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }
                else {
                    Toast.makeText(StartTour.this, "Fail", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (stopPointList != null) {
            for (StopPoint p : stopPointList) {
                LatLng latLng = new LatLng(p.getLat(), p.get_long());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(p.getName())
                        .snippet(p.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                if (p.getServiceTypeId() == 1)
                    marker.setIcon(cultery);
                else if(p.getServiceTypeId() == 2)
                    marker.setIcon(hotel);
                else if(p.getServiceTypeId() == 3)
                    marker.setIcon(parking);
                else if(p.getServiceTypeId() == 4)
                    marker.setIcon(other);
                markers.add(marker);
            }
        }

        final GPSServiceRequest gpsServiceRequest = new GPSServiceRequest();
        gpsServiceRequest.setTourId(String.valueOf(tourId));
        GPS_Runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("GPSservice", "onSuccess: ok");
                            if (location!= null) {
                                gpsServiceRequest.setLat(location.getLatitude());
                                gpsServiceRequest.setLong(location.getLongitude());
                            }
                            userService.getUserCooord(gpsServiceRequest, new Callback<List<CoordMember>>() {
                                @Override
                                public void success(List<CoordMember> coordMembers, Response response) {
                                    for (Marker i : userMarkers) {
                                        i.remove();
                                    }
                                    for (CoordMember p : coordMembers) {
                                        if (!p.getId().equals(userId)) {
                                            LatLng latLng = new LatLng(p.getLat(), p.getLong());
                                            userMarkers.add(mMap.addMarker(new MarkerOptions().position(latLng)
                                            .title(p.getId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))));
                                        }
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d("GPSservice", "failure: fail");
                                }
                            });
                        }
                    });

                }
                catch(Exception e)
                {
                    Log.e("", "run:",e );
                }
                finally {
                    GPS_Handler.postDelayed(GPS_Runnable,10000);
                }
            }

        };
        GPS_Runnable.run();
    }

}
