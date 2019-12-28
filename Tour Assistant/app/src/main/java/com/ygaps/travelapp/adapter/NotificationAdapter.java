package com.ygaps.travelapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.InviteNotification;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.ProcessInvitationRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Long.parseLong;

public class NotificationAdapter extends ArrayAdapter<InviteNotification> {
    Activity context;
    int resource;
    ArrayList<InviteNotification> inviteNotifications;

    public NotificationAdapter(@NonNull Activity context, int resource, ArrayList<InviteNotification> inviteNotifications) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.inviteNotifications = inviteNotifications;
    }

    @Override
    public int getCount(){
        return inviteNotifications.size();
    }

    @Override
    public InviteNotification getItem(int position){
        return inviteNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        final View customView = inflater.inflate(this.resource, null);
        TextView notification=customView.findViewById(R.id.notification);
        TextView rightBtn=customView.findViewById(R.id.rightBtn);
        TextView leftBtn=customView.findViewById(R.id.leftBtn);
        TextView centerBtn=customView.findViewById(R.id.centerBtn);

        final InviteNotification inviteNotification =getItem(position);

        switch(inviteNotification.getType())
        {
            case "6":
                rightBtn.setText("Chấp nhận");
                rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProcessInvitationRequest processInvitationRequest=new ProcessInvitationRequest();
                        processInvitationRequest.setTourId(inviteNotification.getId());
                        processInvitationRequest.isAccepted=true;
                        UserService userService= MyAPIClient.getInstance().getAdapter().create(UserService.class);
                        userService.ResponseInvitation(processInvitationRequest, new Callback<DefaultResponse>() {
                            @Override
                            public void success(DefaultResponse defaultResponse, Response response) {
                                Toast.makeText(customView.getContext(),"Thành công",Toast.LENGTH_LONG ).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("inviteResponse", "Failure");
                            }
                        });
                    }
                });
                centerBtn.setText("Từ Chối");
                centerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProcessInvitationRequest processInvitationRequest=new ProcessInvitationRequest();
                        processInvitationRequest.setTourId(inviteNotification.getId());
                        processInvitationRequest.isAccepted=false;
                        UserService userService= MyAPIClient.getInstance().getAdapter().create(UserService.class);
                        userService.ResponseInvitation(processInvitationRequest, new Callback<DefaultResponse>() {
                            @Override
                            public void success(DefaultResponse defaultResponse, Response response) {
                                Toast.makeText(customView.getContext(),"Thành công",Toast.LENGTH_LONG ).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("inviteResponse", "Failure");
                            }
                        });
                    }
                });
                notification.setText("Bạn được mời tham gia tour "+ inviteNotification.getName());
                leftBtn.setVisibility(View.INVISIBLE);
                break;
        }

        return customView;
    }

}
