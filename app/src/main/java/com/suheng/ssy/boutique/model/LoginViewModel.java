package com.suheng.ssy.boutique.model;

import android.databinding.ObservableField;

/**
 * Created by wbj on 2018/12/4.
 */
public class LoginViewModel {

    public static final String TAG = LoginViewModel.class.getSimpleName();
    public static final int EDIT_PHONE_MAX_LENGTH = 11;
    private LoginNavigator mLoginNavigator;
    public ObservableField<String> mPhoneNumber = new ObservableField<>();
    public ObservableField<String> mSmsCode = new ObservableField<>();
    public ObservableField<String> mPwd = new ObservableField<>();

    public LoginViewModel(LoginNavigator loginNavigator) {
        mLoginNavigator = loginNavigator;
    }

    public void onClickLogin() {

    }

    public void onClickObtainSms() {

    }

    public void onClickSwitchType() {

    }

    public void onClickClearInput() {
        mPhoneNumber.set("");
    }

    public boolean isPhoneLegal() {
        return mPhoneNumber.get().matches("^[1]\\d{" + (EDIT_PHONE_MAX_LENGTH - 1) + "}$");
    }

}
