package com.hzj.event;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.hzj.event.util.BehaviorUtil;
import com.hzj.event.util.MD5Util;


/**
 * 大数据埋点基类，所有 Activity 都必须是此类的子类
 * <p>
 * Created by hzj on 2017/1/4.
 */

public class AutoColleteActivity extends FragmentActivity {

    private static final String TAG = "AutoColleteActivity";

    /**
     * 按下时点中的控件
     */
    private MyView downView;

    private View rootView;
    private String rootViewTree;// 控件在视图树上的根路径

    private String bigDataPrefix;
    private String bigDataIngorePrefix;
    private String bigDataEventPrefix;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        rootView = getWindow().getDecorView();
        rootViewTree = getPackageName() + "." + getClass().getSimpleName();
        bigDataPrefix = getString(R.string.collection_tag);
        bigDataIngorePrefix = getString(R.string.collection_ignore_tag);
        bigDataEventPrefix = getString(R.string.collection_event_prefix);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (BehaviorUtil.isAutoCollectEvent()) {
            dealAutoCollect(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    private void dealAutoCollect(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (downView != null) {
                MyView myView = findClickView(ev);

                // 自动统计所有控件的点击事件
                if (myView != null && downView.view == myView.view) {
                    String funcName;
                    int id = myView.view.getId();

                    if (!TextUtils.isEmpty(myView.specifyTag)) {
                        funcName = myView.specifyTag;
                    } else if (id != -1) {
                        funcName = MD5Util.md5(rootViewTree + id);
                    } else {
                        funcName = MD5Util.md5(myView.viewTree);
                    }

                    BehaviorUtil.clickEvent(funcName);
                    if (BehaviorUtil.isToastAutoCollectEvent()) {
                        Toast.makeText(this, funcName, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "bigdata-->funcName = " + funcName + ", viewTree = " + myView.viewTree);

                        Log.i(TAG, "bigdata-->id = " + myView.view.getId());
                    }
                }
                downView = null;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downView = findClickView(ev);
        }
    }

    /**
     * 查找点中的视图
     */
    private MyView findClickView(MotionEvent ev) {
        Log.w(TAG, "bigdata-->findClickView");
        MyView myView = new MyView(rootView, rootViewTree);
        return searchClickView(myView, ev, 0);
    }

    /**
     * 查找点击的View
     *
     * @param myView 顶层布局view
     * @param event  事件
     * @return 点击的view
     */
    private MyView searchClickView(MyView myView, MotionEvent event, int index) {
        MyView clickView = null;
        View view = myView.view;
        if (isInView(view, event)) {
            // 当第二层不为LinearLayout时，说明系统进行了改造，多了一层,需要多剔除一层
            myView.level++;
            if (myView.level == 2 && !"LinearLayout".equals(view.getClass().getSimpleName())) {
                myView.filterLevelCount++;
            }
            if (myView.level > myView.filterLevelCount) {
                myView.viewTree = myView.viewTree + "." + view.getClass().getSimpleName() + "[" + index + "]";
            }
            Log.i(TAG, "bigdata-->tag = " + view.getTag());
            // 如果Layout有设置特定的tag，则直接返回View，主要用于复合组件的点击事件
            if (view.getTag() != null) {
                // 主动标记不需要统计时，不进行自动统计
                String tag = view.getTag().toString();

                if (tag.startsWith(bigDataIngorePrefix)) {
                    return null;
                } else if (tag.startsWith(bigDataPrefix)) {
                    if (tag.startsWith(bigDataEventPrefix)) {
                        myView.specifyTag = tag.replace(bigDataEventPrefix, "");
                    }
                    return myView;
                }
            }

            if (view instanceof ViewGroup) {    //遇到一些Layout之类的ViewGroup，继续遍历它下面的子View
                if (view instanceof AbsListView) {
                    Log.i(TAG, "bigdata-->AbsListView ");
                    return null;
                }

                ViewGroup group = (ViewGroup) view;
                int childCount = group.getChildCount();

                if (childCount == 0) {
                    return myView;
                }
                for (int i = childCount - 1; i >= 0; i--) {
                    myView.view = group.getChildAt(i);
                    clickView = searchClickView(myView, event, i);
                    if (clickView != null) {
                        return clickView;
                    }
                }
            } else {
                clickView = myView;
            }
        }
        return clickView;
    }

    private boolean isInView(View view, MotionEvent event) {
        if (view == null || view.getVisibility() != View.VISIBLE) {
            return false;
        }
        int clickX = (int) event.getRawX();
        int clickY = (int) event.getRawY();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int width = view.getWidth();
        int height = view.getHeight();
        return clickX > x && clickX < (x + width) && clickY > y && clickY < (y + height);
    }

    private static class MyView {

        View view;
        String viewTree;
        String specifyTag;
        int level = 0;
        int filterLevelCount = 3;

        MyView(View view, String viewTree) {
            this.view = view;
            this.viewTree = viewTree;
        }
    }

}
