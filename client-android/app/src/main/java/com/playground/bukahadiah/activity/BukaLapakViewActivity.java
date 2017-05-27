package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.playground.bukahadiah.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BukaLapakViewActivity extends BaseActivity {

    @BindView(R.id.webView) WebView webViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buka_lapak_view);
        ButterKnife.bind(this);

        showLoading();

        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setDefaultTextEncodingName("utf-8");

        webViewContent.loadUrl(getIntent().getStringExtra("url"));
        webViewContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            }
        });

    }
}
