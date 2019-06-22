package com.example.fatma.graduation_demo_user.activities.uber_activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatma.graduation_demo_user.Models.driver_model;
import com.example.fatma.graduation_demo_user.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class uber_pickup extends AppCompatActivity {
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    MapView mMapView;
    private double latt,longt;
    private String state,city,address;
    private static final float DEFAULT_ZOOM = 15f;
    private AutocompleteSupportFragment autocompleteFragment;
    private TextView drop_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_pickup);

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

                moveCamera(place.getLatLng(),DEFAULT_ZOOM,place.getName());
                latt=place.getLatLng().latitude;
                longt=place.getLatLng().longitude;
                address=place.getAddress();
                drop_off.setText(address);
                Geocoder gcd = new Geocoder(uber_pickup.this, Locale.getDefault());
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
                    Intent uber_confirm_drop=new Intent(uber_pickup.this,uber_confirm_drop.class);
                    uber_confirm_drop.putExtra("lattitude",latt);
                    uber_confirm_drop.putExtra("longitude",longt);
                    startActivity(uber_confirm_drop);
                }else {
                    double lat= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_latitude",""));
                    double lng= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_longitude",""));
                    Intent uber_confirm_drop=new Intent(uber_pickup.this,uber_confirm_drop.class);
                    uber_confirm_drop.putExtra("lattitude",lat);
                    uber_confirm_drop.putExtra("longitude",lng);
                    startActivity(uber_confirm_drop);
                }

            }
        });
        display_map();

    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            googleMap.clear();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker);

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(icon)
                    .draggable(true);
            googleMap.addMarker(options);
        }

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
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker);

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker Title")
                            .snippet("Marker Description").icon(icon).draggable(true));
                    Geocoder gcd = new Geocoder(uber_pickup.this, Locale.getDefault());
                    try {
                        List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                        city=addresses.get(0).getLocality();
                        state=addresses.get(0).getAdminArea();
                        drop_off.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker arg0) {
                        // TODO Auto-generated method stub
                        Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onMarkerDragEnd(Marker arg0) {
                        // TODO Auto-generated method stub
                        Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                        latt=arg0.getPosition().latitude;
                        longt=arg0.getPosition().longitude;

                    }

                    @Override
                    public void onMarkerDrag(Marker arg0) {
                        // TODO Auto-generated method stub
                        Log.i("System out", "onMarkerDrag...");
                    }
                });

            }


        });
    }

}
