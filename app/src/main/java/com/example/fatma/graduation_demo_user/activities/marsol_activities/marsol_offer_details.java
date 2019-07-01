package com.example.fatma.graduation_demo_user.activities.marsol_activities;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fatma.graduation_demo_user.Models.marsool_data_model;
import com.example.fatma.graduation_demo_user.Models.near_by_model;
import com.example.fatma.graduation_demo_user.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

public class marsol_offer_details extends AppCompatActivity {
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    private EditText marsol_offer_describtion;
    private TextView place;
    MapView mMapView;
    private double pick_up_lat, pick_up_lng;
    private String place_text,icon;
    private static final float DEFAULT_ZOOM = 15f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marsol_offer_details);
        hideSoftKeyboard();
        mMapView = (MapView) findViewById(R.id.mapView);
        Confirm_Leaving_from = (Button) findViewById(R.id.Confirm_Leaving_from);
        marsol_offer_describtion=(EditText)findViewById(R.id.marsol_offer_describtion);
        place=(TextView)findViewById(R.id.place);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pick_up_lat=getIntent().getDoubleExtra("pick_up_lat",0);
        pick_up_lng=getIntent().getDoubleExtra("pick_up_lng",0);
        place_text=getIntent().getStringExtra("place_text");
        icon=getIntent().getStringExtra("icon");

        place.setText(place_text);

        display_map();
        Confirm_Leaving_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  describtion=marsol_offer_describtion.getText().toString();
                if (describtion.length()>10){
                    Intent marsool_delivery_place=new Intent(marsol_offer_details.this,marsool_delivery_place.class);
                    marsool_delivery_place.putExtra("pick_up_lat", pick_up_lat);
                    marsool_delivery_place.putExtra("pick_up_lng", pick_up_lng);
                    marsool_delivery_place.putExtra("place_text", place_text);
                    marsool_delivery_place.putExtra("icon", icon);
                    marsool_delivery_place.putExtra("describtion", describtion);



                    startActivity(marsool_delivery_place);                }
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

                if (lat == 0) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker Title").snippet("Marker Description"));


                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(pick_up_lat, pick_up_lng))
                            .zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    if (icon.equals("market")){
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.market);

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(pick_up_lat, pick_up_lng))
                                .title(getResources().getString(R.string.selected_place))
                                .icon(icon)
                                .snippet(place_text));
                        }else {
                        new TheTask(icon,pick_up_lat,pick_up_lng,getResources().getString(R.string.selected_place),place_text).execute();
                    }
                }

            }


        });
    }


    class TheTask extends AsyncTask<Void,Void,Void>
    {
        Bitmap bmp;
        String  icon,name,formatted_address;
        double lat,lng;

        public TheTask(String  icon,double lat,double lng,String name,String formatted_address){
            this.icon=icon;
            this.name=name;
            this.formatted_address=formatted_address;
            this.lat=lat;
            this.lng=lng;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            URL url ;
            try {
                url = new URL(icon);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .title(name)
                    .snippet(formatted_address)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        }
    }
}
