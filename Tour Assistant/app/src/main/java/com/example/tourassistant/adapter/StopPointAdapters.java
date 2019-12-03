package com.example.tourassistant.adapter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompatParcelizer;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tourassistant.Activity.R;
import com.example.tourassistant.Object.Member;
import com.example.tourassistant.Object.StopPoint;
import com.example.tourassistant.Object.Tour;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import static java.lang.Long.parseLong;

public class StopPointAdapters extends ArrayAdapter<StopPoint> {

    Activity context;
    int resource;
    ArrayList<StopPoint> stopPoints;
    ArrayList<StopPoint> stopPointsFilter;

    public StopPointAdapters(@NonNull Activity context, int resource, ArrayList<StopPoint> stopPoints) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.stopPoints = stopPoints;
        this.stopPointsFilter = new ArrayList<StopPoint>();
        this.stopPointsFilter.addAll(stopPoints);
    }

    @Override
    public int getCount(){
        return stopPointsFilter.size();
    }

    @Override
    public StopPoint getItem(int position){
        return stopPointsFilter.get(position);
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

        ImageView serviceType = customView.findViewById(R.id.service_type_img);
        TextView nameStoppoint = customView.findViewById(R.id.nameStopPoint);
        TextView time = customView.findViewById(R.id.timeStopPoint);
        TextView service = customView.findViewById(R.id.ServiceTypeStopPoint);

        StopPoint stopPoint = getItem(position);

        try{
            nameStoppoint.setText(stopPoint.getName());
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(stopPoint.getArrivalAt());
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(stopPoint.getLeaveAt());
            time.setText(String.valueOf(startDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                    .concat(String.valueOf(startDate.get(Calendar.MONTH) + 1)).concat("/")
                    .concat(String.valueOf(startDate.get(Calendar.YEAR))).concat(" - ")
                    .concat(String.valueOf(endDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                    .concat(String.valueOf(endDate.get(Calendar.MONTH) + 1)).concat("/")
                    .concat(String.valueOf(endDate.get(Calendar.YEAR))));
            int imageService = 0;
            if (stopPoint.getServiceTypeId() == 1){
                service.setText("Restaurant");
                imageService = R.drawable.ic_cutlery;
            }
            if (stopPoint.getServiceTypeId() == 2){
                service.setText("Hotel");
                imageService = R.drawable.ic_hotel;
            }
            if (stopPoint.getServiceTypeId() == 3){
                service.setText("Restaurant");
                imageService = R.drawable.ic_parking;
            }
            if (stopPoint.getServiceTypeId() == 4){
                service.setText("Restaurant");
                imageService = R.drawable.ic_24_hours;
            }
            serviceType.setImageResource(imageService);
        }
        catch (Exception e){}

        return customView;
    }

    public void filterTag(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stopPointsFilter.clear();
        for (StopPoint no : stopPoints) {
            if (no.getName()!=null&&no.getName().toString().toLowerCase(Locale.getDefault()).contains(charText)) {
                stopPointsFilter.add(no);
            }
        }
        notifyDataSetChanged();
    }
}
