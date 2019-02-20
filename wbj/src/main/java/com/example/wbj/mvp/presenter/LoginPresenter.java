package com.example.wbj.mvp.presenter;

/**
 * Created by wbj on 2017/12/13.
 */
public interface LoginPresenter {
    void validate(String name, String pwd);

    void destroy();
}
