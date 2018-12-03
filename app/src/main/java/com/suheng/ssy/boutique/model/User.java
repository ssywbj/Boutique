package com.suheng.ssy.boutique.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class User {

    private ObservableField<String> name;//不是声明public修饰符的，一定要设置setter和getter方法，不然在XML会找不到该属性
    public ObservableInt age;

    public User(String name, int age) {
        this.name = new ObservableField<>(name);
        this.age = new ObservableInt(age);
    }

    public ObservableField<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

}
