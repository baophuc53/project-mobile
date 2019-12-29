package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.StopPoint;
import com.ygaps.travelapp.adapter.StopPointAdapters;
import com.ygaps.travelapp.model.StopPointRequest;
import com.ygaps.travelapp.model.StopPointResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListStopPointActivity extends AppCompatActivity {

    ListView lvStopPoints;
    StopPointAdapters stopPointAdapters;
    SearchView search;
    TextView NoStopPoint;
    StopPoint stopPoint = null;
    final Calendar cArriveTime = Calendar.getInstance();
    final Calendar cLeaveTime = Calendar.getInstance();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stop_point);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView  title = findViewById(R.id.actionbar_textview);
        title.setText("Stop Points");
        Intent thisIntent=getIntent();
        id = (int)thisIntent.getLongExtra("tourId",0);
        addControl();
        Show();
        addSeach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent=getIntent();
        if (intent.getBooleanExtra("isMyTour", false))
            getMenuInflater().inflate(R.menu.add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPoint:
                Intent intent=new Intent(ListStopPointActivity.this, MapsActivity.class);
                intent.putExtra("tourId", id);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSeach() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                stopPointAdapters.filterTag(text);
                return false;
            }
        });
    }

    private void Show() {
            Intent intent = getIntent();
            final int tourId = id;
            boolean isnull = intent.getBooleanExtra("isNull", true);
            if (!isnull) {
                final List<StopPoint> listStopPoint = (List<StopPoint>) intent.getSerializableExtra("StopPointList");
                stopPointAdapters = new StopPointAdapters(ListStopPointActivity.this, R.layout.items_list_stop_point,
                        (ArrayList<StopPoint>) listStopPoint);
                lvStopPoints.setAdapter(stopPointAdapters);
                lvStopPoints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_start_tour, null);
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ListStopPointActivity.this);
                        bottomSheetDialog.setContentView(dialogView);
                        Button Rating =dialogView.findViewById(R.id.PoliceBtn);
                        Button Update=dialogView.findViewById(R.id.ProblemBtn);
                        Button Delete=dialogView.findViewById(R.id.LimitSpeedBtn);
                        Rating.setText("Rating and reviews");
                        Update.setText("Update stop point");
                        Delete.setText("Delete stop point");
                        //Rating stop point
                        Rating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListStopPointActivity.this, PointInfoActivity.class);
                                int pointId = stopPointAdapters.getItem(position).getServiceId();
                                if (pointId > 0) {
                                    intent.putExtra("id", pointId);
                                    startActivity(intent);
                                }
                            }
                        });

                        //delete stop point
                        Delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListStopPointActivity.this);
                                builder.setMessage("Do you want to delete this stop point ?")
                                        .setCancelable(false)
                                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                UserService userService;
                                                userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                                                userService.RemoveStopPoint(stopPointAdapters.getItem(position).getId(), new Callback<StopPointResponse>() {
                                                    @Override
                                                    public void success(StopPointResponse stopPointResponse, Response response) {
                                                        Toast.makeText(ListStopPointActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                        listStopPoint.remove(stopPointAdapters.getItem(position));
                                                        stopPointAdapters.notifyDataSetChanged();
                                                        recreate();
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        switch (error.getKind()) {
                                                            case HTTP:
                                                                if (error.getResponse().getStatus() == 403)
                                                                    Toast.makeText(ListStopPointActivity.this, "Not permission to remove stop point", Toast.LENGTH_LONG).show();
                                                                else if (error.getResponse().getStatus() == 404)
                                                                    Toast.makeText(ListStopPointActivity.this, "Stop point is not found", Toast.LENGTH_LONG).show();
                                                                break;
                                                            default:
                                                                Toast.makeText(ListStopPointActivity.this, "Fail", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Warning");
                                alert.show();
                            }
                        });

                        //update stop point
                        Update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(ListStopPointActivity.this);
                                dialog.setContentView(R.layout.layout_update_stop_point);
                                dialog.setCancelable(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                Button addStopPoint = dialog.findViewById(R.id.update_stopPointBtn);
                                ImageButton cancel = dialog.findViewById(R.id.cancel_update_dialog);
                                final EditText arriveTime = dialog.findViewById(R.id.edit_update_arrive_time);
                                final EditText arriveDate = dialog.findViewById(R.id.edit_update_arrive_date);
                                final EditText leaveTime = dialog.findViewById(R.id.edit_update_leave_time);
                                final EditText leaveDate = dialog.findViewById(R.id.edit_update_leave_date);
                                final EditText name = dialog.findViewById(R.id.edit_update_name);
                                final Spinner service = dialog.findViewById(R.id.edit_update_service_type);
                                final TextView address = dialog.findViewById(R.id.edit_update_address);
                                final EditText maxCost = dialog.findViewById(R.id.edit_update_max_cost);
                                final EditText minCost = dialog.findViewById(R.id.edit_update_min_cost);

                                address.setText(stopPointAdapters.getItem(position).getAddress());
                                ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(ListStopPointActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        getResources().getStringArray(R.array.services));
                                serviceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                service.setAdapter(serviceAdapter);
                                name.setText(stopPointAdapters.getItem(position).getName());
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
                                        if (TextUtils.isEmpty(minCost.getText().toString())) {
                                            correct = false;
                                            minCost.setError("");
                                        }
                                        if (TextUtils.isEmpty(maxCost.getText().toString())) {
                                            correct = false;
                                            minCost.setError("");
                                        }
                                        if (correct) {
                                            stopPoint = new StopPoint();
                                            stopPoint.setId(stopPointAdapters.getItem(position).getId());
                                            stopPoint.setLat(stopPointAdapters.getItem(position).getLat());
                                            stopPoint.setLong(stopPointAdapters.getItem(position).getLong());
                                            stopPoint.setArrivalAt(cArriveTime.getTimeInMillis());
                                            stopPoint.setLeaveAt(cLeaveTime.getTimeInMillis());
                                            stopPoint.setMinCost(Long.parseLong(minCost.getText().toString()));
                                            stopPoint.setMaxCost(Long.parseLong(maxCost.getText().toString()));
                                            stopPoint.setName(name.getText().toString());
                                            stopPoint.setServiceTypeId(service.getSelectedItemPosition() + 1);
                                            List<StopPoint> stopPointList = new ArrayList<>();
                                            stopPointList.add(stopPoint);
                                            StopPointRequest stopPointRequest = new StopPointRequest();
                                            stopPointRequest.setStopPoints(stopPointList);
                                            stopPointRequest.setTourId(tourId);
                                            final UserService userService;
                                            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                                            userService.addStopPoints(stopPointRequest, new Callback<StopPointResponse>() {
                                                @Override
                                                public void success(StopPointResponse stopPointResponse, Response response) {
                                                    Toast.makeText(ListStopPointActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                    stopPointAdapters.getItem(position).setName(stopPoint.getName());
                                                    stopPointAdapters.getItem(position).setServiceTypeId(stopPoint.getServiceTypeId());
                                                    stopPointAdapters.getItem(position).setArrivalAt(stopPoint.getArrivalAt());
                                                    stopPointAdapters.getItem(position).setLeaveAt(stopPoint.getLeaveAt());
                                                    stopPointAdapters.notifyDataSetChanged();
                                                    dialog.cancel();
                                                }
                                                @Override
                                                public void failure(RetrofitError error) {
                                                    switch (error.getKind()) {
                                                        case HTTP:
                                                            if (error.getResponse().getStatus() == 404)
                                                                Toast.makeText(ListStopPointActivity.this, "Tour không hợp lệ", Toast.LENGTH_SHORT).show();
                                                            else if (error.getResponse().getStatus() == 403)
                                                                Toast.makeText(ListStopPointActivity.this, "Không được phép thêm điểm dừng", Toast.LENGTH_SHORT).show();
                                                            else if (error.getResponse().getStatus() == 500)
                                                                Toast.makeText(ListStopPointActivity.this, "Server không thể tạo điểm dừng", Toast.LENGTH_SHORT).show();
                                                            break;
                                                        default:
                                                            Toast.makeText(ListStopPointActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.cancel();
                                                }
                                            });

                                        }
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
                                        new TimePickerDialog(ListStopPointActivity.this, timeSetListener, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), true).show();
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
                                        new DatePickerDialog(ListStopPointActivity.this, pickArriveDate, cDate.get(Calendar.YEAR),
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
                                        new TimePickerDialog(ListStopPointActivity.this, timeSetListener, cTime.get(Calendar.HOUR_OF_DAY), cTime.get(Calendar.MINUTE), true).show();
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
                                        new DatePickerDialog(ListStopPointActivity.this, pickLeaveDate, cDate.get(Calendar.YEAR),
                                                cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH)).show();
                                    }
                                });
                            }
                        });
                        bottomSheetDialog.show();
                        return true;
                    }
                });

            }
            else{
                NoStopPoint.setText("No stop point");
                NoStopPoint.setVisibility(View.VISIBLE);
            }
    }

    private void addControl() {
        lvStopPoints = findViewById(R.id.list_stop_point);
        search = findViewById(R.id.search_stopPoint);
        NoStopPoint = findViewById(R.id.noStopPointTv);
    }
}
