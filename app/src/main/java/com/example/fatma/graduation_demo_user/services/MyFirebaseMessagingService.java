package com.example.fatma.graduation_demo_user.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.fatma.graduation_demo_user.R;
import com.example.fatma.graduation_demo_user.activities.Main_view;
import com.example.fatma.graduation_demo_user.custom.DatabaseHandler;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.fatma.graduation_demo_user.custom.App.CHANNEL_1_ID;

/**
 * Created by gaber on 26/08/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManagerCompat notificationManager;
    private DatabaseHandler db;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {

                notificationManager = NotificationManagerCompat.from(this);
                db=new DatabaseHandler(this);
                find_user(remoteMessage);

            Log.w("message",remoteMessage.getData().toString());




        }
    }
    private void find_user(RemoteMessage remoteMessage)  {
        try {
        JSONObject data=new JSONObject(remoteMessage.getData());
        int value=data.getInt("value");
        String promo=data.getString("promo");
        String text=data.getString("text");
        String type = data.getString("type");
        if (type.equals("promo")){
            db.insertphone_number(promo,text,value,System.currentTimeMillis());
            notification_promo(promo,text,value,System.currentTimeMillis());
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

 private void notification_promo(String promo,String text,int value,long time){
     Intent activityIntent = new Intent(this, Main_view.class);
     PendingIntent contentIntent = PendingIntent.getActivity(this,
             0, activityIntent, 0);

     Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
             .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
             .setContentTitle(getResources().getString(R.string.Promo))
             .setContentInfo(text+" "+promo)
             .setPriority(NotificationCompat.PRIORITY_HIGH)
             .setCategory(NotificationCompat.CATEGORY_MESSAGE)
             .setColor(Color.WHITE)
             .setContentIntent(contentIntent)
             .setAutoCancel(false)
             .setOnlyAlertOnce(true)
             .build();


     notificationManager.notify(1, notification);
 }

}

