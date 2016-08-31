package com.hzj.eventcollection.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzj.event.BaseFragment;
import com.hzj.eventcollection.R;

public class FragmentSecond extends BaseFragment {

    public static BaseFragment newInstance() {
        return new FragmentSecond();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, null);
    }
}
