package com.suheng.ssy.boutique;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suheng.ssy.boutique.dagger.DaggerOnlyInjectComponent;
import com.suheng.ssy.boutique.dagger.Person;
import com.suheng.ssy.boutique.databinding.ActivityConstraintLayoutBinding;
import com.suheng.ssy.boutique.model.LoginNavigator;
import com.suheng.ssy.boutique.model.LoginViewModel;

import javax.inject.Inject;

public class ConstraintLayoutActivity extends BasicActivity implements LoginNavigator {
    private static final String TAG = ConstraintLayoutActivity.class.getSimpleName();
    private static final int ANIM_DURATION = 400;
    private LoginViewModel mLoginViewModel;

    //A a;
    /*@Inject
    A a;

    @Inject
    CoffeeMachine mCoffeeMachine;*/

    private View mLayoutSms, mLayoutPwd;
    private TextView mEditSmsCode, mTextObtainSms;
    private TextView mEditPwd;
    private View mCheckBoxPwd;
    @Inject
    Person mPerson;
    ActivityConstraintLayoutBinding mLayoutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_constraint_layout);
        mLoginViewModel = new LoginViewModel(this);
        mLayoutBinding.setLoginViewModel(mLoginViewModel);
        //layoutBinding.setVariable(BR.loginViewModel, mLoginViewModel);//或这样绑定,setVariable为父类（ViewDataBinding）的方法
        mLayoutBinding.editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.PHONE_MAX_LENGTH)});
        if (mLoginViewModel.getLoginBySms().get()) {
            mLayoutSms = mLayoutBinding.stubSms.getViewStub().inflate();
            mEditSmsCode = mLayoutSms.findViewById(R.id.edit_sms);
            mEditSmsCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.SMS_CODE_MAX_LENGTH)});
            mTextObtainSms = mLayoutSms.findViewById(R.id.text_obtain_sms);
        } else {
            mLayoutPwd = mLayoutBinding.stubPwd.getViewStub().inflate();
            mEditPwd = mLayoutPwd.findViewById(R.id.edit_pwd);
            mEditPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.PWD_MAX_LENGTH)});
            mCheckBoxPwd = mLayoutPwd.findViewById(R.id.btn_eyes);
        }

        //DaggerSimpleComponent.create().inject(this);
        //Log.d(TAG, "CoffeeMachine makeCoffee: " + mCoffeeMachine.makeCoffee());

        //a = new A();//A的构造方法改变了，此处要修改（第二处要修改）
        //Log.d(TAG, "aaaa: " + a.eat());
        DaggerOnlyInjectComponent.builder().build().inject(this);
        Log.d(TAG, "mPerson.getName(): " + mPerson.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewModel.cancelCountdown();
    }

    @Override
    public void switchSmsLogin() {
        if (mLayoutSms == null) {
            mLayoutSms = mLayoutBinding.stubSms.getViewStub().inflate();
            mEditSmsCode = mLayoutSms.findViewById(R.id.edit_sms);
            mEditSmsCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.SMS_CODE_MAX_LENGTH)});
            mTextObtainSms = mLayoutSms.findViewById(R.id.text_obtain_sms);
        }

        final int translationX = mLayoutPwd.getRight();
        ObjectAnimator translationOut = ObjectAnimator.ofFloat(mLayoutPwd, View.TRANSLATION_X, 0, -translationX);//往左平移，出屏幕
        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(mLayoutPwd, View.ALPHA, 1, 0);//透明度：全不透明到全透明
        AnimatorSet animatorOut = new AnimatorSet();
        animatorOut.playTogether(translationOut, alphaOut);
        animatorOut.setDuration(ANIM_DURATION);
        animatorOut.start();

        ObjectAnimator translationIn = ObjectAnimator.ofFloat(mLayoutSms, View.TRANSLATION_X, translationX, 0);//往左平移，进屏幕
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(mLayoutSms, View.ALPHA, 0, 1);//透明度：全透明到全不透明
        AnimatorSet animatorIn = new AnimatorSet();
        animatorIn.playTogether(translationIn, alphaIn);
        animatorIn.setDuration(ANIM_DURATION);
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLayoutBinding.btnSwitchLoginType.setEnabled(false);

                mEditPwd.setText("");

                mLayoutSms.setVisibility(View.VISIBLE);
                mEditSmsCode.setVisibility(View.VISIBLE);
                mTextObtainSms.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLayoutBinding.btnSwitchLoginType.setEnabled(true);
                mLoginViewModel.setLoginBySms(true);

                if (mEditPwd.hasFocus()) {
                    mEditSmsCode.requestFocus();
                }
                mLayoutPwd.setVisibility(View.GONE);
                mEditPwd.setVisibility(View.GONE);
                mCheckBoxPwd.setVisibility(View.GONE);
            }
        });
        animatorIn.start();
    }

    @Override
    public void switchPwdLogin() {
        if (mLayoutPwd == null) {
            mLayoutPwd = mLayoutBinding.stubPwd.getViewStub().inflate();
            mEditPwd = mLayoutPwd.findViewById(R.id.edit_pwd);
            mEditPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.PWD_MAX_LENGTH)});
            mCheckBoxPwd = mLayoutPwd.findViewById(R.id.btn_eyes);
        }

        //https://blog.csdn.net/u013872857/article/details/53750682，Android View坐标系详解,https://my.oschina.net/zhuyichuan/blog/1527546
        final int translationX = mLayoutSms.getRight();//view自身的右边到其父布局左边的距离
        AnimatorSet animatorOut = new AnimatorSet();
        animatorOut.playTogether(ObjectAnimator.ofFloat(mLayoutSms, View.TRANSLATION_X, 0, -translationX)
                , ObjectAnimator.ofFloat(mLayoutSms, View.ALPHA, 1, 0));//左移出屏幕，透明度1-0
        animatorOut.setDuration(ANIM_DURATION);
        animatorOut.start();

        AnimatorSet animatorIn = new AnimatorSet();
        animatorIn.playTogether(ObjectAnimator.ofFloat(mLayoutPwd, View.TRANSLATION_X, translationX, 0)
                , ObjectAnimator.ofFloat(mLayoutPwd, View.ALPHA, 0, 1));//左移进屏幕，透明度0-1
        animatorIn.setDuration(ANIM_DURATION);
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLayoutBinding.btnSwitchLoginType.setEnabled(false);

                mEditSmsCode.setText("");

                mLayoutPwd.setVisibility(View.VISIBLE);
                mEditPwd.setVisibility(View.VISIBLE);
                mCheckBoxPwd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLayoutBinding.btnSwitchLoginType.setEnabled(true);
                mLoginViewModel.setLoginBySms(false);

                if (mEditSmsCode.hasFocus()) {
                    mEditPwd.requestFocus();
                }
                mLayoutSms.setVisibility(View.GONE);
                mEditSmsCode.setVisibility(View.GONE);
                mTextObtainSms.setVisibility(View.GONE);
            }
        });
        animatorIn.start();
    }
}
