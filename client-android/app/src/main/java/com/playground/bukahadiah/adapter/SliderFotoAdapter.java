package com.playground.bukahadiah.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by aderifaldi on 21-May-17.
 */

public class SliderFotoAdapter extends PagerAdapter {

    private Activity activity;
    private String[] images;

    public SliderFotoAdapter(Activity context, String[] _images) {
        activity = context;
        images = _images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view == object) return true;
        else return false;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Holder holder = new Holder();

        if (holder.itemView == null) {
            holder.mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder.itemView = holder.mLayoutInflater.inflate(R.layout.item_image_slider, container, false);
        }

        holder.imageGalery = (PhotoView) holder.itemView.findViewById(R.id.photo);

        holder.imageURL = images[position];
        Glide.with(activity)
                .load(holder.imageURL)
                .crossFade()
                .into(holder.imageGalery);

        container.addView(holder.itemView);
        return holder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    static class Holder {
        View itemView;
        LayoutInflater mLayoutInflater;
        PhotoView imageGalery;
        String imageURL;
    }
}