package com.suheng.ssy.boutique.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerBindingHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mViewBinding;

    public RecyclerBindingHolder(@NonNull View itemView) {
        super(itemView);
        mViewBinding = DataBindingUtil.bind(itemView);
    }

    public void setBinding(int variableId, @Nullable Object value) {
        mViewBinding.setVariable(variableId, value);
        mViewBinding.executePendingBindings();
    }
}
