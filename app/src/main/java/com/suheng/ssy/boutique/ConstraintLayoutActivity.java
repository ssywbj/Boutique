package com.suheng.ssy.boutique;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;

import com.suheng.ssy.boutique.dagger.DaggerOnlyInjectComponent;
import com.suheng.ssy.boutique.dagger.Person;
import com.suheng.ssy.boutique.databinding.ActivityConstraintLayoutBinding;
import com.suheng.ssy.boutique.model.LoginNavigator;
import com.suheng.ssy.boutique.model.LoginViewModel;

import javax.inject.Inject;

public class ConstraintLayoutActivity extends BasicActivity implements LoginNavigator {
    private static final String TAG = ConstraintLayoutActivity.class.getSimpleName();
    private LoginViewModel mLoginViewModel;

    //A a;
    /*@Inject
    A a;

    @Inject
    CoffeeMachine mCoffeeMachine;*/

    private View mLayoutSms, mLayoutPwd;
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

        mLayoutBinding.editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_PHONE_MAX_LENGTH)});
        if (mLoginViewModel.getLoginBySms().get()) {
            mLayoutSms = mLayoutBinding.stubSms.getViewStub().inflate();
            /*((EditText) mLayoutSms.findViewById(R.id.edit_sms_code)).setFilters(new InputFilter[]{new InputFilter
                    .LengthFilter(LoginViewModel.EDIT_SMS_CODE_MAX_LENGTH)});*/
        } else {
            mLayoutPwd = mLayoutBinding.stubPwd.getViewStub().inflate();
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
        }

        int right = mLayoutPwd.getRight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLayoutPwd, View.TRANSLATION_X, 0, -right);//往左移，出屏幕
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLayoutPwd, View.ALPHA, 1, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator1);
        animatorSet.setDuration(400);
        animatorSet.start();

        //ObjectAnimator.ofFloat(mLayoutSms, View.TRANSLATION_X, right, 0).setDuration(400).start();//往左移，进屏幕
        //ObjectAnimator.ofFloat(mLayoutSms, View.ALPHA, 0, 1).setDuration(400).start();//往左移，进屏幕

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(ObjectAnimator.ofFloat(mLayoutSms, View.TRANSLATION_X, right, 0)
                , ObjectAnimator.ofFloat(mLayoutSms, View.ALPHA, 0, 1));
        animatorSet1.setDuration(400);
        animatorSet1.start();
    }

    @Override
    public void switchPwdLogin() {
        if (mLayoutPwd == null) {
            mLayoutPwd = mLayoutBinding.stubPwd.getViewStub().inflate();
        }

        int right = mLayoutSms.getRight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLayoutSms, View.TRANSLATION_X, 0, -right);//往左移，出屏幕
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, ObjectAnimator.ofFloat(mLayoutSms, View.ALPHA, 1, 0));
        animatorSet.setDuration(400);
        animatorSet.start();

        //ObjectAnimator.ofFloat(mLayoutPwd, View.TRANSLATION_X, right, 0);//往左移，进屏幕
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(ObjectAnimator.ofFloat(mLayoutPwd, View.TRANSLATION_X, right, 0)
                , ObjectAnimator.ofFloat(mLayoutPwd, View.ALPHA, 0, 1));
        animatorSet1.setDuration(400);
        animatorSet1.start();
    }
}
