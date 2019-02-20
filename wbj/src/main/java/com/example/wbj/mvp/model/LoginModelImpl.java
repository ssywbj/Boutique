package com.example.wbj.mvp.model;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by wbj on 2017/12/13.
 */
public class LoginModelImpl implements LoginModel {

    @Override
    public void login(final String name, final String pwd, final OnLoginRequestListener onLoginRequestListener) {
        new Handler().postDelayed(new Runnable() {//模拟网络请求
            @Override
            public void run() {

                if (!"Ssywbj".equals(name)) {
                    onLoginRequestListener.onLoginFail(-1, "账号不存在");
                    return;
                }
                if (!"123456".equals(pwd)) {
                    onLoginRequestListener.onLoginFail(-2, "密码不正确");
                    return;
                }

                if ("Ssywbj".equals(name) && "123456".equals(pwd)) {
                    onLoginRequestListener.onLoginSuccess(new UserInfo());
                }

            }
        }, 2000);
    }

}
