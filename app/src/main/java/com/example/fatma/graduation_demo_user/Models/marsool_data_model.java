package com.example.fatma.graduation_demo_user.Models;

public class marsool_data_model {
    public double from_lat,from_lng,to_lat,to_lng;
    public String describtion,key;
    public marsool_data_model(double from_lat,double from_lng,double to_lat,double to_lng,String describtion,String key){
        this.from_lat=from_lat;
        this.from_lng=from_lng;
        this.to_lat=to_lat;
        this.to_lng=to_lng;
        this.key=key;
        this.describtion=describtion;

    }
    public marsool_data_model(){

    }
}
