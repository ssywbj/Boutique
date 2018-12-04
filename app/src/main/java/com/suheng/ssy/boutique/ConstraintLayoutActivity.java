package com.suheng.ssy.boutique;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;

import com.suheng.ssy.boutique.databinding.ActivityConstraintLayoutBinding;
import com.suheng.ssy.boutique.model.LoginNavigator;
import com.suheng.ssy.boutique.model.LoginViewModel;

public class ConstraintLayoutActivity extends BasicActivity implements LoginNavigator {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConstraintLayoutBinding layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_constraint_layout);
        layoutBinding.setLoginViewModel(new LoginViewModel(this));

        layoutBinding.editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LoginViewModel.EDIT_PHONE_MAX_LENGTH)});
    }

}
