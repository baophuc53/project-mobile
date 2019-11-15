package com.example.tourassistant.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Freezable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourassistant.Object.Tour;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTourActivity extends AppCompatActivity {

    EditText tourName, startDate, endDate, minCost, maxCost, Adults, Childrens, image;
    CheckBox Isprivate;
    Button createTourbtn, add_image;
    LinearLayout startDateLayout, endDateLayout;
    Tour newTour = new Tour();
    int GALLERY_REQUEST_CODE = 10;
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
        add_event_isPrivate();
        add_event_startDate();
        add_event_endDate();
        add_event_chooseImage();
        createTourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTourActivity.this, MapsActivity.class);
                startActivity(intent);
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
        if (resultCode == CreateTourActivity.RESULT_OK)
            if (requestCode == GALLERY_REQUEST_CODE){
                Uri selectedIMG = data.getData();
                String [] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedIMG, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //dưa image len server???

                //ghi ten image
                File f = new File(imgDecodableString);
                image.setText(f.getName());
            }
    }

    private void add_event_chooseImage() {
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
    }

    private void add_event_endDate() {
        final Calendar cEndDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener pickEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cEndDate.set(Calendar.YEAR, year);
                cEndDate.set(Calendar.MONTH, month);
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
                .concat(String.valueOf(cEndDate.get(Calendar.MONTH))).concat("/")
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
        .concat(String.valueOf(cStartDate.get(Calendar.MONTH))).concat("/")
        .concat(String.valueOf(cStartDate.get(Calendar.YEAR))));
    }

    private void add_event_isPrivate() {
        Isprivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Isprivate.isChecked())
                    newTour.setIsPrivate(true);
                else
                    newTour.setIsPrivate(false);
                Toast.makeText(CreateTourActivity.this, newTour.getIsPrivate().toString(), Toast.LENGTH_LONG).show();
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
    }
}
