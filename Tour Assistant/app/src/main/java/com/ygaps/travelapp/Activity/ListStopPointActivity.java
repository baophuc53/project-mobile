package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.StopPoint;
import com.ygaps.travelapp.adapter.StopPointAdapters;
import com.ygaps.travelapp.model.StopPointResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListStopPointActivity extends AppCompatActivity {

    ListView lvStopPoints;
    StopPointAdapters stopPointAdapters;
    SearchView search;
    TextView NoStopPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stop_point);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView  title = findViewById(R.id.actionbar_textview);
        title.setText("Stop Points");
        addControl();
        Show();
        addSeach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent=getIntent();
        if (intent.getBooleanExtra("isMyTour", false))
            getMenuInflater().inflate(R.menu.add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPoint:
                Intent thisIntent=getIntent();
                Intent intent=new Intent(ListStopPointActivity.this, MapsActivity.class);
                int id = (int)thisIntent.getLongExtra("tourId",0);
                intent.putExtra("tourId", id);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSeach() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                stopPointAdapters.filterTag(text);
                return false;
            }
        });
    }

    private void Show() {
            Intent intent = getIntent();
            boolean isnull = intent.getBooleanExtra("isNull", true);
            if (isnull == false) {
                final List<StopPoint> listStopPoint = (List<StopPoint>) intent.getSerializableExtra("StopPointList");
                stopPointAdapters = new StopPointAdapters(ListStopPointActivity.this, R.layout.items_list_stop_point,
                        (ArrayList<StopPoint>) listStopPoint);
                lvStopPoints.setAdapter(stopPointAdapters);
                lvStopPoints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListStopPointActivity.this);
                        builder.setMessage("Do you want to delete this stop point ?")
                                .setCancelable(false)
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        UserService userService;
                                        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
                                        userService.RemoveStopPoint(stopPointAdapters.getItem(position).getId(), new Callback<StopPointResponse>() {
                                            @Override
                                            public void success(StopPointResponse stopPointResponse, Response response) {
                                                Toast.makeText(ListStopPointActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                listStopPoint.remove(stopPointAdapters.getItem(position));
                                                stopPointAdapters.notifyDataSetChanged();
                                                recreate();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                switch (error.getKind()) {
                                                    case HTTP:
                                                        if (error.getResponse().getStatus() == 403)
                                                            Toast.makeText(ListStopPointActivity.this, "Not permission to remove stop point", Toast.LENGTH_LONG).show();
                                                        else if (error.getResponse().getStatus() == 404)
                                                            Toast.makeText(ListStopPointActivity.this, "Stop point is not found", Toast.LENGTH_LONG).show();
                                                        break;
                                                    default:
                                                        Toast.makeText(ListStopPointActivity.this, "Fail", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Warning");
                        alert.show();
                        return true;
                    }
                });

                lvStopPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ListStopPointActivity.this, PointInfoActivity.class);
                        int pointId = stopPointAdapters.getItem(position).getServiceId();
                        if (pointId > 0) {
                            intent.putExtra("id", pointId);
                            startActivity(intent);
                        }
                    }
                });
            }
            else{
                NoStopPoint.setText("No stop point");
                NoStopPoint.setVisibility(View.VISIBLE);
            }
    }

    private void addControl() {
        lvStopPoints = findViewById(R.id.list_stop_point);
        search = findViewById(R.id.search_stopPoint);
        NoStopPoint = findViewById(R.id.noStopPointTv);
    }
}
