package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.suheng.ssy.boutique.dagger.ab.A;
import com.suheng.ssy.boutique.dagger.coffee.CoffeeMachine;
import com.suheng.ssy.boutique.dagger.coffee.DaggerSimpleComponent;
import com.suheng.ssy.boutique.databinding.ActivityDaggerBinding;

import javax.inject.Inject;

public class DaggerActivity extends LaunchTypeActivity {

    private static final String TAG = DaggerActivity.class.getSimpleName();
    //A a;
    @Inject
    A a;

    @Inject
    CoffeeMachine mCoffeeMachine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDaggerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dagger);
        binding.setAty(this);

        //mCoffeeMachine = new CoffeeMachine();
        //mCoffeeMachine = new CoffeeMachine(new Cooker("Wbj", "Suheng"));
        //mCoffeeMachine = new CoffeeMachine(new SimpleMaker(new Cooker("Wbj", "Suheng")));
        DaggerSimpleComponent.builder().build().inject(this);
        Log.d(TAG, "CoffeeMachine makeCoffee: " + mCoffeeMachine.makeCoffee());

        //a = new A();//A的构造方法改变了，此处要修改（第一处要修改）
        Log.d(TAG, "aaaa: " + a.eat());
    }

}
