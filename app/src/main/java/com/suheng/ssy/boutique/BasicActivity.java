package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BasicActivity extends AppCompatActivity {

    protected String mTag = "WBJ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mTag = getClass().getSimpleName();
    }
}
