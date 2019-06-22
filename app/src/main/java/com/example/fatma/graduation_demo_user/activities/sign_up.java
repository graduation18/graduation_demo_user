package com.example.fatma.graduation_demo_user.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.fatma.graduation_demo_user.Models.user_data_model;
import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.uber_activities.uber_pickup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rilixtech.CountryCodePicker;

import java.util.List;

public class sign_up extends AppCompatActivity {


    AppCompatEditText edtEmail;
    AppCompatEditText edtName;
    AppCompatEditText edtPassword;
    ProgressBar progress1;
    CountDownTimer cTimer = null;
    Button request_verify;
    String phone;


    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        phone=getIntent().getStringExtra("phone");
        progress1=(ProgressBar)findViewById(R.id.progress1);
        request_verify=(Button)findViewById(R.id.request_verify);
        progress1=(ProgressBar)findViewById(R.id.progress1);
        request_verify=(Button)findViewById(R.id.request_verify);
        edtEmail = (AppCompatEditText) findViewById(R.id.email);
        edtName = (AppCompatEditText) findViewById(R.id.name);
        edtPassword = (AppCompatEditText) findViewById(R.id.password);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void sign_up(String token ,String phone ,String email,String password,String name)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").push();

        user_data_model user=new user_data_model(phone,token,name,email,password);
        myRef.setValue(user);
        SharedPreferences.Editor editor = getSharedPreferences("logged_in", MODE_PRIVATE).edit();
        editor.putBoolean("state",true);
        editor.putString("key",myRef.getKey());
        editor.apply();
        Intent main=new Intent(this,uber_pickup.class);
        startActivity(main);
        finish();

    }

    public void verfiy(View view) {
        String email=edtEmail.getText().toString();
        String name=edtName.getText().toString();
        String password=edtPassword.getText().toString();
        if (phone.length()==13&&email.length()>0&&name.length()>0&&password.length()>8) {
            sign_up(FirebaseInstanceId.getInstance().getToken(),phone,email,password,name);
            Intent got_confirm_code = new Intent(sign_up.this, Main_view.class);
            got_confirm_code.putExtra("email",email) ;
            got_confirm_code.putExtra("name", name);
            got_confirm_code.putExtra("password",password );
            got_confirm_code.putExtra("phone",phone );

            startActivity(got_confirm_code);
            finish();
        }

    }}
