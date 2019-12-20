package com.ygaps.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ygaps.travelapp.Object.Member;
import com.ygaps.travelapp.adapter.MemberAdapters;

import java.util.ArrayList;
import java.util.List;

public class ListMemberActivity extends AppCompatActivity {

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
        title.setText("List Members of Tour");
        addControl();
        Show();
        addSearch();
    }

    protected void addSearch() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                memberAdapters.filterTag(text);
                return false;
            }
        });
    }

    protected void Show() {
        Intent intent = getIntent();
        List<Member> listMember = (List<Member>)intent.getSerializableExtra("MemberList");
        memberAdapters = new MemberAdapters(ListMemberActivity.this, R.layout.items_list_members,
                (ArrayList<Member>) listMember);
        lvMembers.setAdapter(memberAdapters);
    }

    protected void addControl() {
        lvMembers = findViewById(R.id.list_member);
        search = findViewById(R.id.search_Member);
    }
}
