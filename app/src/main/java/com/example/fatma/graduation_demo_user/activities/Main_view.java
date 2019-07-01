package com.example.fatma.graduation_demo_user.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.blabla_activities.trip_search;
import com.example.fatma.graduation_demo_user.activities.marsol_activities.marsol_pick_up_place;
import com.example.fatma.graduation_demo_user.activities.uber_activities.uber_pickup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Main_view extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view2);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        get_location();
    }

    public void open_uber(View view) {
        Intent uber=new Intent(this,uber_pickup.class);
        startActivity(uber);


    }

    public void open_bla(View view) {
        Intent bla_bla=new Intent(this,trip_search.class);
        startActivity(bla_bla);

    }

    public void open_marsool(View view) {
        Intent marsol_pick_up=new Intent(this,marsol_pick_up_place.class);
        startActivity(marsol_pick_up);

    }
    @SuppressLint("MissingPermission")
    private void get_location() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            getSharedPreferences("personal_data", MODE_PRIVATE)
                                    .edit()
                                    .putString("person_latitude", String.valueOf(lat))
                                    .putString("person_longitude", String.valueOf(lng))
                                    .apply();
                            // Logic to handle location object
                        }
                    }
                });
    }
}
