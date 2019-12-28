package com.ygaps.travelapp.Activity.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.ygaps.travelapp.Activity.MainActivity;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.InviteNotification;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.TokenRequest;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        InviteNotification notification=new Gson().fromJson(remoteMessage.getData().toString(), InviteNotification.class);
        // handle a notification payload.
        Log.d(TAG, "Message Notification Body: " + notification);

        SharedPreferences sharedPreferences=getSharedPreferences("Notification",0);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        int length= sharedPreferences.getInt("length",0);
        editor.putString("Notification"+length, new Gson().toJson(notification));
        editor.putInt("length",length+1);
        editor.commit();
        sendNotification(notification);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setFcmToken(token);
        tokenRequest.setPlatform(1);
        tokenRequest.setAppVersion("1.0");

        String deviceId;
        deviceId = getDeviceId(MyFirebaseService.this);
        tokenRequest.setDeviceId(deviceId);

        UserService userService= MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.RegisterToken(tokenRequest, new Callback<DefaultResponse>() {
            @Override
            public void success(DefaultResponse defaultResponse, Response response) {
                Log.i("register firebase", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("register firebase", "failure");
            }
        });
    }

    private String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return androidId;
    }


    private void sendNotification(InviteNotification messageBody) {
        String channelId = getString(R.string.app_name);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId);

        Intent intent = new Intent(this,  MainActivity.class);
        intent.putExtra("fragment","Notification");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        switch(messageBody.getType())
        {
            case "6":

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle(channelId)
                        .setContentText("Bạn đươc mời tham gia tour" +messageBody.getName())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }}
