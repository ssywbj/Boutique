package com.example.wbj.login;

import com.example.aaron.library.MLog;
import com.example.wbj.mvp.presenter.LoginPresenter;
import com.example.wbj.mvp.presenter.LoginPresenterImpl;
import com.example.wbj.mvp.view.LoginView;

/**
 * Created by wbj on 2018/1/19.
 */
public class LoginAty implements LoginView {
    private LoginPresenter mLoginPresenter;

    public LoginAty() {
        mLoginPresenter = new LoginPresenterImpl(this);
    }

    public void validate(String name, String pwd) {
        //MLog.d("thread-->" + Thread.currentThread().getName());
        mLoginPresenter.validate(name, pwd);
    }

    @Override
    public void emptyName() {
        MLog.d("用户名为空");
    }

    @Override
    public void emptyPwd() {
        MLog.d("密码为空");
    }

    @Override
    public void loginSuccess() {
        MLog.d("登录成功");
    }

    @Override
    public void loginFail(String reason, int code) {
        MLog.d("登录失败，reason: " + reason + ", code = " + code);
    }

    @Override
    public void loginProgress() {
        MLog.d("登录中。。。");
    }
}
