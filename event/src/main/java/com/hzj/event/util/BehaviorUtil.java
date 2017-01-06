package com.hzj.event.util;

import android.util.Log;

/**
 * Created by hzj on 2016/8/31.
 */
public class BehaviorUtil {

    private static final String TAG = "BehaviorUtil";

    /**
     * 是否toast自动埋点的事件
     */
    private static boolean isToast;

    /**
     * 是否自动埋点
     */
    private static boolean isAutoCollectEvent;

    /**
     * 是否显示自动统计事件名称
     *
     * @return
     */
    public static boolean isToastAutoCollectEvent() {
        return isToast;
    }

    /**
     * 设置是否显示自动统计事件
     *
     * @param toast
     */
    public static void setToastAutoCollectEvent(boolean toast) {
        isToast = toast;
    }

    /**
     * 计次事件统计
     *
     * @param functionName 触发本次动作的功能点名，不能为空
     */
    public static void clickEvent(String functionName) {
        Log.d(TAG, "click event :" + functionName);
    }


    /**
     * 是否自动埋点
     */
    public static boolean isAutoCollectEvent() {
        return isAutoCollectEvent;
    }

    /**
     * 设置是否自动埋点
     */
    public static void setAutoCollectEvent(boolean isAutoCollectEvent) {
        BehaviorUtil.isAutoCollectEvent = isAutoCollectEvent;
    }
}
