package com.example.wbj.mvp.presenter;

import com.example.wbj.mvp.model.LoginModel;
import com.example.wbj.mvp.model.LoginModelImpl;
import com.example.wbj.mvp.model.OnLoginRequestListener;
import com.example.wbj.mvp.model.UserInfo;
import com.example.wbj.mvp.view.LoginView;

/**
 * Created by wbj on 2017/12/13.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginRequestListener {
    private LoginView mLoginView;
    private LoginModel mLoginModel;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
        mLoginModel = new LoginModelImpl();
    }

    @Override
    public void validate(String name, String pwd) {
        if (name.isEmpty()) {
            mLoginView.emptyName();
            return;
        }
        if (pwd.isEmpty()) {
            mLoginView.emptyPwd();
            return;
        }

        mLoginView.loginProgress();//一系列验证过后，再调用登录接口
        mLoginModel.login(name, pwd, this);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        mLoginView.loginSuccess();
    }

    @Override
    public void onLoginFail(int code, String msg) {
        mLoginView.loginFail(msg, code);
    }

}
