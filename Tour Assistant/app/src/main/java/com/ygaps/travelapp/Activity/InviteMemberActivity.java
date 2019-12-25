package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.Member;
import com.ygaps.travelapp.adapter.MemberAdapters;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.InviteRequest;
import com.ygaps.travelapp.model.SearchUserResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.ygaps.travelapp.Activity.Constants.PAGE_NUM;
import static com.ygaps.travelapp.Activity.Constants.ROW_PER_PAGE;

public class InviteMemberActivity extends AppCompatActivity {
    ListView lvMembers;
    MemberAdapters memberAdapters;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_member);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("List Members");
        addControl();
        addSearch();
        addEventClickMember();
    }

    private void addEventClickMember() {
        lvMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(InviteMemberActivity.this);
                builder.setTitle("Invite");
                builder.setMessage("Bạn có muốn mời người này không?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=getIntent();
                        long tourId=intent.getLongExtra("Tour",0);
                        InviteRequest inviteRequest=new InviteRequest();
                        inviteRequest.setInvitedUserId(memberAdapters.getItem(position).getId().toString());
                        inviteRequest.setTourId(""+tourId);
                        inviteRequest.isInvited=intent.getBooleanExtra("isPrivate",false);

                        UserService userService;
                        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                        userService.inviteMember(inviteRequest, new Callback<DefaultResponse>() {
                            @Override
                            public void success(DefaultResponse defaultResponse, Response response) {
                                Toast.makeText(InviteMemberActivity.this, "Mời thành công", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(InviteMemberActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
                                Log.e("Error:",error.toString());
                            }
                        });
                    }
                });
                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }

    protected void addSearch(){
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                UserService userService;
                userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                userService.searchUser(text, PAGE_NUM,""+ROW_PER_PAGE, new Callback<SearchUserResponse>() {
                    @Override
                    public void success(SearchUserResponse searchUserResponse, Response response) {
                    memberAdapters=new MemberAdapters(InviteMemberActivity.this,
                                                        R.layout.items_list_members,(ArrayList<Member>) searchUserResponse.getMembers());
                    lvMembers.setAdapter(memberAdapters);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        switch (error.getKind()) {
                            case HTTP:
                                if (error.getResponse().getStatus() == 500)
                                    Toast.makeText(InviteMemberActivity.this, "Lỗi server", Toast.LENGTH_LONG).show();
                                break;
                            case NETWORK:
                            case UNEXPECTED:
                                Toast.makeText(InviteMemberActivity.this, "Có vấn đề về mạng", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(InviteMemberActivity.this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return false;
            }
        });
    }

    protected void addControl() {
        lvMembers = findViewById(R.id.list_member);
        search = findViewById(R.id.search_Member);
    }
}
