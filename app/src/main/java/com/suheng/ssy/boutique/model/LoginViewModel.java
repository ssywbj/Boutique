package com.suheng.ssy.boutique.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.util.Log;

/**
 * Created by wbj on 2018/12/4.
 */
public class LoginViewModel {

    public static final String TAG = LoginViewModel.class.getSimpleName();
    public static final int EDIT_PHONE_MAX_LENGTH = 11;
    public static final int EDIT_SMS_CODE_MAX_LENGTH = 6;
    private LoginNavigator mLoginNavigator;
    //赋值为new ObservableField<>("")时，mPhoneNumber.get()的默认值为""；而赋值为new ObservableField<>()时，mPhoneNumber.get()默认值为null，稍有不注意引用到就会报空指针异常。
    public ObservableField<String> mPhoneNumber = new ObservableField<>("");
    public ObservableField<String> mSmsCode = new ObservableField<>("");
    public ObservableField<String> mPwd = new ObservableField<>();
    public ObservableBoolean mIsBtnClearEnabled = new ObservableBoolean();
    public ObservableBoolean mIsBtnLoginEnabled = new ObservableBoolean();


    public LoginViewModel(LoginNavigator loginNavigator) {
        mLoginNavigator = loginNavigator;
    }

    public void onClickLogin(String phone, String smsCode) {
        Log.d(TAG, "phone = " + phone + ", smsCode = " + smsCode);
    }

    public void onClickObtainSms() {

    }

    public void onClickSwitchType() {

    }

    public void onClickClearInput() {
        mPhoneNumber.set("");
        mIsBtnClearEnabled.set(false);
    }


    public void afterTextChanged(Editable s) {
        mIsBtnClearEnabled.set(this.isPhoneLegal());
        mIsBtnLoginEnabled.set(this.isPhoneLegal() && this.isSmsCodeLegal());
    }

    public boolean isPhoneLegal() {
        return mPhoneNumber.get().matches("^[1]\\d{" + (EDIT_PHONE_MAX_LENGTH - 1) + "}$");
    }

    public boolean isSmsCodeLegal() {
        return mSmsCode.get().matches("^\\d{" + (EDIT_SMS_CODE_MAX_LENGTH) + "}$");
    }
}
