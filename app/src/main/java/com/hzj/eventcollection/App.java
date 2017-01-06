package com.hzj.eventcollection;

import android.app.Application;

import com.hzj.event.util.BehaviorUtil;

/**
 * Created by hzj on 2016/8/31.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BehaviorUtil.setToastAutoCollectEvent(true);
        BehaviorUtil.setAutoCollectEvent(true);
    }
}
