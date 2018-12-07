package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;

import com.suheng.ssy.boutique.dagger.Consumer;
import com.suheng.ssy.boutique.dagger.DataModule;
import com.suheng.ssy.boutique.dagger.PriorityTestEntity;
import com.suheng.ssy.boutique.dagger.coffee.ConsumerQualifier;
import com.suheng.ssy.boutique.dagger.coffee.DaggerConsumerComponent;
import com.suheng.ssy.boutique.databinding.ActivityConstraintLayoutBinding;
import com.suheng.ssy.boutique.model.LoginNavigator;
import com.suheng.ssy.boutique.model.LoginViewModel;

import javax.inject.Inject;
import javax.inject.Named;

public class ConstraintLayoutActivity extends BasicActivity implements LoginNavigator {

    private static final String TAG = ConstraintLayoutActivity.class.getSimpleName();
    private LoginViewModel mLoginViewModel;

    //A a;
    /*@Inject
    A a;

    @Inject
    CoffeeMachine mCoffeeMachine;*/

    @Inject
    PriorityTestEntity mPriorityTestEntity;
    @Inject
    Consumer mConsumer;//默认对象
    //如果需要特定的对象，用@Qualifier标识符注解，@Named是自定义的一个标识符注解
    @Inject
    @Named(DataModule.MALE)
    Consumer mConsumerMale;
    @Inject
    @Named(DataModule.FEMALE)
    Consumer mConsumerFemale;
    @Inject
    @ConsumerQualifier
    Consumer mConsumerQualifier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConstraintLayoutBinding layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_constraint_layout);
        mLoginViewModel = new LoginViewModel(this);
        layoutBinding.setLoginViewModel(mLoginViewModel);

        layoutBinding.editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_PHONE_MAX_LENGTH)});
        layoutBinding.editSmsCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_SMS_CODE_MAX_LENGTH)});

        //DaggerSimpleComponent.create().inject(this);
        //Log.d(TAG, "CoffeeMachine makeCoffee: " + mCoffeeMachine.makeCoffee());

        //a = new A();//A的构造方法改变了，此处要修改（第二处要修改）
        //Log.d(TAG, "aaaa: " + a.eat());

        DaggerConsumerComponent.create().inject(this);
        Log.d(TAG, "mConsumer.getSex(): " + mConsumer.getSex() + ", mPriorityTestEntity.getName(): " + mPriorityTestEntity.getName());
        Log.d(TAG, mConsumerMale.getSex() + "；" + mConsumerFemale.getSex() + "；" + mConsumerQualifier.getSex());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewModel.cancelCountdown();
    }
}
