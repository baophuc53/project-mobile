package com.example.tourassistant.adapter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
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
import com.example.tourassistant.Object.Tour;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import static java.lang.Long.parseLong;

public class MemberAdapters extends ArrayAdapter<Member>{

    Activity context;
    int resource;
    ArrayList<Member> members;
    ArrayList<Member> membersFilter;

    public MemberAdapters(@NonNull Activity context, int resource, ArrayList<Member> members) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.members = members;
        this.membersFilter = new ArrayList<Member>();
        this.membersFilter.addAll(members);
    }

    @Override
    public int getCount(){
        return membersFilter.size();
    }

    @Override
    public Member getItem(int position){
        return membersFilter.get(position);
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

        ImageView avtMemer = customView.findViewById(R.id.avtUsersmall);
        TextView nameMember = customView.findViewById(R.id.nameMember);
        TextView phone = customView.findViewById(R.id.phoneMember);
        TextView role = customView.findViewById(R.id.roleMember);

        Member member = getItem(position);

        try{
            Glide.with(customView)
                    .load(member.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avtMemer);
            nameMember.setText("Name: " + member.getName());
            phone.setText("Phone: " + member.getPhone());
            if (member.getIsHost() == true){
                role.setText("Role: Host");
                role.setTextColor(Color.parseColor("red"));
            }
            else{
                role.setText("Role: Member");
                role.setTextColor(Color.parseColor("green"));
            }
        }
        catch (Exception e){}

        return customView;
    }

    public void filterTag(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        membersFilter.clear();
        for (Member no : members) {
            if (no.getName()!=null&&no.getName().toString().toLowerCase(Locale.getDefault()).contains(charText)) {
                membersFilter.add(no);
            }
        }
        notifyDataSetChanged();
    }
}
