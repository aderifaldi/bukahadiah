package com.playground.bukahadiah.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.radyalabs.irfan.util.AppUtility;

/**
 * Created by aderifaldi on 08/12/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        AppUtility.logD(TAG, "Refreshed token: " + refreshedToken);
        GlobalVariable.saveFCMToken(this, refreshedToken);

    }

}
