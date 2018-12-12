package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.fragment.RecyclerFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by wbj on 2018/12/11.
 */
@Singleton
@Subcomponent(modules = {ActModule.class})//@Subcomponent注解表示这个component能被包含
public interface ActSubComponent {

    void inject(RecyclerFragment recyclerFragment);
}
