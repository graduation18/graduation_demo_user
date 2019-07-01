package com.example.fatma.graduation_demo_user.Models;

public class promo_code {
    public int id ;
    public String promo;
    public int value ;
    public String message_text;
    public long time;
    public promo_code(int id,
                      String promo,
                      int value,
                      String message_text,
                      long time){
        this.id=id ;
        this.promo=promo;
        this.value =value;
        this.message_text=message_text;
        this.time=time;

    }
}
