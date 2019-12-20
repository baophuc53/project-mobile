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
import com.ygaps.travelapp.Object.FeedbackList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.callback.Callback;

public class FeedbackAdapter extends ArrayAdapter<FeedbackList> {
    private Activity context;
    private int resource;
    private List<FeedbackList> feedbackLists;

    public FeedbackAdapter(@NonNull Activity context, int resource, List<FeedbackList> feedbackLists) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.feedbackLists = feedbackLists;
    }

    @Override
    public int getCount(){
        return feedbackLists.size();
    }

    @Override
    public FeedbackList getItem(int position){
        return feedbackLists.get(position);
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


        ImageView avtUser = customView.findViewById(R.id.avtUsersmall_point);
        TextView nameUsercmt = customView.findViewById(R.id.nameUsercmt_point);
        TextView cmtUser = customView.findViewById(R.id.CmtTv_point);
        RatingBar ratingBar = customView.findViewById(R.id.rating_point);
        TextView date = customView.findViewById(R.id.create_on_point);
        Calendar calendar = Calendar.getInstance();
        FeedbackList feedback = getItem(position);
        try {
            Glide.with(customView)
                    .load(feedback.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avtUser);
            nameUsercmt.setText(feedback.getName());
            cmtUser.setText(feedback.getFeedback());
            ratingBar.setRating(feedback.getPoint());
            calendar.setTimeInMillis(Long.parseLong(feedback.getCreatedOn()));
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
