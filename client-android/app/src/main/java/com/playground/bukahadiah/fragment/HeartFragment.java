package com.playground.bukahadiah.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class HeartFragment extends BaseFragment {

    @BindView(R.id.pageName)
    CustomTextView pageName;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_empty, container, false);
        ButterKnife.bind(this, view);

        pageName.setText("My Event");

        return view;
    }

}
