package com.example.wbj.mvp.model;

/**
 * Created by wbj on 2017/12/13.
 */
public interface OnLoginRequestListener {
    void onLoginSuccess(UserInfo userInfo);

    void onLoginFail(int code, String msg);
}
