package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.suheng.ssy.boutique.dagger.Consumer;
import com.suheng.ssy.boutique.dagger.ConsumerQualifier;
import com.suheng.ssy.boutique.dagger.DaggerConsumerComponent;
import com.suheng.ssy.boutique.dagger.DataModule;
import com.suheng.ssy.boutique.dagger.PriorityTestEntity;
import com.suheng.ssy.boutique.databinding.ActivityDaggerBinding;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

public class DaggerActivity extends BasicActivity {

    private static final String TAG = DaggerActivity.class.getSimpleName();
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

    private ActivityDaggerBinding mDaggerBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDaggerBinding = DataBindingUtil.setContentView(this, R.layout.activity_dagger);
        mDaggerBinding.setAty(this);

        //mCoffeeMachine = new CoffeeMachine();
        //mCoffeeMachine = new CoffeeMachine(new Cooker("Wbj", "Suheng"));
        //mCoffeeMachine = new CoffeeMachine(new SimpleMaker(new Cooker("Wbj", "Suheng")));
        //DaggerSimpleComponent.builder().build().inject(this);
        //Log.d(TAG, "CoffeeMachine makeCoffee: " + mCoffeeMachine.makeCoffee());

        //a = new A();//A的构造方法改变了，此处要修改（第一处要修改）
        //Log.d(TAG, "aaaa: " + a.eat());

        DaggerConsumerComponent.create().inject(this);
        Log.d(TAG, "mConsumer.getSex(): " + mConsumer.getSex() + ", mPriorityTestEntity.getName(): " + mPriorityTestEntity.getName());
        Log.d(TAG, mConsumerMale.getSex() + "；" + mConsumerFemale.getSex() + "；" + mConsumerQualifier.getSex());

        mDaggerBinding.nodeProgressBar.setMax(MAX_PROGRESS);
        mDaggerBinding.nodeProgressBar.setProgress(360);
    }

    public static final int MAX_PROGRESS = 500;

    public void onClickDagger(View view) {
        int progress = new Random().nextInt(MAX_PROGRESS + 1);
        mDaggerBinding.button2.setText(progress + "/" + MAX_PROGRESS);
        mDaggerBinding.nodeProgressBar.setProgress(progress);

        //mDaggerBinding.nodeProgressBar.setProgress(200);
    }

}
