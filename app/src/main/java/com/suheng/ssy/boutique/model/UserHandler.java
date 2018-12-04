package com.suheng.ssy.boutique.model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.Log;
import android.widget.ImageView;

public class UserHandler {

    private static final String TAG = UserHandler.class.getSimpleName();
    private User mUser;
    public ObservableField<String> mUrl = new ObservableField<>();

    public UserHandler(User user) {
        mUser = user;
    }

    public void onClickName(String name) {
        if (mUser == null) {
            return;
        }
        mUser.setName(name);
    }

    public void onClickAge(int age) {
        if (mUser == null) {
            return;
        }
        mUser.age.set(age);
    }

    public void onClickGetEdit(String name, int age) {
        Log.d(TAG, "name value = " + name + ", age = " + age);
    }

    public void afterTextChanged(Editable s) {
        mUser.setName(s.toString());
        Log.d(TAG, "afterTextChanged = " + s.toString());
    }

    public void onClickSetSchool(String school, int id) {
        //避免空指针异常：当值为null的时候，会被赋值为默认值null，而不会抛出空指针异常。注意：null是指对象为空，而不是空字符串
        mUser.school.set(school);
        Log.d(TAG, "id = " + id);
    }

    //@BindingAdapter(value = {"imageUrl", "loadError"}, requireAll = false)//imageUrl或loadError缺少的情况下，依然可以调用适配器
    @BindingAdapter({"imageUrl", "loadError"})
    //imageUrl和loadError都用于ImageView对象并且imageUrl是字符串loadError是Drawable时，才会调用适配器
    public static void loadImage(ImageView imageView, String imageUrl, Drawable loadError) {//根据Url加载图片（要申明为static方法）
        Log.d(TAG, "imageView = " + imageView + ", imageUrl = " + imageUrl);
        imageView.setImageDrawable(loadError);
    }

    /*@BindingAdapter("android:text")//可以覆盖Android原先的控件属性！覆盖后，整个工程中使用到 "android:text" 这个属性的控件
    // ，其显示的文本就会多出一个后缀“-Button”。
    public static void setText(TextView view, String text) {
        view.setText(text + "-Button");
    }

    @BindingConversion//BindingConversion可以对数据进行转换或者进行类型转换
    public static String conversionString(String text) {
        return text + "-conversionString";
        //整个工程中的布局文件中以@{String}方式引用到的String类型变量都会加上后缀-conversionString，BindingAdapter和
        // BindingConversion会同时生效，而BindingConversion的优先级要高些（xxx-conversionString-Button）
    }*/

}