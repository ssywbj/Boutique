package com.suheng.ssy.boutique;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable {

    private Navigor mNavigor;
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setNavigor(Navigor navigor) {
        this.mNavigor = navigor;
    }

    //使用notifyPropertyChanged方法刷新界面步骤1：private修饰符，需要在成员变量的get方法上添加@Bindable注解；
    // 而如果是public修饰符，则可以直接在成员变量上方加上@Bindable注解
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //使用notifyPropertyChanged方法刷新界面步骤2：只更新对应BR的flag；只有声明了@Bindable注解或在XML中定
        // 义如TmpUser，user才能被写BR常量里，现在因为age没有被@Bindable注解，所以BR引用不到age
        notifyPropertyChanged(BR.name);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        this.setName("" + age * age);
        notifyChange(); //刷新所有的值域，不需要“@Bindable”注解，只要值有改动，就刷新对应的界面
    }

    public void onClickName(String text) {
        this.setName(text);
        if (mNavigor != null) {
            mNavigor.tip(name);
        }
    }

}
