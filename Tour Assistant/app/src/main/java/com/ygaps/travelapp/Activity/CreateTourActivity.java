package com.ygaps.travelapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.model.CreateTourRequest;
import com.ygaps.travelapp.model.CreateTourResponse;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateTourActivity extends AppCompatActivity {

    private static final int SRC_PLACE_PICKER_REQUEST = 1;
    private static final int DES_PLACE_PICKER_REQUEST = 2;
    private static final int GALLERY_REQUEST_CODE = 10;
    EditText tourName, startDate, endDate, minCost, maxCost, Adults, Childrens, image;
    EditText source, des;
    LatLng sourceCoord, desCoord;
    String pathAvt, avataImageB64;
    CheckBox Isprivate;
    Button createTourbtn, add_image;
    LinearLayout startDateLayout, endDateLayout;
    Tour newTour = new Tour();
    Calendar cEndDate = Calendar.getInstance();
    Calendar cStartDate = Calendar.getInstance();
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Create Tour");
        addControll();
        addEvent();


    }

    private void addEvent() {
        add_event_startDate();
        add_event_isPrivate();
        add_event_startDate();
        add_event_endDate();
        add_event_startPlace();
        add_event_endPlace();
        add_event_chooseImage();
        createTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTour();
            }
        });
    }

    private void add_event_startPlace() {
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTourActivity.this, PlacePickerActivity.class);
                startActivityForResult(i, SRC_PLACE_PICKER_REQUEST);
            }
        });
    }


    private void add_event_endPlace() {
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTourActivity.this, PlacePickerActivity.class);
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

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == CreateTourActivity.RESULT_OK) {
                Uri selectedIMG = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedIMG, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathAvt = cursor.getString(columnIndex);
                cursor.close();
                File f = new File(pathAvt);
                image.setText(f.getName());
                //convert to base64
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapFactory.decodeFile(pathAvt);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    avataImageB64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                } catch (Exception e) {
                }
            }
        }
        else if (requestCode == SRC_PLACE_PICKER_REQUEST) {
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

    private void add_event_chooseImage() {
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(CreateTourActivity.this, galleryPermissions)) {
                    pickFromGallery();
                } else {
                    EasyPermissions.requestPermissions(CreateTourActivity.this, "Access for storage", 101, galleryPermissions);
                }
            }
        });
    }

    private void add_event_endDate() {
        final Calendar cEndDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener pickEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cEndDate.set(Calendar.YEAR, year);
                cEndDate.set(Calendar.MONTH  , month);
                cEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newTour.setEndDate(cEndDate.getTimeInMillis());
                updateEndDateEdt(cEndDate);
            }
        };
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTourActivity.this, pickEndDate, cEndDate.get(Calendar.YEAR),
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
                newTour.setStartDate(cStartDate.getTimeInMillis());
                updateStarDateEdt(cStartDate);
            }
        };
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTourActivity.this, pickStartDate, cStartDate.get(Calendar.YEAR),
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
                    newTour.isPrivate= true;
                else
                    newTour.isPrivate=false;
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
        image = (EditText) findViewById(R.id.create_tour_image);
        add_image = (Button) findViewById(R.id.create_tour_btn_add_image);
        Isprivate = (CheckBox) findViewById(R.id.Is_private_btn);
        createTourbtn = (Button) findViewById(R.id.create_tour_btn);
        source = (EditText) findViewById(R.id.create_tour_source_edt);
        des = (EditText) findViewById(R.id.create_tour_des_edt);
    }

    public boolean checkTourInfo(){
        boolean correct = true;
        if (TextUtils.isEmpty(tourName.getText().toString())){
            correct = false;
            tourName.setError("Vui lòng điền tên chuyến đi");
        }

        if (TextUtils.isEmpty(startDate.getText().toString())){
            correct = false;
            startDate.setError("Vui lòng chọn ngày xuất phát");
        }
        if (TextUtils.isEmpty(endDate.getText().toString())){
            correct = false;
            endDate.setError("Vui lòng chọn ngày về");
        }
        return correct;
    }

    public void createTour(){
        boolean correct = checkTourInfo();
        if (correct == true) {
            newTour.setName(tourName.getText().toString());
            newTour.setSourceLat(sourceCoord.latitude);
            newTour.setSourceLong(sourceCoord.longitude);
            newTour.setDesLat(desCoord.latitude);
            newTour.setDesLong(desCoord.longitude);
            if (!TextUtils.isEmpty(Adults.getText().toString())){
                newTour.setAdults(Long.parseLong(Adults.getText().toString()));
            }
            else{
                newTour.setAdults(Long.parseLong("0"));
            }
            if (!TextUtils.isEmpty(Childrens.getText().toString())){
                newTour.setChilds(Long.parseLong(Childrens.getText().toString()));
            }
            else{
                newTour.setChilds(Long.parseLong("0"));
            }
            if (!TextUtils.isEmpty(minCost.getText().toString())){
                newTour.setMinCost(Long.parseLong(minCost.getText().toString()));
            }
            else{
                newTour.setMinCost(Long.parseLong("0"));
            }
            if (!TextUtils.isEmpty(maxCost.getText().toString())){
                newTour.setMaxCost(Long.parseLong(maxCost.getText().toString()));
            }
            else{
                newTour.setMaxCost(Long.parseLong("0"));
            }
            if (!TextUtils.isEmpty(image.getText().toString())){
                newTour.setAvatar(avataImageB64);
            }
            else{
                newTour.setAvatar(null);
            }

            CreateTourRequest createTourRequest=new CreateTourRequest();
            createTourRequest.setName(newTour.getName());
            createTourRequest.setSourceLat(newTour.getSourceLat());
            createTourRequest.setSourceLong(newTour.getSourceLong());
            createTourRequest.setDesLat(newTour.getDesLat());
            createTourRequest.setDesLong(newTour.getDesLong());
            createTourRequest.setAdults(newTour.getAdults());
            createTourRequest.setChilds(newTour.getChilds());
            createTourRequest.setIsPrivate(newTour.isPrivate);
            createTourRequest.setStartDate(newTour.getStartDate());
            createTourRequest.setEndDate(newTour.getEndDate());
            createTourRequest.setMaxCost(newTour.getMaxCost());
            createTourRequest.setMinCost(newTour.getMinCost());
            createTourRequest.setAvatar(newTour.getAvatar());
            final UserService userService;
            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
            userService.createTour(createTourRequest,
                      new Callback<CreateTourResponse>(){
                        @Override
                        public void success(CreateTourResponse createTourResponse, Response response) {
                            Toast.makeText(CreateTourActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CreateTourActivity.this, MapsActivity.class);
                            intent.putExtra("tourId", Integer.parseInt(createTourResponse.getId().toString()));
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            switch (error.getKind()) {
                                case HTTP:
                                    if (error.getResponse().getStatus() == 400)
                                        Toast.makeText(CreateTourActivity.this, "Giá trị sai", Toast.LENGTH_LONG).show();
                                    else if (error.getResponse().getStatus() == 500)
                                        Toast.makeText(CreateTourActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(CreateTourActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                            }
                        }
            });
        }
    }
}
