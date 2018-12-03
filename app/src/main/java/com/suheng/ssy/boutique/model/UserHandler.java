package com.suheng.ssy.boutique.model;

import android.text.Editable;
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

    public void afterTextChanged(Editable s) {
        mUser.setName(s.toString());
        Log.d(TAG, "afterTextChanged = " + s.toString());
    }

    public void onClickSetNameNull() {
        mUser.setName(null);//避免空指针异常：name为null的话，tmpUser.name会被赋值为默认值null，而不会抛出空指针异常
    }
}