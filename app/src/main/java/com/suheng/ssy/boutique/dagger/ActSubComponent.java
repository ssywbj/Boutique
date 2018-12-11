package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.SingleTaskActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by wbj on 2018/12/11.
 */
@Subcomponent//@Subcomponent注解表示这个component能被包含
@Singleton
public interface ActSubComponent {

    void inject(SingleTaskActivity taskActivity);
}
