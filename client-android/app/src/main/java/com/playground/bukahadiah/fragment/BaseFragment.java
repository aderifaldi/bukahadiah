package com.playground.bukahadiah.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.api.ApiServiceBH;
import com.playground.bukahadiah.api.ApiServiceBL;
import com.playground.bukahadiah.api.ServiceGeneratorBH;
import com.playground.bukahadiah.api.ServiceGeneratorBL;
import com.playground.bukahadiah.customui.textview.FontPath;
import com.radyalabs.irfan.util.AppUtility;

/**
 * Created by RadyaLabs PC on 10/04/2017.
 */

public class BaseFragment extends Fragment {

    public Typeface tfRegular, tfLight;
    public ProgressDialog loading;

    public ApiServiceBL apiServiceBL = null;
    public ApiServiceBH apiServiceBH = null;

    public JsonObject jsonPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tfRegular = Typeface.createFromAsset(getActivity().getAssets(), FontPath.REGULAR);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), FontPath.LIGHT);

        apiServiceBL = ServiceGeneratorBL.createService(ApiServiceBL.class);
        apiServiceBH = ServiceGeneratorBH.createService(ApiServiceBH.class);

        jsonPost = new JsonObject();
    }

    public void showLoading(){
        loading = AppUtility.showLoading(loading, getActivity());
        loading.setCancelable(false);
    }

    public void dismissLoading(){
        loading.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void goToAnimation(){
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void backAnimation(){
        getActivity().overridePendingTransition(R.anim.left_back_in, R.anim.left_back_out);
    }

    public void exitByBackByPresses() {
        getActivity().finish();
        backAnimation();
    }

}
