package com.hzj.event;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hzj.event.util.BehaviorUtil;
import com.hzj.event.util.MD5Util;

/**
 * 控件埋点的基类
 * <p/>
 * Created by hzj on 2016/7/10.
 */
public class BaseActivity extends FragmentActivity {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * 按下时点中的控件
     */
    private MyView downView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (downView != null) {
                MyView myView = findClickView(ev);

                // 自动统计所有控件的点击事件
                if (myView != null && downView.view == myView.view) {
                    String funcName = MD5Util.md5(myView.viewTree);
                    BehaviorUtil.clickEvent(funcName);
                    if (BehaviorUtil.isToastAutoCollectEvent()) {
                        Toast.makeText(this, funcName, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "bigdata-->funcName = " + funcName + ", viewTree = " + myView.viewTree);
                    }
                }
                downView = null;
            }

        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downView = findClickView(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 查找点中的视图
     *
     * @param ev
     * @return
     */
    private MyView findClickView(MotionEvent ev) {
        View rootView = this.getWindow().getDecorView();
        // 控件在视图树上的路径,暂时不使用
        String viewTree = this.getPackageName() + "." + this.getClass().getSimpleName();
        MyView myView = new MyView(rootView, viewTree);
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
        if (view != null && isInView(view, event) && view.getVisibility() == View.VISIBLE) {
            // 当第二层不为LinearLayout时，说明系统进行了改造，多了一层,需要多剔除一层
            myView.level++;
            if (myView.level == 2 && !"LinearLayout".equals(view.getClass().getSimpleName())) {
                myView.filterLevelCount++;
            }
            if (myView.level > myView.filterLevelCount) {
                myView.viewTree = myView.viewTree + "." + view.getClass().getSimpleName() + "[" + index + "]";
            }

            // 如果Layout有设置特定的tag，则直接返回View，主要用于复合组件的点击事件
            if (view.getTag() != null) {
                // 主动标记不需要统计时，不进行自动统计
                String tag = view.getTag().toString();
                if (tag.startsWith("bigdata_") && !"bigdata_ignore".equals(tag)) {
                    return myView;
                } else {
                    return null;
                }
            }

            if (view instanceof ViewGroup) {    //遇到一些Layout之类的ViewGroup，继续遍历它下面的子View
                ViewGroup group = (ViewGroup) view;
                int childCount = group.getChildCount();
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
        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
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

        public View view;
        public String viewTree;
        public int level = 0;
        public int filterLevelCount = 3;

        public MyView(View view, String viewTree) {
            this.view = view;
            this.viewTree = viewTree;
        }
    }

//    private View.OnClickListener getOnClickListener(View view) {
//        if (view == null) {
//            return null;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            return getOnClickListenerV14(view);
//        } else {
//            return getOnClickListenerV(view);
//        }
//    }

    //Used for APIs lower than ICS (API 14)
//    private View.OnClickListener getOnClickListenerV(View view) {
//        View.OnClickListener retrievedListener = null;
//        String viewStr = "android.view.View";
//        Field field;
//
//        try {
//            field = Class.forName(viewStr).getDeclaredField("mOnClickListener");
//            retrievedListener = (View.OnClickListener) field.get(view);
//        } catch (NoSuchFieldException ex) {
//            Log.e("Reflection", "No Such Field.");
//        } catch (IllegalAccessException ex) {
//            Log.e("Reflection", "Illegal Access.");
//        } catch (ClassNotFoundException ex) {
//            Log.e("Reflection", "Class Not Found.");
//        }
//
//        return retrievedListener;
//    }

    //Used for new ListenerInfo class structure used beginning with API 14 (ICS)
//    private View.OnClickListener getOnClickListenerV14(View view) {
//        View.OnClickListener retrievedListener = null;
//        String viewStr = "android.view.View";
//        String lInfoStr = "android.view.View$ListenerInfo";
//
//        try {
//            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
//            Object listenerInfo = null;
//
//            if (listenerField != null) {
//                listenerField.setAccessible(true);
//                listenerInfo = listenerField.get(view);
//            }
//
//            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");
//
//            if (clickListenerField != null && listenerInfo != null) {
//                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
//            }
//        } catch (NoSuchFieldException ex) {
//            Log.e("Reflection", "No Such Field.");
//        } catch (IllegalAccessException ex) {
//            Log.e("Reflection", "Illegal Access.");
//        } catch (ClassNotFoundException ex) {
//            Log.e("Reflection", "Class Not Found.");
//        }
//
//        return retrievedListener;
//    }
}
