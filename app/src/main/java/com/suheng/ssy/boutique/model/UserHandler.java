package com.suheng.ssy.boutique.model;

import android.util.Log;

public class UserHandler {

    private static final String TAG = UserHandler.class.getSimpleName();
    private User mUser;

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

}