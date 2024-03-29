package com.example.fatma.graduation_demo_user.activities.uber_activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.custom.DirectionsJSONParser;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class uber_confirm_drop extends AppCompatActivity {
    private RequestQueue queue;
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    MapView mMapView;
    private double pick_up_lat,pick_up_lng,drop_off_lat,drop_off_lng;
    private String state,city,address;
    private static final float DEFAULT_ZOOM = 15f;
    private AutocompleteSupportFragment autocompleteFragment;
    private TextView drop_off;
    private Polyline polylineFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_confirm_drop);
        pick_up_lat=getIntent().getDoubleExtra("lattitude",0);
        pick_up_lng=getIntent().getDoubleExtra("longitude",0);

        Places.initialize(this, "AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg");
        PlacesClient placesClient = Places.createClient(this);

        drop_off = (TextView) findViewById(R.id.drop_off);
        mMapView = (MapView)findViewById(R.id.mapView);
        Confirm_Leaving_from=(Button)findViewById(R.id.Confirm_Leaving_from);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("EG");

        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {


            @Override
            public void onPlaceSelected(Place place) {

                moveCamera(place.getLatLng(),DEFAULT_ZOOM,"Current Place");
                drop_off_lat=place.getLatLng().latitude;
                drop_off_lng=place.getLatLng().longitude;
                address=place.getAddress();
                Geocoder gcd = new Geocoder(uber_confirm_drop.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(drop_off_lat, drop_off_lng, 1);
                    city=addresses.get(0).getLocality();
                    state=addresses.get(0).getAdminArea();
                    drop_off.setText(addresses.get(0).getAddressLine(0));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        Confirm_Leaving_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drop_off_lng>0){
                    Intent uber_confirm_ride=new Intent(uber_confirm_drop.this,uber_confirm_ride.class);
                    uber_confirm_ride.putExtra("pick_up_lat",pick_up_lat);
                    uber_confirm_ride.putExtra("pick_up_lng",pick_up_lng);
                    uber_confirm_ride.putExtra("drop_off_lat",drop_off_lat);
                    uber_confirm_ride.putExtra("drop_off_lng",drop_off_lng);
                    startActivity(uber_confirm_ride);
                }else {

                }

            }
        });
        display_map();
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideSoftKeyboard();
    }
    private void hideSoftKeyboard(){
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
    private void display_map(){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
                double lat= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_latitude",""));
                double lng= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_longitude",""));

                if (lat>0) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(18).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
               googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                   @Override
                   public void onCameraIdle() {
                       LatLng latLng=googleMap.getCameraPosition().target;
                       drop_off_lat=latLng.latitude;
                       drop_off_lng=latLng.longitude;
                       Geocoder gcd = new Geocoder(uber_confirm_drop.this, Locale.getDefault());
                       try {
                           List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                           city=addresses.get(0).getLocality();
                           state=addresses.get(0).getAdminArea();
                           drop_off.setText(addresses.get(0).getAddressLine(0));
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });
                googleMap.setMyLocationEnabled(true);


            }


        });
    }



}
