package com.example.wbj.mvp.model;

/**
 * Created by wbj on 2017/12/13.
 */
public interface LoginModel {
    void login(String name, String pwd, OnLoginRequestListener onLoginRequestListener);
}
