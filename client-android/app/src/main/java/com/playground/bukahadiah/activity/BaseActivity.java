package com.playground.bukahadiah.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.api.ApiServiceBH;
import com.playground.bukahadiah.api.ApiServiceBL;
import com.playground.bukahadiah.api.ApiServiceNotification;
import com.playground.bukahadiah.api.ServiceGeneratorBH;
import com.playground.bukahadiah.api.ServiceGeneratorBL;
import com.playground.bukahadiah.api.ServiceGeneratorNotification;
import com.playground.bukahadiah.customui.textview.FontPath;
import com.radyalabs.irfan.util.AppUtility;

/**
 * Created by RadyaLabs PC on 10/04/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public Typeface tfRegular, tfLight;
    public ProgressDialog loading;

    public ApiServiceBL apiServiceBL = null;
    public ApiServiceBH apiServiceBH = null;
    public ApiServiceNotification apiServiceNotification = null;

    public JsonObject jsonPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tfRegular = Typeface.createFromAsset(getAssets(), FontPath.REGULAR);
        tfLight = Typeface.createFromAsset(getAssets(), FontPath.LIGHT);

        apiServiceBL = ServiceGeneratorBL.createService(ApiServiceBL.class);
        apiServiceBH = ServiceGeneratorBH.createService(ApiServiceBH.class);
        apiServiceNotification = ServiceGeneratorNotification.createService(ApiServiceNotification.class);

        jsonPost = new JsonObject();
    }

    public void showLoading(){
        loading = AppUtility.showLoading(loading, this);
        loading.setCancelable(false);
    }

    public void dismissLoading(){
        loading.dismiss();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackByPresses();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void goToAnimation(){
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void backAnimation(){
        overridePendingTransition(R.anim.left_back_in, R.anim.left_back_out);
    }

    public void exitByBackByPresses() {
        finish();
        backAnimation();
    }

}
