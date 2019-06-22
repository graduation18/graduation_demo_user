package com.example.fatma.graduation_demo_user.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.uber_activities.uber_pickup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class mobile_authentication extends AppCompatActivity {


    ProgressBar progress1;
    CountryCodePicker ccp;
    AppCompatEditText edtPhoneNumber;
    CountDownTimer cTimer = null;
     Button request_verify;
     String user_key;


    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getSharedPreferences("logged_in",MODE_PRIVATE).getBoolean("state",false)){
            Intent main=new Intent(this,Main_view.class);
            startActivity(main);
            finish();
        }
        setContentView(R.layout.activity_mobile_authentication);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        edtPhoneNumber = (AppCompatEditText) findViewById(R.id.phone_number_edt);
        progress1=(ProgressBar)findViewById(R.id.progress1);
        request_verify=(Button)findViewById(R.id.request_verify);




    }


    public void verfiy(View view) {
        String phone="+"+ccp.getSelectedCountryCode()+edtPhoneNumber.getText().toString();
        if (phone.length()==13){
            check_user(edtPhoneNumber.getText().toString(),phone);
        }


    }


    private void check_user(final String sub_phone, final String phone)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query=reference.orderByChild("phone_number").equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean found=false;
                    for (DataSnapshot sub_type : dataSnapshot.getChildren()) {

                        user_key=sub_type.getKey();
                        if (!FirebaseInstanceId.getInstance().getToken().equals(sub_type.child("user_token").getValue())){
                            DatabaseReference myRef = sub_type.getRef();
                            myRef.child("user_token").setValue(FirebaseInstanceId.getInstance().getToken());
                            }
                            getSharedPreferences("logged_in",MODE_PRIVATE).edit()
                                    .putString("user_key", user_key)
                                    .commit();
                        Intent got_confirm_code=new Intent(mobile_authentication.this,confirm_code.class);
                        Log.w("dsaldj",phone);
                        got_confirm_code.putExtra("phone_number",phone);
                        got_confirm_code.putExtra("signed_in",true);
                        startActivity(got_confirm_code);
                        finish();


                    }


                }else {
                    Intent got_confirm_code=new Intent(mobile_authentication.this,confirm_code.class);
                    Log.w("dsaldj",phone);
                    got_confirm_code.putExtra("phone_number",phone);
                    got_confirm_code.putExtra("signed_in",false);
                    startActivity(got_confirm_code);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("khgj",databaseError.getMessage());


            }
        });

    }



}


