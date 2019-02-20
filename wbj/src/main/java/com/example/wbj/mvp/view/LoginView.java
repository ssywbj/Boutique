package com.example.wbj.mvp.view;

/**
 * Created by wbj on 2017/12/13.
 */
public interface LoginView {
    /**
     * 用户名为空
     */
    void emptyName();

    /**
     * 密码为空
     */
    void emptyPwd();

    /**
     * 登录成功
     */
    void loginSuccess();

    /**
     * 登录失败
     *
     * @param reason 原因
     * @param code   错误码
     */
    void loginFail(String reason, int code);

    /**
     * 登录中
     */
    void loginProgress();
}
