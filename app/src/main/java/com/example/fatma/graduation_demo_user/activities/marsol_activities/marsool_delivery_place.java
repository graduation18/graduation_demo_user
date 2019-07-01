package com.example.fatma.graduation_demo_user.activities.marsol_activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.fatma.graduation_demo_user.Models.marsool_data_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.Main_view;
import com.example.fatma.graduation_demo_user.activities.uber_activities.uber_confirm_drop;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class marsool_delivery_place extends AppCompatActivity {
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    MapView mMapView;
    private double latt,longt,pick_up_lat, pick_up_lng;;
    private String state,city,address,place_text,icon,describtion;
    private static final float DEFAULT_ZOOM = 15f;
    private AutocompleteSupportFragment autocompleteFragment;
    private TextView drop_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marsool_delivery_place);

        Places.initialize(this, "AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg");
        PlacesClient placesClient = Places.createClient(this);

        drop_off=(TextView)findViewById(R.id.drop_off);
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

        autocompleteFragment.setOnPlaceSelectedListener(
                new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {


            @Override
            public void onPlaceSelected(Place place) {

                moveCamera(place.getLatLng(),DEFAULT_ZOOM);
                latt=place.getLatLng().latitude;
                longt=place.getLatLng().longitude;
                address=place.getAddress();
                drop_off.setText(address);
                Geocoder gcd = new Geocoder(marsool_delivery_place.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(latt, longt, 1);
                    city=addresses.get(0).getLocality();
                    state=addresses.get(0).getAdminArea();
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
                if (latt>0){
                    add_offer(new marsool_data_model(pick_up_lat,pick_up_lng,latt,longt,describtion,getSharedPreferences("logged_in",MODE_PRIVATE).getString("key","")));

                }else {
                    double lat= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_latitude",""));
                    double lng= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_longitude",""));
                    add_offer(new marsool_data_model(pick_up_lat,pick_up_lng,lat,lng,describtion,getSharedPreferences("logged_in",MODE_PRIVATE).getString("key","")));

                }

            }
        });
        pick_up_lat=getIntent().getDoubleExtra("pick_up_lat",0);
        pick_up_lng=getIntent().getDoubleExtra("pick_up_lng",0);
        place_text=getIntent().getStringExtra("place_text");
        icon=getIntent().getStringExtra("icon");
        describtion=getIntent().getStringExtra("describtion");
        display_map();

    }
    private void moveCamera(LatLng latLng, float zoom){
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
                final double lat= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_latitude",""));
                double lng= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_longitude",""));

                if (lat>0) {
                    Geocoder gcd = new Geocoder(marsool_delivery_place.this, Locale.getDefault());
                    try {
                        List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                        city=addresses.get(0).getLocality();
                        state=addresses.get(0).getAdminArea();
                        drop_off.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(18).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng=googleMap.getCameraPosition().target;
                    latt=latLng.latitude;
                    longt=latLng.longitude;
                    Geocoder gcd = new Geocoder(marsool_delivery_place.this, Locale.getDefault());
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
    private void add_offer(marsool_data_model offer) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("delivery_offers").push();
        myRef.setValue(offer);
        add_offer_location(offer.from_lat,offer.from_lng,myRef.getKey());

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
                    Intent Main_view=new Intent(marsool_delivery_place.this,Main_view.class);
                    startActivity(Main_view);
                    finishAffinity();
                }
            }


        });


    }

}
