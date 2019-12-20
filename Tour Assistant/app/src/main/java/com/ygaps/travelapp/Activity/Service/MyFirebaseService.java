package com.ygaps.travelapp.Activity.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ygaps.travelapp.Activity.MainActivity;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.model.TokenRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // handle a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            sendNotification(remoteMessage.getNotification().getBody());
//        }
//    }
//
//    @Override
//    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);
//
//        sendRegistrationToServer(token);
//    }
//
//    private void sendRegistrationToServer(String token) {
//        TokenRequest tokenRequest = new TokenRequest();
//        tokenRequest.setFcmToken(token);
//
//        TelephonyManager telephonyManager;
//        telephonyManager = (TelephonyManager) getSystemService(Context.
//                TELEPHONY_SERVICE);
//        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            String deviceId = telephonyManager.getDeviceId();
//            tokenRequest.setDevideId(deviceId);
//        }
//
//        UserService userService= MyAPIClient.getInstance().getAdapter().create(UserService.class);
//        userService.RegisterToken(tokenRequest, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.i(TAG, "success");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e(TAG, "failure");
//            }
//        });
//    }
//
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.project_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
//                        .setContentTitle(getString(R.string.project_id))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent)
//                        .setDefaults(Notification.DEFAULT_ALL)
//                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                        .addAction(new NotificationCompat.Action(
//                                android.R.drawable.sym_call_missed,
//                                "Cancel",
//                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)))
//                        .addAction(new NotificationCompat.Action(
//                                android.R.drawable.sym_call_outgoing,
//                                "OK",
//                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
}
