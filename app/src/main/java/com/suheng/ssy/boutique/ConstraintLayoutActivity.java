package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;

import com.suheng.ssy.boutique.dagger.ab.A;
import com.suheng.ssy.boutique.dagger.coffee.CoffeeMachine;
import com.suheng.ssy.boutique.dagger.coffee.DaggerSimpleComponent;
import com.suheng.ssy.boutique.databinding.ActivityConstraintLayoutBinding;
import com.suheng.ssy.boutique.model.LoginNavigator;
import com.suheng.ssy.boutique.model.LoginViewModel;

import javax.inject.Inject;

public class ConstraintLayoutActivity extends BasicActivity implements LoginNavigator {

    private static final String TAG = ConstraintLayoutActivity.class.getSimpleName();
    private LoginViewModel mLoginViewModel;

    //A a;
    @Inject
    A a;

    @Inject
    CoffeeMachine mCoffeeMachine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConstraintLayoutBinding layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_constraint_layout);
        mLoginViewModel = new LoginViewModel(this);
        layoutBinding.setLoginViewModel(mLoginViewModel);

        layoutBinding.editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_PHONE_MAX_LENGTH)});
        layoutBinding.editSmsCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_SMS_CODE_MAX_LENGTH)});

        DaggerSimpleComponent.create().inject(this);
        Log.d(TAG, "CoffeeMachine makeCoffee: " + mCoffeeMachine.makeCoffee());

        //a = new A();//A的构造方法改变了，此处要修改（第二处要修改）
        Log.d(TAG, "aaaa: " + a.eat());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewModel.cancelCountdown();
    }
}
