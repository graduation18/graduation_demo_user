package com.example.fatma.graduation_demo_user.activities.blabla_activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.fatma.graduation_demo_user.Models.trip_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.adapters.trips_adapter;
import com.example.fatma.graduation_demo_user.custom.MyDividerItemDecoration;
import com.example.fatma.graduation_demo_user.custom.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class trips extends AppCompatActivity
{
    private List<trip_model> data_model_list = new ArrayList<>();
    private RecyclerView data_recyclerView;
    private trips_adapter data_adapter;
    private String from, to ;
    private long time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trips);
        from=getIntent().getStringExtra("from");
        to=getIntent().getStringExtra("to");
        time=getIntent().getLongExtra("time",0);

        data_recyclerView = findViewById(R.id.trips_recycler);
        data_adapter = new trips_adapter(this, data_model_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        data_recyclerView.setLayoutManager(mLayoutManager);
        data_recyclerView.setItemAnimator(new DefaultItemAnimator());
        data_recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 5));
        data_recyclerView.setAdapter(data_adapter);
        data_recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, data_recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                Intent confirm_trip=new Intent(trips.this, com.example.fatma.graduation_demo_user.activities.blabla_activities.confirm_trip.class);
                confirm_trip.putExtra("driver_id",data_model_list.get(position).driver_id);
                confirm_trip.putExtra("cost",data_model_list.get(position).cost);
                confirm_trip.putExtra("going_to_address",data_model_list.get(position).going_to_address);
                confirm_trip.putExtra("going_to_city",data_model_list.get(position).going_to_city);
                confirm_trip.putExtra(".going_to_latt",data_model_list.get(position).going_to_latt);
                confirm_trip.putExtra("going_to_longt",data_model_list.get(position).going_to_longt);
                confirm_trip.putExtra("going_to_state",data_model_list.get(position).going_to_state);
                confirm_trip.putExtra("Leave_from_address",data_model_list.get(position).Leave_from_address);
                confirm_trip.putExtra("Leave_from_city",data_model_list.get(position).Leave_from_city);
                confirm_trip.putExtra("Leave_from_latt",data_model_list.get(position).Leave_from_latt);
                confirm_trip.putExtra("Leave_from_longt",data_model_list.get(position).Leave_from_longt);
                confirm_trip.putExtra("Leave_from_state",data_model_list.get(position).Leave_from_state);
                confirm_trip.putExtra("seats_number",data_model_list.get(position).seats_number);
                confirm_trip.putExtra("time",data_model_list.get(position).time);
                startActivity(confirm_trip);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        search_for_ride(from,to,time,data_model_list);
    }
    private void search_for_ride(String leaving_from, final String Going_to, final long Date, final List<trip_model>list_of_trips)
    {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("trips");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){



                        data_model_list.add(
                                dataSnapshot1.getValue(trip_model.class)
                        );
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        data_adapter.notifyDataSetChanged();
    }
}
