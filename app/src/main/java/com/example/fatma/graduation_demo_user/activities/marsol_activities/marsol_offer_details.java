package com.example.fatma.graduation_demo_user.activities.marsol_activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.fatma.graduation_demo_user.Models.marsool_data_model;
import com.example.fatma.graduation_demo_user.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class marsol_offer_details extends AppCompatActivity {
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    private EditText marsol_offer_describtion;
    MapView mMapView;
    private double pick_up_lat, pick_up_lng;
    private String state, city, address;
    private static final float DEFAULT_ZOOM = 15f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marsol_offer_details);
        hideSoftKeyboard();
        mMapView = (MapView) findViewById(R.id.mapView);
        Confirm_Leaving_from = (Button) findViewById(R.id.Confirm_Leaving_from);
        marsol_offer_describtion=(EditText)findViewById(R.id.marsol_offer_describtion);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        display_map();
        Confirm_Leaving_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  describtion=marsol_offer_describtion.getText().toString();
                if (describtion.length()>10){
                    add_offer(new marsool_data_model(pick_up_lat,pick_up_lng,describtion,getSharedPreferences("logged_in",MODE_PRIVATE).getString("key","")));
                }
            }
        });
    }


    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void display_map() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
                double lat = Double.parseDouble(getSharedPreferences("personal_data", MODE_PRIVATE).getString("person_latitude", ""));
                double lng = Double.parseDouble(getSharedPreferences("personal_data", MODE_PRIVATE).getString("person_longitude", ""));

                if (lat > 0) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker Title").snippet("Marker Description"));


                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            }


        });
    }
    private void add_offer(marsool_data_model offer) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("delivery_offers").push();
        myRef.setValue(offer);
        add_offer_location(offer.lat,offer.lng,myRef.getKey());

    }
    private void add_offer_location(final double Latt, final double longt,String offer_key){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("delivery_offers_locations");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.setLocation(offer_key, new GeoLocation(Latt,longt), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }


        });


    }


}
