package com.example.fatma.graduation_demo_user.services;

        import com.google.firebase.iid.FirebaseInstanceId;
        import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by gaber on 26/08/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedtoken= FirebaseInstanceId.getInstance().getToken();


    }




}
