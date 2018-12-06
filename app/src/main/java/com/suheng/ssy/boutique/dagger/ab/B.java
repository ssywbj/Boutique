package com.suheng.ssy.boutique.dagger.ab;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/6.
 */
public class B {

    @Inject
    public B() {
    }

    public void eat() {
        Log.d("WBJ", "B eat eat eat");
    }
}
