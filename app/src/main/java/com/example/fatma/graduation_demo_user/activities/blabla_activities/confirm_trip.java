package com.example.fatma.graduation_demo_user.activities.blabla_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.libraries.places.api.Places;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatma.graduation_demo_user.Models.driver_model;
import com.example.fatma.graduation_demo_user.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class confirm_trip extends AppCompatActivity {

    private RequestQueue queue;
    private driver_model driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip);
        search_for_driver(getIntent().getStringExtra("driver_id"));

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
    private void send_message(String to_user_token)
    {


        try {
            JSONObject main = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("text","wants to bok ride with you" );
            data.put("time",String .valueOf((int)System.currentTimeMillis()));
            main.put("data", data);
            main.put("to", to_user_token);
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

    public void confirm(View view) {
        send_message(driver.token);
    }
    }
