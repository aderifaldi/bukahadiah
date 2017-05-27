package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.SliderFotoAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProduct;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoSliderActivity extends BaseActivity {

    @BindView(R.id.fotoSlider)
    ViewPager fotoSlider;
    @BindView(R.id.textIndicator)
    CustomTextView textIndicator;

    private BLProduct.Product product;
    private SliderFotoAdapter adapter;
    private String posisi, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider);
        ButterKnife.bind(this);

        product = (BLProduct.Product) getIntent().getExtras().getSerializable("product");

        total = "" + product.images.length;
        textIndicator.setText("1/" + total);

        adapter = new SliderFotoAdapter(this, product.images);

        fotoSlider.setAdapter(adapter);
        fotoSlider.setCurrentItem(0);

        fotoSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                posisi = "" + (position + 1);
                textIndicator.setText(posisi + "/" + total);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
