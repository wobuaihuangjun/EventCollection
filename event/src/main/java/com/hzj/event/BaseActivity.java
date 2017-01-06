package com.hzj.event;

import android.os.Bundle;

/**
 * 控件埋点的基类
 * <p/>
 * Created by hzj on 2016/7/10.
 */
public class BaseActivity extends AutoColleteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
