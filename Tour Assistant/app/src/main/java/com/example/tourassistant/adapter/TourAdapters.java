package com.example.tourassistant.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.tourassistant.Activity.R;
import com.example.tourassistant.Object.Tour;


import java.util.ArrayList;

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
        return tours.size();
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

        ImageView avtTour = (ImageView) customView.findViewById(R.id.avtTour);
        TextView nameTour = (TextView) customView.findViewById(R.id.nameTour);
        TextView timeTour = (TextView) customView.findViewById(R.id.timeTour);
        TextView numPeopletour = (TextView) customView.findViewById(R.id.numPeopletour);
        TextView priceTour = (TextView) customView.findViewById(R.id.pricetour);

        Tour tour = getItem(position);
        avtTour.setImageURI(Uri.parse(tour.getAvatar()));
        nameTour.setText(tour.getName());
        timeTour.setText(tour.getStarDate().toString().concat(" - ").concat(tour.getEndDate().toString()));
        numPeopletour.setText(tour.getAdults().toString().concat(" adults")
        .concat(tour.getChilds().toString().concat(" childrens")));
        priceTour.setText(tour.getMinCost().toString().concat(" - ").concat(tour.getMaxCost().toString()));

        return customView;
    }

}
