# EventCollection
android控件自动化埋点

##控件点击事件埋点策略

1. 埋点采用半自动化的方式进行，埋点的界面必须继承自BaseActivity或者BaseFragment ，不含子控件的控件将会自动收集点击事件；
2. 埋点统一收集类名+控件的UI目录树。
3. 需要埋点包含子控件的Layout控件，在 .xml 布局文件中添加 android:tag 属性，否则将不会收集，此时子控件的点击事件将会被忽略。tag 的值统一以click_event开头。

例如：
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
 
    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:tag="click_event">
 
        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher" />
 
    </LinearLayout>
 
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button" />
 
</LinearLayout>
```
Button 的点击事件将会自动收集，要收集LinearLayout的点击事件，此时必须设置 tag 属性，ImageView的点击事件将不会再收集。

LinearLayout 的UI目录树为：MainActivity：DecorView[0]->LinearLayout[0]->FrameLayout[1]->LinearLayout[0]->LinearLayout[0]；

Button           的UI目录树为：MainActivity：DecorView[0]->LinearLayout[0]->FrameLayout[1]->LinearLayout[0]->Button[1]；

设备不同黑体部分的UI路径可能会有所不同
