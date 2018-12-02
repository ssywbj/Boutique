package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.suheng.ssy.boutique.databinding.ActivityDataBindingBinding;

public class DataBindingActivity extends BasicActivity implements Navigor {

    private ActivityDataBindingBinding mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        User user = new User("Wbj", 29);
        user.setNavigor(this);
        mDataBinding.setUser(user);

        mDataBinding.setText("Text");
    }


    @Override
    public void tip(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
