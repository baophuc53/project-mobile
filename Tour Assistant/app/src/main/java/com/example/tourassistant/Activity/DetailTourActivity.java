package com.example.tourassistant.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tourassistant.Api.MyAPIClient;
import com.example.tourassistant.Api.UserService;
import com.example.tourassistant.Object.Comment;
import com.example.tourassistant.Object.Tour;
import com.example.tourassistant.adapter.CommentAdapters;
import com.example.tourassistant.adapter.ExpandableHeightListView;
import com.example.tourassistant.adapter.TourAdapters;
import com.example.tourassistant.model.ListTourRequest;
import com.example.tourassistant.model.SendCmtRequest;
import com.example.tourassistant.model.StopPointResponse;
import com.example.tourassistant.model.TourInfoRequest;
import com.example.tourassistant.model.TourInfoResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailTourActivity extends AppCompatActivity {

    ImageView avtTour, avtUser, privateIMG;
    TextView nameTour, statusTour, priceTour, levelTour, dateTour, numDateTour, numPeopleTour, privateTour;
    Button stopPointTour, MemberTour, addCMT;
    EditText leaveCmtTour;
    CommentAdapters commentAdapters;
    ExpandableHeightListView lvComments;

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
        if(intent.getBooleanExtra("isMyListTour",false))
            getMenuInflater().inflate(R.menu.detail_tour_actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite:
                Intent thisIntent=getIntent();
                Intent intent=new Intent(DetailTourActivity.this,InviteMemberActivity.class);
                intent.putExtra("Tour",thisIntent.getLongExtra("tourId",0));
                intent.putExtra("isPrivate",privateTour.getText()=="Private");
                startActivity(intent);
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
                    long tourId = intent.getLongExtra("tourId", 0);
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
        long tourId = intent.getLongExtra("tourId", 0);

        TourInfoRequest request=new TourInfoRequest();
        UserService userService;

        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getTourInfo(tourId, new Callback<TourInfoResponse>() {
            @Override
            public void success(final TourInfoResponse tourInfoResponse, Response response) {
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
                        Intent intent = new Intent(DetailTourActivity.this, ListStopPointActivity.class);
                        if (tourInfoResponse.getStopPoints().size() == 0){
                            intent.putExtra("isNull", true);
                            startActivity(intent);
                        }
                        else{
                            intent.putExtra("isNull", false);
                        intent.putExtra("StopPointList", (Serializable) tourInfoResponse.getStopPoints());
                        startActivity(intent);}
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
    }
}
