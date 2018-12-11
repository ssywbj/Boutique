package com.suheng.ssy.boutique.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by wbj on 2018/12/11.
 */
public abstract class BasicFragment extends Fragment {

    protected String mTag = "WBJ";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getClass().getSimpleName();
    }

}
