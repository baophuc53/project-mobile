package com.ygaps.travelapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Object.ReviewTour;

import java.util.Calendar;
import java.util.List;

public class ReviewAdapter extends ArrayAdapter<ReviewTour> {
    private Activity context;
    private int resource;
    private List<ReviewTour> reviewTours;

    public ReviewAdapter(@NonNull Activity context, int resource, List<ReviewTour> reviewTours) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.reviewTours = reviewTours;
    }

    @Override
    public int getCount(){
        return reviewTours.size();
    }

    @Override
    public ReviewTour getItem(int position){
        return reviewTours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);


        ImageView avtUser = customView.findViewById(R.id.avtUsersmall_reviewTour);
        TextView nameUsercmt = customView.findViewById(R.id.nameUser_reviewTour);
        TextView cmtUser = customView.findViewById(R.id.CmtTv_reviewTour);
        RatingBar ratingBar = customView.findViewById(R.id.rating_revewTour);
        TextView date = customView.findViewById(R.id.create_on_reviewTour);
        Calendar calendar = Calendar.getInstance();
        ReviewTour reviewTour = getItem(position);
        try {
            Glide.with(customView)
                    .load(reviewTour.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avtUser);
            nameUsercmt.setText(reviewTour.getName());
            cmtUser.setText(reviewTour.getReview());
            ratingBar.setRating(reviewTour.getPoint());
            calendar.setTimeInMillis(Long.parseLong(reviewTour.getCreatedOn()));
            date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).concat("/")
                    .concat(String.valueOf(calendar.get(Calendar.MONTH) + 1)).concat("/")
                    .concat(String.valueOf(calendar.get(Calendar.YEAR))));
        }
        catch (Exception e){
            e.printStackTrace();
        };
        return customView;
    }


}
