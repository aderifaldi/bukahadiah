package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        pageTitle.setText("Settings");

    }

    @OnClick({R.id.back, R.id.privacy, R.id.faq, R.id.about, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.privacy:
                break;
            case R.id.faq:
                break;
            case R.id.about:
                break;
            case R.id.logout:
                GlobalVariable.saveIsLogin(getApplicationContext(), false);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                backAnimation();
                finish();
                break;
        }
    }
}
