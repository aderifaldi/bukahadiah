package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.helper.GlobalVariable;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GlobalVariable.getIsLogin(getApplicationContext())){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                goToAnimation();
                finish();
            }
        }, 2000);

    }

}
