package com.ygaps.travelapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.PointStat;
import com.ygaps.travelapp.Object.ReviewTour;
import com.ygaps.travelapp.adapter.ExpandableHeightListView;
import com.ygaps.travelapp.adapter.ReviewAdapter;
import com.ygaps.travelapp.model.ListReviewOfTourResponse;
import com.ygaps.travelapp.model.PointOfReviewTourResponse;
import com.ygaps.travelapp.model.SendReviewTourRequest;
import com.ygaps.travelapp.model.SendReviewTourResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReviewListActivity extends AppCompatActivity {

    TextView point_info_textView_tour, point_info_total_tour, noReviewTv;
    RatingBar point_info_ratingBar_tour, user_rating_tour;
    RatingReviews point_info_rating_reviews_tour;
    ExpandableHeightListView list_reviews;
    EditText user_reviews;
    Button send_review;
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Reviews");

        addControll();
        addRatingReviewEvent();
        ShowListReview();
        SendReview();
    }

    private void SendReview() {
        send_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Long tourId = intent.getLongExtra("tourId", 0);
                SendReviewTourRequest sendReviewTourRequest = new SendReviewTourRequest();
                sendReviewTourRequest.setTourId(tourId);
                if (user_rating_tour.getRating() > 0 && ! user_reviews.getText().toString().equals("")) {
                    sendReviewTourRequest.setPoint(Math.round(user_rating_tour.getRating()));
                    sendReviewTourRequest.setReview(user_reviews.getText().toString());
                    UserService userService;
                    final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
                    String Token = sharedPreferences.getString("token", "");
                    MyAPIClient.getInstance().setAccessToken(Token);
                    userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                    userService.sendReview(sendReviewTourRequest, new Callback<SendReviewTourResponse>() {
                        @Override
                        public void success(SendReviewTourResponse sendReviewTourResponse, Response response) {
                            Toast.makeText(ReviewListActivity.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(ReviewListActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void ShowListReview() {
        Intent intent = getIntent();
        Long tourId = intent.getLongExtra("tourId", 0);
        UserService userService;
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getListReviews(tourId, 1, 10000, new Callback<ListReviewOfTourResponse>() {
            @Override
            public void success(ListReviewOfTourResponse listReviewOfTourResponse, Response response) {
                    ArrayList<ReviewTour> arrayList = new ArrayList<>(listReviewOfTourResponse.getReviews());
                    reviewAdapter = new ReviewAdapter(ReviewListActivity.this, R.layout.items_review_list
                            , arrayList);
                    list_reviews.setAdapter(reviewAdapter);
                list_reviews.setExpanded(true);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ReviewListActivity.this, "Lấy reviews thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRatingReviewEvent() {
        Intent intent = getIntent();
        Long tourId = intent.getLongExtra("tourId", 0);
        UserService userService;
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getTourReviewPoint(tourId, new Callback<PointOfReviewTourResponse>() {
            @Override
            public void success(PointOfReviewTourResponse pointOfReviewTourResponse, Response response) {
                List<PointStat> list = pointOfReviewTourResponse.getPointStats();
                int[] raters = new int[5];
                for (int j = 0; j < 5; j++) {
                    raters[4 - j] = list.get(j).getTotal();
                }
                int s = 0;
                for (int i : raters) {
                    s += i;
                }
                float avg = 0;
                for (int i = 0; i < 5; i++) {
                    avg += (5 - i) * raters[i];
                }
                if (s > 0) {
                    avg /= s;
                    point_info_textView_tour.setText(String.valueOf((double)Math.round(avg * 10)/10));
                    point_info_ratingBar_tour.setRating(avg);
                    point_info_total_tour.setText(String.valueOf(s));
                }
                point_info_rating_reviews_tour.createRatingBars(50, BarLabels.STYPE3, Color.parseColor("#0f9d58"), raters);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ReviewListActivity.this, "Lấy điểm đánh giá thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControll() {
        point_info_textView_tour = findViewById(R.id.point_info_textView_tour);
        point_info_total_tour = findViewById(R.id.point_info_total_tour);
        noReviewTv = findViewById(R.id.noReviewTv);
        point_info_ratingBar_tour = findViewById(R.id.point_info_ratingBar_tour);
        point_info_rating_reviews_tour = findViewById(R.id.point_info_rating_reviews_tour);
        list_reviews = findViewById(R.id.list_reviews);
        user_rating_tour = findViewById(R.id.user_rating_tour);
        user_reviews = findViewById(R.id.user_reviews);
        send_review = findViewById(R.id.send_review);
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
