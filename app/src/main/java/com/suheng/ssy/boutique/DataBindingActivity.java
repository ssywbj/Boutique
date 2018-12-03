package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableList;
import android.databinding.ObservableMap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.suheng.ssy.boutique.databinding.ActivityDataBindingBinding;
import com.suheng.ssy.boutique.model.UserHandler;

/**
 * 1.XML只做赋值或者简单的三元运算或者判空等不要做复杂运算；
 * 2.逻辑运算在Model中；
 * 3.有时候可以偷懒将Activity当作ViewModel来使用。
 */
public class DataBindingActivity extends BasicActivity implements Navigor {

    //AaaBbbBinding（绑定类）命名规则（系统自动生成）：根据布局文件名(aaa_bbb)生成——改为首字母大写的驼峰命名法并省略布局文件名包含的下划线。
    private ActivityDataBindingBinding mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        User user = new User("Wbj", 29);
        user.setNavigor(this);
        mDataBinding.setUser(user);
        mDataBinding.setText("Text");
        /*实现了Observable接口的类允许注册一个监听器，当可观察对象的属性更改时就会通知这个监听器，
        此时就需要用到OnPropertyChangedCallback，其中propertyId就用于标识特定的字段（当可观察对象：只有声明了@Bindable注解或在XML中定义
        如TmpUser，user才能被写BR常量里，现在因为age没有被@Bindable注解，所以BR引用不到age）。
        */
        user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.name) {
                    Log.i(mTag, "BR.name = " + BR.name);
                } else if (propertyId == BR._all) {
                    Log.i(mTag, "BR._all = " + BR._all);
                } else {
                    Log.i(mTag, "propertyId = " + propertyId);
                }
            }
        });

        com.suheng.ssy.boutique.model.User aliasUser = new com.suheng.ssy.boutique.model.User("Ssy", 18);
        mDataBinding.setTmpUser(aliasUser);
        mDataBinding.setUserHandler(new UserHandler(aliasUser));

        ObservableList<String> listText = new ObservableArrayList<>();
        listText.add("AAAAAAAA");
        listText.add("BBBBBBBB");
        mDataBinding.setList(listText);
        mDataBinding.setIndex(0);

        ObservableMap<String, String> mapText = new ObservableArrayMap<>();
        mapText.put("name", "韦帮杰");
        mapText.put("age", "29");
        mDataBinding.setMap(mapText);
        mDataBinding.setKey("name");
    }

    @Override
    public void tip(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        //mDataBinding.btnAge.setText("30");//获取控件的方式：获取方式与绑定类类似，但首字母小写
    }
}
