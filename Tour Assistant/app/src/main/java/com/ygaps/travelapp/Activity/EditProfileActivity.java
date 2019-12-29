package com.ygaps.travelapp.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.model.UpdateUserInfoRequest;
import com.ygaps.travelapp.model.UpdateUserInfoResponse;
import com.ygaps.travelapp.model.UserInfoResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText nameUser, email, phone, address, birthday;
    ImageView avatar;
    Spinner genderspinner;
    Button save;
    UserInfoResponse user = new UserInfoResponse();
    UpdateUserInfoRequest updateUser = new UpdateUserInfoRequest();

    String[] state = {"Female", "Male"};
    ArrayAdapter<String> adapter_state;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Edit Profile");
        addControl();
        getCurrentUserInfo();
        addEvent();
    }

    private void addEvent() {
        addBirthdayEvent();
        addChooseGenderEvent();

        save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUser.setFullName(nameUser.getText().toString());
                    updateUser.setPhone(phone.getText().toString());
                    updateUser.setEmail(email.getText().toString());
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormatEdt = new SimpleDateFormat("dd-MM-yyyy");
                    @SuppressLint("SimpleDateFormat") DateFormat updateDobFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    try {
                        Date dateTemp = dateFormatEdt.parse(birthday.getText().toString());
                        String dateTemnString = updateDobFormat.format(dateTemp);
                        Date updateDob = updateDobFormat.parse(dateTemnString);
                        updateUser.setDob(updateDob);
                    } catch (Exception e) {
                    }
                    updateUser.setGender(genderspinner.getSelectedItemPosition());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                    alertDialogBuilder.setMessage("Do you want to save infomation?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
                            String Token = sharedPreferences.getString("token", "");

                            UserService userService;
                            MyAPIClient.getInstance().setAccessToken(Token);
                            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                            userService.updateUserInfo(updateUser, new Callback<UpdateUserInfoResponse>() {
                                @Override
                                public void success(UpdateUserInfoResponse updateUserInfoResponse, Response response) {
                                    finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Nothing
                        }
                    });
                    alertDialogBuilder.show();
                }
            });
    }

    private void addBirthdayEvent() {
        @SuppressLint("SimpleDateFormat") final DateFormat dateFormatEdt = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") final DateFormat updateDobFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        final Calendar cBirthday = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener pickDob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cBirthday.set(Calendar.YEAR, year);
                cBirthday.set(Calendar.MONTH, month);
                cBirthday.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String strDate = dateFormatEdt.format(cBirthday.getTime());
                birthday.setText(strDate);

                try {
                    Date dateTemp = dateFormatEdt.parse(birthday.getText().toString());
                    String dateTemnString = updateDobFormat.format(dateTemp);
                    Date updateDob = updateDobFormat.parse(dateTemnString);
                    updateUser.setDob(updateDob);
                } catch (Exception e) {
                }

            }
        };
        if (TextUtils.isEmpty(birthday.getText())) {
            birthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(EditProfileActivity.this, pickDob, cBirthday.get(Calendar.YEAR),
                            cBirthday.get(Calendar.MONTH), cBirthday.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        } else {
            String temp = birthday.getText().toString();
            final int day, month, year;
            day = Integer.parseInt(temp.substring(0, 2));
            month = Integer.parseInt(temp.substring(3, 5));
            year = Integer.parseInt(temp.substring(6, temp.length()));
            birthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(EditProfileActivity.this, pickDob, year, month - 1, day).show();
                }
            });
        }
    }

    private void getCurrentUserInfo() {
        Intent intent = getIntent();
        user = (UserInfoResponse) intent.getSerializableExtra("userInfo");
        try {
            Glide.with(EditProfileActivity.this)
                    .load(user.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avatar);
            updateUser.setFullName(user.getFullName());
            nameUser.setText(updateUser.getFullName());
            updateUser.setEmail(user.getEmail());
            email.setText(updateUser.getEmail());
            updateUser.setPhone(user.getPhone());
            phone.setText(updateUser.getPhone());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat showDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = dateFormat.parse(user.getDob());
                updateUser.setDob(date);
                birthday.setText(showDateFormat.format(date));
            } catch (Exception e) {
            }
            updateUser.setGender(user.getGender());
            genderspinner.post(new Runnable() {
                @Override
                public void run() {
                    genderspinner.setSelection(updateUser.getGender());
                }
            });
        } catch (Exception e) {
        }

    }

    private void addChooseGenderEvent() {
        adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderspinner.setSelection(position);
                String selState = (String) genderspinner.getSelectedItem();
                try {
                    if (selState == "Female")
                        updateUser.setGender(0);
                    if (selState == "Male")
                        updateUser.setGender(1);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void addControl() {
        avatar = findViewById(R.id.avtUser);
        nameUser = findViewById(R.id.nameUser);
        email = findViewById(R.id.user_email);
        phone = findViewById(R.id.user_phone);
        address = findViewById(R.id.user_address);
        birthday = findViewById(R.id.user_birthday);
        genderspinner = (Spinner) findViewById(R.id.genderspinner);
        save = findViewById(R.id.SaveButton);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
