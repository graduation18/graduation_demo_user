package com.example.fatma.graduation_demo_user.activities.marsol_activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatma.graduation_demo_user.Models.near_by_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.custom.DirectionsJSONParser;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class marsol_pick_up_place extends AppCompatActivity {
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from;
    MapView mMapView;
    private double pick_up_lat, pick_up_lng;
    private String state, city, address;
    private static final float DEFAULT_ZOOM = 15f;
    private AutocompleteSupportFragment autocompleteFragment;
    private RadioGroup radioGroup;
    private RadioButton restaurant,super_markets,pharmacies;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marsol_pick_ip_place);

        Places.initialize(this, "AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg");
        PlacesClient placesClient = Places.createClient(this);

        mMapView = (MapView) findViewById(R.id.mapView);
        Confirm_Leaving_from = (Button) findViewById(R.id.Confirm_Leaving_from);
        radioGroup=(RadioGroup) findViewById(R.id.radio_group);
        restaurant=(RadioButton)findViewById(R.id.restaurant);
        super_markets=(RadioButton)findViewById(R.id.super_markets);
        pharmacies=(RadioButton)findViewById(R.id.pharmacies);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("EG");

        autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {


            @Override
            public void onPlaceSelected(Place place) {

                moveCamera(place.getLatLng(), DEFAULT_ZOOM, "Current Place");
                pick_up_lat = place.getLatLng().latitude;
                pick_up_lng = place.getLatLng().longitude;
                address = place.getAddress();
                Geocoder gcd = new Geocoder(marsol_pick_up_place.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(pick_up_lat, pick_up_lng, 1);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
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
                if (pick_up_lat > 0) {
                    Intent uber_confirm_ride = new Intent(marsol_pick_up_place.this,marsol_offer_details.class);
                    uber_confirm_ride.putExtra("pick_up_lat", pick_up_lat);
                    uber_confirm_ride.putExtra("pick_up_lng", pick_up_lng);

                    startActivity(uber_confirm_ride);
                } else {

                }

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.restaurant:
                        if(restaurant.isChecked())
                            get_near_by_json("restaurant",googleMap);
                        break;

                    case R.id.super_markets:
                        if(super_markets.isChecked())
                            get_near_by_json("supermarket",googleMap);

                        break;

                    case R.id.pharmacies:
                        if(pharmacies.isChecked())
                            get_near_by_json("pharmacy",googleMap);

                        break;
                }
            }
        });
        display_map();
    }
    private void moveCamera(LatLng latLng, float zoom, String title) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            googleMap.clear();
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            googleMap.addMarker(options);
        }

        hideSoftKeyboard();
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
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        pick_up_lat = marker.getPosition().latitude;
                        pick_up_lng = marker.getPosition().longitude;
                        Geocoder gcd = new Geocoder(marsol_pick_up_place.this, Locale.getDefault());
                        try {
                            List<Address> addresses = gcd.getFromLocation(pick_up_lat, pick_up_lng, 1);
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            address=addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

            }


        });
    }
    private void get_near_by_json(String place_type, final GoogleMap mMap)
    {


        try {
            double lat= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_latitude",""));
            double lng= Double.parseDouble(getSharedPreferences("personal_data",MODE_PRIVATE).getString("person_longitude",""));

            String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?location="+lat+","+lng
                    +"&radius=1500&type="+place_type+"&key=AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parse_direction_json(response,mMap);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        } catch (Exception e) {

        }


    }
    private void parse_direction_json(JSONObject object, GoogleMap mMap)
    {
        List<near_by_model>places=new ArrayList<>();
        DirectionsJSONParser parser = new DirectionsJSONParser();
        places = parser.parse_near_by(object);
        mMap.clear();
        for (near_by_model place:places){
            mMap.addMarker(new MarkerOptions().position(new LatLng(place.lat,place.lng))
            .title(place.name)
            .snippet(place.formatted_address)
            );
        }

    }


}

