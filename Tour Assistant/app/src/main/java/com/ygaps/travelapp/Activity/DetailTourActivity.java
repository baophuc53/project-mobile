package com.ygaps.travelapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Comment;
import com.ygaps.travelapp.Object.StopPoint;
import com.ygaps.travelapp.adapter.CommentAdapters;
import com.ygaps.travelapp.adapter.ExpandableHeightListView;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.JoinRequest;
import com.ygaps.travelapp.model.SendCmtRequest;
import com.ygaps.travelapp.model.StopPointResponse;
import com.ygaps.travelapp.model.TourInfoRequest;
import com.ygaps.travelapp.model.TourInfoResponse;
import com.ygaps.travelapp.model.UserMessageRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailTourActivity extends AppCompatActivity {

    ImageView avtTour, avtUser, privateIMG, editTour;
    TextView nameTour, statusTour, priceTour, levelTour, dateTour, numDateTour, numPeopleTour, privateTour;
    Button stopPointTour, MemberTour, addCMT, reviewTour;
    EditText leaveCmtTour;
    CommentAdapters commentAdapters;
    ExpandableHeightListView lvComments;
    long tourId;
    List<StopPoint> stopPointList = new ArrayList<>();

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Detail Tour");
        addControl();
        addEvent();
        addEventAddCmt();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent=getIntent();
        if(intent.getBooleanExtra("isMyTour",false))
            getMenuInflater().inflate(R.menu.detail_tour_actionbar_menu, menu);
        else
            getMenuInflater().inflate(R.menu.join_tour_actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite:
                Intent thisIntent = getIntent();
                Intent intent = new Intent(DetailTourActivity.this, InviteMemberActivity.class);
                intent.putExtra("Tour", thisIntent.getLongExtra("tourId", 0));
                intent.putExtra("isPrivate", privateTour.getText() == "Private");
                startActivity(intent);
                return true;
            case R.id.join:
                if (privateTour.getText() == "Private")
                    Toast.makeText(DetailTourActivity.this, "Không thể tham gia tour riêng tư", Toast.LENGTH_LONG).show();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailTourActivity.this);
                    builder.setTitle("Join");
                    builder.setMessage("Bạn có muốn tham gia tour này không?");
                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            JoinRequest joinRequest = new JoinRequest();
                            joinRequest.setTourId("" + tourId);
                            joinRequest.isInvited = (privateTour.getText() == "Private");
                            UserService userService;
                            userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                            userService.joinTour(joinRequest, new Callback<DefaultResponse>() {
                                @Override
                                public void success(DefaultResponse joinResponse, Response response) {
                                    Toast.makeText(DetailTourActivity.this, "Tham gia thành công", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(DetailTourActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
                                    Log.e("Error:", error.toString());
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return true;
            case R.id.startTour:
                intent=new Intent(DetailTourActivity.this,StartTour.class);
                SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
                String userId = sharedPreferences.getString("userId", "");
                UserMessageRequest userMessageRequest=new UserMessageRequest(""+tourId,userId,"Tour "+nameTour.getText()+" bắt đầu");
                UserService userService;
                userService=MyAPIClient.getInstance().getAdapter().create(UserService.class);
                userService.sendMessage(userMessageRequest, new Callback<DefaultResponse>() {
                    @Override
                    public void success(DefaultResponse defaultResponse, Response response) {
                        Toast.makeText(DetailTourActivity.this,"Chuyến đi bắt đầu",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DetailTourActivity.this,"Lỗi thông báo các thành viên khác",Toast.LENGTH_LONG).show();
                    }
                });
                intent.putExtra("Tour", tourId);
                intent.putExtra("userId", userId);
                if (stopPointList.size()>0)
                    intent.putExtra("StopPointList", (Serializable) stopPointList);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void addEventAddCmt() {
        addCMT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveCmtTour.getText().toString() != ""){
                    SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
                    String token = sharedPreferences.getString("token", "");
                    String userId = sharedPreferences.getString("userId", "");

                    SendCmtRequest sendCmtRequest = new SendCmtRequest();
                    sendCmtRequest.setUserId(userId);
                    Intent intent = getIntent();
                    sendCmtRequest.setTourId(String.valueOf(tourId));
                    sendCmtRequest.setComment(leaveCmtTour.getText().toString());

                    UserService userService;
                    MyAPIClient.getInstance().setAccessToken(token);
                    userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                    userService.sendCmttoTour(sendCmtRequest, new Callback<StopPointResponse>() {
                        @Override
                        public void success(StopPointResponse stopPointResponse, Response response) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            switch (error.getKind()){
                                case HTTP:
                                    if (error.getResponse().getStatus() == 500)
                                        Toast.makeText(DetailTourActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void addEvent() {
        Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);
        boolean isMytour = intent.getBooleanExtra("isMyTour", false);
        if (isMytour)
            editTour.setVisibility(View.VISIBLE);
        TourInfoRequest request=new TourInfoRequest();
        UserService userService;

        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getTourInfo(tourId, new Callback<TourInfoResponse>() {
            @Override
            public void success(final TourInfoResponse tourInfoResponse, Response response) {
                stopPointList = tourInfoResponse.getStopPoints();
                editTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent thisintent = new Intent(DetailTourActivity.this, UpdateTourActivity.class);
                        thisintent.putExtra("CurrentTourInfo", (Serializable) tourInfoResponse);
                        startActivity(thisintent);
                    }
                });
                try {
                    Glide.with(DetailTourActivity.this)
                            .load("https://english4u.com.vn/Uploads/images/bi%E1%BB%83n%202.jpg")
                            .apply(new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.bg_avatar_tour))
                            .into(avtTour);

                    nameTour.setText(tourInfoResponse.getName());
                    if (tourInfoResponse.getStatus() == 0) {
                        statusTour.setText("Available");
                        statusTour.setTextColor(Color.parseColor("green"));
                    } else {
                        statusTour.setText("Unavailable");
                        statusTour.setTextColor(Color.parseColor("red"));
                    }
                    priceTour.setText(tourInfoResponse.getMinCost() + " VND - " + tourInfoResponse.getMaxCost() + " VND");
                    long levelPrice = Math.abs(tourInfoResponse.getMaxCost() - tourInfoResponse.getMinCost());
                    if (levelPrice < 5000000) {
                        levelTour.setText("Normal");
                    } else {
                        levelTour.setText("VIP");
                    }
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTimeInMillis(tourInfoResponse.getStartDate());
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTimeInMillis(tourInfoResponse.getEndDate());
                    dateTour.setText(String.valueOf(startDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                            .concat(String.valueOf(startDate.get(Calendar.MONTH) + 1)).concat("/")
                            .concat(String.valueOf(startDate.get(Calendar.YEAR))).concat(" - ")
                            .concat(String.valueOf(endDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                            .concat(String.valueOf(endDate.get(Calendar.MONTH) + 1)).concat("/")
                            .concat(String.valueOf(endDate.get(Calendar.YEAR))));
                    long num = (Math.abs(tourInfoResponse.getStartDate() - tourInfoResponse.getEndDate())) / (24 * 60 * 60 * 1000);
                    numDateTour.setText(Long.toString(num));
                    numPeopleTour.setText(tourInfoResponse.getAdults().toString().concat(" adults - ").concat(tourInfoResponse.getChilds().toString().concat(" childrens")));
                    if (tourInfoResponse.getIsPrivate() == true){
                        privateTour.setText("Private");
                        int imgPrivate = R.drawable.ic_lock;
                        privateIMG.setImageResource(imgPrivate);
                    }
                    else{
                        privateTour.setText("Public");
                            int imgPrivate = R.drawable.ic_open_lock;
                        privateIMG.setImageResource(imgPrivate);
                    }

                    commentAdapters = new CommentAdapters(DetailTourActivity.this,
                            R.layout.items_comment_layout, (ArrayList<Comment>) tourInfoResponse.getComments());
                    lvComments.setAdapter(commentAdapters);
                    lvComments.setExpanded(true);
                }
                catch (Exception e){}
                MemberTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailTourActivity.this, ListMemberActivity.class);
                        intent.putExtra("MemberList", (Serializable) tourInfoResponse.getMembers());
                        startActivity(intent);
                    }
                });
                stopPointTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent thisIntent = getIntent();
                        Intent intent = new Intent(DetailTourActivity.this, ListStopPointActivity.class);
                        intent.putExtra("isMyTour", thisIntent.getBooleanExtra("isMyTour",false));
                        intent.putExtra("tourId", tourId);
                        if (tourInfoResponse.getStopPoints().size() == 0){
                            intent.putExtra("isNull", true);
                            startActivity(intent);
                        }
                        else{
                            intent.putExtra("isNull", false);
                            intent.putExtra("StopPointList", (Serializable) tourInfoResponse.getStopPoints());
                            startActivity(intent);
                        }
                    }
                });
                reviewTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailTourActivity.this, ReviewListActivity.class);
                        intent.putExtra("tourId", tourId);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 404)
                            Toast.makeText(DetailTourActivity.this, "Không tìm thấy tour", Toast.LENGTH_LONG).show();
                        if (error.getResponse().getStatus() == 500)
                            Toast.makeText(DetailTourActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(DetailTourActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addControl() {
        avtTour = findViewById(R.id.avtTour);
        avtUser = findViewById(R.id.avtUser);
        privateIMG = findViewById(R.id.privateTourIMG);
        nameTour = findViewById(R.id.nameTour);
        statusTour = findViewById(R.id.statusTour);
        priceTour = findViewById(R.id.pricetour);
        levelTour = findViewById(R.id.priceLevel);
        dateTour = findViewById(R.id.dateTour);
        numDateTour = findViewById(R.id.numDate);
        numPeopleTour = findViewById(R.id.numPeopletour);
        privateTour = findViewById(R.id.privateTour);
        stopPointTour = findViewById(R.id.stopPoint);
        MemberTour = findViewById(R.id.member);
        leaveCmtTour = findViewById(R.id.addCmtEdt);
        addCMT = findViewById(R.id.addCmtBtn);
        lvComments = findViewById(R.id.list_cmt);
        reviewTour = findViewById(R.id.review);
        editTour = findViewById(R.id.editTour);
    }
}
