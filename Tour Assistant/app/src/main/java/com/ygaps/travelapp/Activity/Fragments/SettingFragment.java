package com.ygaps.travelapp.Activity.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ygaps.travelapp.Activity.ChangePasswordActivity;
import com.ygaps.travelapp.Activity.EditProfileActivity;
import com.ygaps.travelapp.Activity.LoginActivity;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.model.UserInfoResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.io.Serializable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingFragment extends Fragment {

    ImageView avtUser;
    TextView nameUser, editProfile;
    Button signout, nextChangePass;
    FrameLayout changePass;
    UserInfoResponse userInfo = new UserInfoResponse();
    Activity currentActivity;

    @Override
    public void onResume() {
        super.onResume();
        getUserInfor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        currentActivity=getActivity();
        View view = inflater.inflate(R.layout.fragment_setting_account, container, false);
        addControl(view);
        getUserInfor();
        addSignOutEvent();
        addEditProfileEvent();
        addChangePasswordEvent();
        return view;
    }

    private void addChangePasswordEvent() {
       changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
       nextChangePass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(currentActivity, ChangePasswordActivity.class);
               startActivity(intent);
           }
       });
    }

    private void addEditProfileEvent() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, EditProfileActivity.class);
                intent.putExtra("userInfo", (Serializable) userInfo);
                startActivity(intent);
            }
        });
    }

    private void getUserInfor() {
        final ProgressDialog progress = new ProgressDialog(currentActivity);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        final SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");

        UserService userService;
        MyAPIClient.getInstance().setAccessToken(Token);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        userService.getUserInfo(new Callback<UserInfoResponse>() {
            @Override
            public void success(UserInfoResponse userInfoResponse, Response response) {
                progress.dismiss();
                userInfo = userInfoResponse;
                if (userInfo.getTypeLogin() == 0)
                {
                    changePass.setVisibility(View.VISIBLE);
                }
                addUserInfo(userInfoResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                switch (error.getKind()) {
                    case HTTP:
                        if (error.getResponse().getStatus() == 401)
                            Toast.makeText(currentActivity, "Không tìm thấy access token", Toast.LENGTH_SHORT).show();
                        if (error.getResponse().getStatus() == 503)
                            Toast.makeText(currentActivity, "Lỗi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(currentActivity, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUserInfo(UserInfoResponse user) {
        try {
            Glide.with(currentActivity)
                    .load(user.getAvatar())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bg_avatar_user))
                    .into(avtUser);
                nameUser.setText(user.getFullName());
        } catch (Exception e) {
        }
    }



    private void addSignOutEvent() {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(currentActivity);
                alertDialogBuilder.setMessage("Do you want to sign out?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("Data", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean loginByFB = sharedPreferences.getBoolean("LoginByFB", false);
                        editor.clear();
                        editor.commit();
                        if (loginByFB == true) {
                            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                    .Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {
                                    LoginManager.getInstance().logOut();
                                }
                            }).executeAsync();
                        }
                        Intent intent = new Intent(currentActivity, LoginActivity.class);
                        startActivity(intent);
                        currentActivity.finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                });
                alertDialogBuilder.show();
            }
        });
    }

    private void addControl(View view) {
        avtUser = view.findViewById(R.id.avtUser);
        nameUser = view.findViewById(R.id.nameUser);
        editProfile = view.findViewById(R.id.editProfile);
        signout = view.findViewById(R.id.SignOutButton);
        changePass = view.findViewById(R.id.change_password);
        nextChangePass = view.findViewById(R.id.change_password_btn);
    }
}
