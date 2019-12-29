package com.ygaps.travelapp.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.model.TourInfoResponse;
import com.ygaps.travelapp.model.UpdateTourResponse;
import com.ygaps.travelapp.model.UpdateTourResquest;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateTourActivity extends AppCompatActivity {

    private static final int SRC_PLACE_PICKER_REQUEST = 1;
    private static final int DES_PLACE_PICKER_REQUEST = 2;
    private static final int GALLERY_REQUEST_CODE = 10;
    EditText tourName, startDate, endDate, minCost, maxCost, Adults, Childrens;
    EditText source, des;
    LatLng sourceCoord, desCoord;
    String pathAvt;
    CheckBox Isprivate;
    Button updateTourbtn;
    LinearLayout startDateLayout, endDateLayout;
    UpdateTourResquest updateTourResquest = new UpdateTourResquest();
    Calendar CstartDate = Calendar.getInstance(), CendDate = Calendar.getInstance();
    Calendar cEndDate = Calendar.getInstance();
    Calendar cStartDate = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour_activity);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Edit Tour");
        addControll();
        setCurrentInfo();
        addEvent();
    }

    private void setCurrentInfo() {
        Intent intent = getIntent();
        TourInfoResponse currentTourInfo = (TourInfoResponse) intent.getSerializableExtra("CurrentTourInfo");
        try {
            updateTourResquest.setId(currentTourInfo.getId().toString());
            tourName.setText(currentTourInfo.getName().toString());
            minCost.setText(currentTourInfo.getMinCost().toString());
            maxCost.setText(currentTourInfo.getMaxCost().toString());
            Adults.setText(currentTourInfo.getAdults().toString());
            Childrens.setText(currentTourInfo.getChilds().toString());
            Calendar cstartDate = Calendar.getInstance();
            cstartDate.setTimeInMillis(currentTourInfo.getStartDate());
            Calendar cendDate = Calendar.getInstance();
            cendDate.setTimeInMillis(currentTourInfo.getEndDate());
            startDate.setText(String.valueOf(cstartDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                    .concat(String.valueOf(cstartDate.get(Calendar.MONTH)+1)).concat("/")
                    .concat(String.valueOf(cstartDate.get(Calendar.YEAR))));
            endDate.setText(String.valueOf(cendDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                    .concat(String.valueOf(cendDate.get(Calendar.MONTH)+1)).concat("/")
                    .concat(String.valueOf(cendDate.get(Calendar.YEAR))));
            if (currentTourInfo.getIsPrivate() == true)
                Isprivate.setChecked(true);
        }catch (Exception e){}
    }

    private void addEvent() {
        add_event_startDate();
        add_event_isPrivate();
        add_event_endDate();
        add_event_startPlace();
        add_event_endPlace();
        updateTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTourResquest.setName(tourName.getText().toString());
                if (!TextUtils.isEmpty(minCost.getText().toString()))
                    updateTourResquest.setMinCost(Long.parseLong(minCost.getText().toString()));
                else
                    updateTourResquest.setMinCost(Long.parseLong("0"));
                if (!TextUtils.isEmpty(maxCost.getText().toString()))
                    updateTourResquest.setMaxCost(Long.parseLong(maxCost.getText().toString()));
                else
                    updateTourResquest.setMaxCost(Long.parseLong("0"));
                if (!TextUtils.isEmpty(Adults.getText().toString()))
                    updateTourResquest.setAdults(Long.parseLong(Adults.getText().toString()));
                else
                    updateTourResquest.setAdults(Long.parseLong("0"));
                if (!TextUtils.isEmpty(Childrens.getText().toString()))
                    updateTourResquest.setChilds(Long.parseLong(Childrens.getText().toString()));
                else
                    updateTourResquest.setChilds(Long.parseLong("0"));
                updateTour();
            }
        });
    }

    private void updateTour() {
        UserService userService;
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.updateTourInfo(updateTourResquest, new Callback<UpdateTourResponse>() {
            @Override
            public void success(UpdateTourResponse updateTourResponse, Response response) {
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 400)
                            Toast.makeText(UpdateTourActivity.this, "Bad request", Toast.LENGTH_SHORT).show();
                        if (error.getResponse().getStatus() == 403)
                            Toast.makeText(UpdateTourActivity.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                        if (error.getResponse().getStatus() == 404)
                            Toast.makeText(UpdateTourActivity.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                        if (error.getResponse().getStatus() == 500)
                            Toast.makeText(UpdateTourActivity.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void add_event_startPlace() {
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateTourActivity.this, PlacePickerActivity.class);
                startActivityForResult(i, SRC_PLACE_PICKER_REQUEST);
            }
        });
    }


    private void add_event_endPlace() {
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateTourActivity.this, PlacePickerActivity.class);
                startActivityForResult(i, DES_PLACE_PICKER_REQUEST);
            }
        });
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



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SRC_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                source.setText("Done");
                sourceCoord = new LatLng(data.getDoubleExtra("Lat", 0),
                        data.getDoubleExtra("Lng", 0));
            }
        }
        else if (requestCode == DES_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                des.setText("Done");
                desCoord = new LatLng(data.getDoubleExtra("Lat", 1),
                        data.getDoubleExtra("Lng", 1));
            }
        }
    }

    private void add_event_endDate() {
        final Calendar cEndDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener pickEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cEndDate.set(Calendar.YEAR, year);
                cEndDate.set(Calendar.MONTH  , month);
                cEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTourResquest.setEndDate(cEndDate.getTimeInMillis());
                updateEndDateEdt(cEndDate);
            }
        };
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateTourActivity.this, pickEndDate, cEndDate.get(Calendar.YEAR),
                        cEndDate.get(Calendar.MONTH), cEndDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateEndDateEdt(Calendar cEndDate) {
        endDate.setText(String.valueOf(cEndDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                .concat(String.valueOf(cEndDate.get(Calendar.MONTH)+1)).concat("/")
                .concat(String.valueOf(cEndDate.get(Calendar.YEAR))));
    }

    private void add_event_startDate() {
        final Calendar cStartDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener pickStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cStartDate.set(Calendar.YEAR, year);
                cStartDate.set(Calendar.MONTH, month);
                cStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTourResquest.setStartDate(cStartDate.getTimeInMillis());
                updateStarDateEdt(cStartDate);
            }
        };
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateTourActivity.this, pickStartDate, cStartDate.get(Calendar.YEAR),
                        cStartDate.get(Calendar.MONTH), cStartDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateStarDateEdt(Calendar cStartDate) {
        startDate.setText(String.valueOf(cStartDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                .concat(String.valueOf(cStartDate.get(Calendar.MONTH)+1)).concat("/")
                .concat(String.valueOf(cStartDate.get(Calendar.YEAR))));
    }

    private void add_event_isPrivate() {
        Isprivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Isprivate.isChecked())
                    updateTourResquest.setPrivate(true);
                else
                    updateTourResquest.setPrivate(false);
            }
        });
    }

    private void addControll() {
        tourName = (EditText) findViewById(R.id.create_tour_name_edt);
        startDate = (EditText) findViewById(R.id.create_tour_startDate_edt);
        endDate = (EditText) findViewById(R.id.create_tour_endDate_edt);
        startDateLayout = (LinearLayout) findViewById(R.id.create_tour_startDate);
        endDateLayout = (LinearLayout) findViewById(R.id.create_tour_endDate);
        minCost = (EditText) findViewById(R.id.create_tour_minCost_edt);
        maxCost = (EditText) findViewById(R.id.create_tour_maxCost_edt);
        Adults = (EditText) findViewById(R.id.create_tour_adults_edt);
        Childrens = (EditText) findViewById(R.id.create_tour_childrens_edt);
        Isprivate = (CheckBox) findViewById(R.id.Is_private_btn);
        updateTourbtn = (Button) findViewById(R.id.update_tour_btn);
        source = (EditText) findViewById(R.id.create_tour_source_edt);
        des = (EditText) findViewById(R.id.create_tour_des_edt);
    }


}
