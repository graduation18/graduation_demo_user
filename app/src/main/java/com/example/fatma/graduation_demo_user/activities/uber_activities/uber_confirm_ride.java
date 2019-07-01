package com.example.fatma.graduation_demo_user.activities.uber_activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatma.graduation_demo_user.Models.driver_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.confirm_code;
import com.example.fatma.graduation_demo_user.activities.mobile_authentication;
import com.example.fatma.graduation_demo_user.custom.DirectionsJSONParser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class uber_confirm_ride extends AppCompatActivity {
    int radius=1;
    String driver_key=null;
    boolean driver_found=false;
    private driver_model driver;
    private RequestQueue queue;
    private GoogleMap googleMap;
    private Button Confirm_Leaving_from,promo_code;
    private TextView fare_estimation;
    MapView mMapView;
    private double pick_up_lat,pick_up_lng,drop_off_lat,drop_off_lng;
    private static final float DEFAULT_ZOOM = 15f;
    String dist_durat= null,distance= null,duration= null;
    private AlertDialog.Builder alert;
    private boolean promo_added=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_confirm_ride);
        pick_up_lat=getIntent().getDoubleExtra("pick_up_lat",0);
        pick_up_lng=getIntent().getDoubleExtra("pick_up_lng",0);
        drop_off_lat=getIntent().getDoubleExtra("drop_off_lat",0);
        drop_off_lng=getIntent().getDoubleExtra("drop_off_lng",0);
        mMapView = (MapView)findViewById(R.id.mapView);
        Confirm_Leaving_from=(Button)findViewById(R.id.Confirm_Leaving_from);
        fare_estimation=(TextView)findViewById(R.id.cost);
        promo_code=(Button)findViewById(R.id.promo_code);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Confirm_Leaving_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_closest_driver(new LatLng(pick_up_lat,pick_up_lng));
            }
        });
        promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!promo_added) {
                    show_promo_dialog();
                }
            }
        });
        display_map();

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
    private driver_model search_for_driver(String driver_id)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("drivers");
        Query query = reference.orderByKey().equalTo(driver_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot sub_type : dataSnapshot.getChildren()) {
                        driver=sub_type.getValue(driver_model.class);
                        Log.w("jisadhsdahu",driver.name);
                        send_message(driver.token);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (driver!=null){
            return driver;
        }
        return null;
    }
    private void get_closest_driver(final LatLng pickup)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("drivers_locations");
        GeoFire geoFire=new GeoFire(reference);
        GeoQuery query=geoFire.queryAtLocation(new GeoLocation(pickup.latitude,pickup.longitude),radius);
        query.removeAllListeners();
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driver_found){
                    driver_key=key;
                    driver_found=true;
                    Log.w("adssadsad",key);
                    search_for_driver(key);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driver_found) {
                    radius++;
                    get_closest_driver(pickup);
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private void send_message(String to_user_token)
    {


        try {
            JSONObject main = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("pick_up_lat",pick_up_lat);
            data.put("pick_up_lng",pick_up_lng);
            data.put("drop_off_lat",drop_off_lat);
            data.put("drop_off_lng",drop_off_lng);
            data.put("text","is near by you" );
            data.put("type","uber_pickup" );
            data.put("key",getSharedPreferences("logged_in",MODE_PRIVATE).getString("key",""));
            data.put("time",String .valueOf((int)System.currentTimeMillis()));
            main.put("data", data);
            main.put("to", to_user_token);
            Log.w("l,jkndsajhkadsji",main.toString());
            String url = "https://fcm.googleapis.com/fcm/send";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, main,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "key=AAAAKajycwU:APA91bGqfmnx9VmTrLU-idWtQydPA58ZxlrluTEmcmwieQpzH5HIvdiOmaAu9x2yqvxE9FAstBUmMHRn0-e61FuSc6lzSzHSariBXJhcB3Tmo6v3K07EYxDhMwlCDlXmLc4rtvQ8m0_g");

                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        } catch (Exception e) {

        }



    }
    private void display_map()
    {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);


                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pick_up_lat,pick_up_lng))
                        .icon(icon));
                BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.drop_off_marker);
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(drop_off_lat,drop_off_lng))
                        .icon(icon2);
                googleMap.addMarker(options);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(pick_up_lat,pick_up_lng))
                            .zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                get_direction_json(new LatLng(pick_up_lat,pick_up_lng),new LatLng(drop_off_lat,drop_off_lng),googleMap);

            }


        });
    }
    private void get_direction_json(LatLng origin, LatLng destination, final GoogleMap mMap)
    {


        try {
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+origin.latitude+","+origin.longitude+
                    "&destination="+destination.latitude+","+destination.longitude+"&mode=driving&key=AIzaSyDxy0ndkDYovy6TEo71pnhMhfopHbX4fpg";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parse_direction_json(response,mMap);
                            parse_direction_json(response);
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
        List<List<HashMap<String, String>>> routes = null;
        DirectionsJSONParser parser = new DirectionsJSONParser();
        routes = parser.parse(object);
        draw_poly_line(routes,mMap);

    }
    private void draw_poly_line(List<List<HashMap<String, String>>> result, GoogleMap mMap)
    {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap point = path.get(j);

                double lat = Double.parseDouble(String.valueOf(point.get("lat")));
                double lng = Double.parseDouble(String.valueOf(point.get("lng")));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            lineOptions.geodesic(true);

        }

// Drawing polyline in the Google Map for the i-th route
        mMap.addPolyline(lineOptions);
    }
    private void parse_direction_json(JSONObject object)
    {
        DirectionsJSONParser parser = new DirectionsJSONParser();
        dist_durat = parser.get_duration_distance(object);
        Log.w("lshdkjsa",dist_durat);
        get_seat_cost(dist_durat);

    }

    private void get_seat_cost(String dist_durat)
    {
        distance=dist_durat.split(",")[0].split("km")[0].replaceAll("\\s+","");
        duration=dist_durat.split(",")[1];
        double distanceInkilos = Double.parseDouble(distance)-1;
        double trip_cost=0;
        if (distanceInkilos<1){
             trip_cost=10;
        }else {
             trip_cost=distanceInkilos*3+7;
        }
        fare_estimation.setText(fare_estimation.getText().toString()+" "+String.valueOf(Math.round(trip_cost)));

    }

    private void show_promo_dialog(){
        alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage(getResources().getString(R.string.promo_code));
        alert.setTitle(getResources().getString(R.string.add_promo_code));

        alert.setView(edittext);

        alert.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                check_promo(YouEditTextValue);
            }
        });
        alert.show();
    }
    private void check_promo(final String promo)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("promo_code");
        Query query=reference.orderByChild("promo").equalTo(promo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sub_type : dataSnapshot.getChildren()) {
                        try {
                            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sub_type.child("valid_to").getValue(String.class));

                            if (new Date().before(date1)){
                                int original_cost= Integer.parseInt(fare_estimation.getText().toString().split(":")[1].trim());
                                int new_cost=original_cost*sub_type.child("value").getValue(Integer.class)/100;
                                fare_estimation.setText(String.valueOf(new_cost));
                                promo_added=true;
                            }else {
                                Toast.makeText(uber_confirm_ride.this,getResources().getString(R.string.promo_code_not_valid_any_more),Toast.LENGTH_LONG).show();

                            }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                    }

                }else {
                    Toast.makeText(uber_confirm_ride.this,getResources().getString(R.string.no_such_promo_code),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("khgj",databaseError.getMessage());


            }
        });

    }


}
