package com.suheng.ssy.boutique.model;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;

import com.suheng.ssy.boutique.R;

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
    private ObservableField<String> mPwd = new ObservableField<>("");
    public ObservableField<String> mObtainSmsCountdown = new ObservableField<>("");
    public ObservableBoolean mBtnObtainSmsEnabled = new ObservableBoolean();
    private ObservableBoolean mBtnLoginEnabled = new ObservableBoolean();
    private ObservableBoolean mLoginBySms = new ObservableBoolean(true);
    private CountDownTimer mCountDownTimer;
    private boolean mIsCountdownStatus;

    public LoginViewModel(LoginNavigator loginNavigator) {
        mLoginNavigator = loginNavigator;
    }

    public void onClickLogin() {
        if (mLoginBySms.get()) {
            this.loginBySms();
        } else {
            this.loginByPwd();
        }
    }

    /*在绑定表达式中会根据需要生成一个名为context的特殊变量，context的值是根据View的getContext()方法返回的Context对象，
    context变量会被具有该名称的显式变量声明所覆盖*/
    public void onClickObtainSms(final Context context) {
        mBtnObtainSmsEnabled.set(false);//倒计时过程中，获取短信按钮不可用
        mIsCountdownStatus = true;

        /*倒计时类CountDownTimer有误差而且随着倒计时的增加，误差有增大的迹象（具体可看打印日志），
        加120是为了减少误差（先用着，后续肯定要改，真恶心）*/
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(30 * 1000 + 120, 1000) {
                @Override
                public void onTick(long l) {
                    //Log.d(TAG, "onTick, l = " + l + ", " + (l / 1000) + ", " + Thread.currentThread().getName());
                    mObtainSmsCountdown.set(context.getString(R.string.obtain_sms_countdown, (l / 1000)));
                }

                @Override
                public void onFinish() {
                    //Log.d(TAG, "onFinish, CountDownTimer" + ", " + Thread.currentThread().getName());
                    mObtainSmsCountdown.set(context.getString(R.string.obtain_sms_retry));
                    mBtnObtainSmsEnabled.set(isPhoneLegal());
                    mIsCountdownStatus = false;
                }
            };
        } else {
            mCountDownTimer.cancel();
        }
        mCountDownTimer.start();
    }

    public void onClickSwitchType() {
        if (mLoginBySms.get()) {
            mLoginNavigator.switchPwdLogin();
        } else {
            mLoginNavigator.switchSmsLogin();
        }
        mLoginBySms.set(!mLoginBySms.get());
    }

    public void onClickClearInput() {
        mPhoneNumber.set("");
    }

    public void onClickEyes() {
        mPhoneNumber.set("");
    }

    public void afterTextChanged(Editable s) {
        mBtnObtainSmsEnabled.set(!mIsCountdownStatus && this.isPhoneLegal());// 如果处于倒计时中，那么获取短信按钮肯定是处于不可用状态
        mBtnLoginEnabled.set(this.isPhoneLegal() && this.isSmsCodeLegal());
    }

    private boolean isPhoneLegal() {
        return mPhoneNumber.get().matches("^[1][3456789]\\d{" + (EDIT_PHONE_MAX_LENGTH - 2) + "}$");
    }

    private boolean isSmsCodeLegal() {
        return mSmsCode.get().matches("^\\d{" + (EDIT_SMS_CODE_MAX_LENGTH) + "}$");
    }

    private void loginBySms() {
        Log.d(TAG, "sms login, phone = " + mPhoneNumber.get() + ", smsCode = " + mSmsCode.get());
    }

    private void loginByPwd() {
        Log.d(TAG, "pwd login, phone = " + mPhoneNumber.get() + ", pwd = " + mPwd.get());
    }

    public void cancelCountdown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    //-----------------------------start: setter and getter-------------------------------------
    public ObservableField<String> getPwd() {
        return mPwd;
    }

    public ObservableBoolean getBtnLoginEnabled() {
        return mBtnLoginEnabled;
    }

    public ObservableBoolean getLoginBySms() {
        return mLoginBySms;
    }
    //-----------------------------end: setter and getter-------------------------------------
}
