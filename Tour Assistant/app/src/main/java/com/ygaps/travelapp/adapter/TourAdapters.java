package com.ygaps.travelapp.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.PointStat;
import com.ygaps.travelapp.Object.Tour;
import com.ygaps.travelapp.model.PointOfReviewTourResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Long.parseLong;

public class TourAdapters extends ArrayAdapter<Tour> {

    Activity context;
    int resource;
    ArrayList<Tour> tours;
    ArrayList<Tour> toursFilter;

    public TourAdapters(@NonNull Activity context, int resource, ArrayList<Tour> tours) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.tours = tours;
        this.toursFilter = new ArrayList<Tour>();
        this.toursFilter.addAll(tours);
    }

    @Override
    public int getCount(){
        return toursFilter.size();
    }

    @Override
    public Tour getItem(int position){
        return toursFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);


        ImageView avtTour = customView.findViewById(R.id.avtTour);
        TextView nameTour = customView.findViewById(R.id.nameTour);
        TextView timeTour = customView.findViewById(R.id.timeTour);
        TextView numPeopletour = customView.findViewById(R.id.numPeopletour);
        TextView priceTour = customView.findViewById(R.id.pricetour);
        final RatingBar rating_tour = customView.findViewById(R.id.tour_info_ratingBar);

        Tour tour = getItem(position);
        Glide.with(customView)
                .load("https://english4u.com.vn/Uploads/images/bi%E1%BB%83n%202.jpg")
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.bg_avatar_tour))
                .into(avtTour);

        //
        try {
            nameTour.setText(tour.getName());
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(tour.getStartDate());
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(tour.getEndDate());
            timeTour.setText(String.valueOf(startDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                    .concat(String.valueOf(startDate.get(Calendar.MONTH) + 1)).concat("/")
                    .concat(String.valueOf(startDate.get(Calendar.YEAR))).concat(" - ")
                    .concat(String.valueOf(endDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                    .concat(String.valueOf(endDate.get(Calendar.MONTH) + 1)).concat("/")
                    .concat(String.valueOf(endDate.get(Calendar.YEAR))));
        }
        catch (Exception e){};

        if(tour.getAdults()==null)
            numPeopletour.setText("");
        else
            numPeopletour.setText(tour.getAdults().toString().concat(" adults"));
        if(tour.getChilds()!=null)
            numPeopletour.setText(numPeopletour.getText().toString().concat(" - ").concat(tour.getChilds().toString().concat(" childrens")));

        if(tour.getMinCost()==null)
            tour.setMinCost(parseLong("0"));

        if(tour.getMaxCost()==null)
            priceTour.setText(tour.getMinCost().toString().concat(" VND - ").concat(" VND"));
        else
            priceTour.setText(tour.getMinCost().toString().concat(" VND - ").concat(tour.getMaxCost().toString()).concat(" VND"));

        UserService userService;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getTourReviewPoint(tour.getId(), new Callback<PointOfReviewTourResponse>() {
            @Override
            public void success(PointOfReviewTourResponse pointOfReviewTourResponse, Response response) {
                List<PointStat> list = pointOfReviewTourResponse.getPointStats();
                int[] raters = new int[5];
                for (int j = 0; j<5; j++) {
                    raters[4-j] = list.get(j).getTotal();
                }
                int s = 0;
                for (int i:raters) {
                    s+=i;
                }
                float avg = 0;
                for (int i = 0; i<5; i++){
                    avg+=(5-i)*raters[i];
                }
                if (s>0) {
                    avg /= s;
                    rating_tour.setRating(avg);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, "that bai", Toast.LENGTH_SHORT).show();
            }
        });

        return customView;
    }

    public void filterTag(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        // if (notesFilter.size() != 0)
        toursFilter.clear();
        if(tours!=null)
            for (Tour no : tours) {
                if (no.getName()!=null&&no.getName().toString().toLowerCase(Locale.getDefault()).contains(charText)) {
                    toursFilter.add(no);
                }
            }
        notifyDataSetChanged();
    }
}
