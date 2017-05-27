package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSuccessActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.txtInfo)
    CustomTextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ButterKnife.bind(this);

        pageTitle.setText("Register Success");
        txtInfo.setText("Registration is successful, click the link sent to your e-mail address to confirm");

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
