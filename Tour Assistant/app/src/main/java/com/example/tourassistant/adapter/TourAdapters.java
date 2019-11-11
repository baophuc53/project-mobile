package com.example.tourassistant.adapter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

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

        ImageView avtTour = customView.findViewById(R.id.avtTour);
        TextView nameTour = customView.findViewById(R.id.nameTour);
        TextView timeTour = customView.findViewById(R.id.timeTour);
        TextView numPeopletour = customView.findViewById(R.id.numPeopletour);
        TextView priceTour = customView.findViewById(R.id.pricetour);

        Tour tour = getItem(position);
        new DownloadImageTask(avtTour).execute(tour.getAvatar());

        //
        nameTour.setText(tour.getName());
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(tour.getStartDate());
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(tour.getEndDate());
        timeTour.setText(String.valueOf(startDate.get(Calendar.DAY_OF_MONTH)).concat("/")
                .concat(String.valueOf(startDate.get(Calendar.MONTH))).concat("/")
                .concat(String.valueOf(startDate.get(Calendar.YEAR))).concat(" - ")
                .concat(String.valueOf(endDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                .concat(String.valueOf(endDate.get(Calendar.MONTH))).concat("/")
                .concat(String.valueOf(endDate.get(Calendar.YEAR))));


        numPeopletour.setText(tour.getAdults().toString().concat(" adults").concat(" - ")
        .concat(tour.getChilds().toString().concat(" childrens")));
        priceTour.setText(tour.getMinCost().toString().concat(" VND - ").concat(tour.getMaxCost().toString()).concat(" VND"));

        return customView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
