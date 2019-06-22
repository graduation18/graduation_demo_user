package com.example.fatma.graduation_demo_user.Models;

public class driver_model {
    public int age;
    public String image,name,phone,token;
    public int rate;

    public driver_model(String image,String name,String phone,String token,int age,int rate){
        this.age=age;
        this.image=image;
        this.name=name;
        this.phone=phone;
        this.token=token;
        this.rate=rate;
    }

    public driver_model(){

    }
}
