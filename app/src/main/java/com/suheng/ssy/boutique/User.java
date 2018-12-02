package com.suheng.ssy.boutique;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

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

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

        if (mNavigor != null) {
            mNavigor.tip(name);
        }

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void onClickName(View view) {
        this.setName("Ssy");
    }

}
