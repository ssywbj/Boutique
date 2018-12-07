package com.suheng.ssy.boutique.dagger;

/**
 * Created by wbj on 2018/12/7.
 */

public class Consumer {

    private String sex;

    public Consumer() {
        sex = "太监";
    }

    public Consumer(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

}
