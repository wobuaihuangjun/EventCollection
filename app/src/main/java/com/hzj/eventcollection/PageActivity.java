package com.hzj.eventcollection;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PageActivity extends BaseActivity {

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager()));
    }


}
