package com.hzj.eventcollection.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzj.event.BaseActivity;
import com.hzj.eventcollection.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickA(View v) {
        startActivity(new Intent(this, PageActivity.class));
    }

}
