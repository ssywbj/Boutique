package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasicActivity extends AppCompatActivity {

    protected String mTag = "WBJ";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();//解决RxJava2警告“The result of subscribe is not used。。。”

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mTag = getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }
}
