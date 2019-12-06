package com.example.tourassistant.adapter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.IconCompatParcelizer;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tourassistant.Activity.R;
import com.example.tourassistant.Object.Comment;
import com.example.tourassistant.Object.Tour;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Long.parseLong;

public class CommentAdapters extends ArrayAdapter<Comment> {

    Activity context;
    int resource;
    ArrayList<Comment> commments;

    public CommentAdapters(@NonNull Activity context, int resource, ArrayList<Comment> comments) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.commments = comments;
    }

    @Override
    public int getCount(){
        return commments.size();
    }

    @Override
    public Comment getItem(int position){
        return commments.get(position);
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


        ImageView avtUser = customView.findViewById(R.id.avtUsersmall);
        TextView nameUsercmt = customView.findViewById(R.id.nameUsercmt);
        TextView cmtUser = customView.findViewById(R.id.CmtTv);

        Comment comments = getItem(position);
        try {
            Glide.with(customView)
                .load(comments.getAvatar())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.bg_avatar_user))
                .into(avtUser);
            nameUsercmt.setText(comments.getName());
            cmtUser.setText(comments.getComment());
        }
        catch (Exception e){};
        return customView;
    }
}