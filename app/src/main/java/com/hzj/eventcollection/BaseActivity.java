package com.hzj.eventcollection;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * 控件埋点的基类
 * <p/>
 * Created by hzj on 2016/7/10.
 */
public class BaseActivity extends FragmentActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //1.递归遍历Activity（就是Context）中的所有View，找出被点击的View
            View rootView = this.getWindow().getDecorView();
            String viewTree = this.getClass().getSimpleName() + "：";
            MyView myView = new MyView(rootView, viewTree);
            myView = searchClickView(myView, ev, 0);
            //2.生成log记录下来
            writeLog(myView);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @param myView 点击的控件
     */
    private void writeLog(MyView myView) {
        if (myView == null) {
            return;
        }
        View clickView = myView.view;

        Object tag = clickView.getTag();
        Log.i(TAG, myView.viewTree);

        if (tag != null) {
            Log.w(TAG, "tag：" + tag.toString());
        }

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
        if (view != null && isInView(view, event) &&
                view.getVisibility() == View.VISIBLE) {  //这里一定要判断View是可见的
            myView.viewTree = myView.viewTree + "->" + view.getClass().getSimpleName() + "[" + index + "]";
            if (view.getTag() != null) { // 如果Layout有设置特定的tag，则直接返回View，主要用于复合组件的点击事件
                return myView;
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
            }
            clickView = myView;
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

    public class MyView {

        View view;
        String viewTree;

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
