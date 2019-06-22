package com.example.fatma.graduation_demo_user.Models;

import org.json.JSONArray;
import org.json.JSONObject;

public class near_by_model {
    public String formatted_address,icon,id,name,place_id,image_url;
    public double lat,lng,price_level,rating;
    public Boolean open_now;
    public int user_ratings_total;
    public near_by_model(String formatted_address,double lat,double lng,String icon,String id,String name,Boolean open_now,
                         String place_id,double price_level,double rating,int user_ratings_total,String image_url){
        this.formatted_address=formatted_address;
        this.lat=lat;
        this.lng=lng;
        this.icon=icon;
        this.id=id;
        this.name=name;
        this.open_now=open_now;
        this.place_id=place_id;
        this.price_level=price_level;
        this.rating=rating;
        this.user_ratings_total=user_ratings_total;
        this.image_url=image_url;
    }

}
