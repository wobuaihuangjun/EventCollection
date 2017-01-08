package com.hzj.eventcollection.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hzj.event.BaseActivity;
import com.hzj.eventcollection.ListViewAdapter;
import com.hzj.eventcollection.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> demo = new ArrayList<>();
        demo.add("test1");
        demo.add("test2");

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ListViewAdapter(demo));
    }

    public void onClickA(View v) {
        startActivity(new Intent(this, PageActivity.class));
    }

}
