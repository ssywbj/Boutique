package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.DaggerActivity;

import dagger.Component;

/**
 * Created by wbj on 2018/12/7.
 * 没有modules和dependencies的情况下，纯粹用@Inject来提供依赖
 */
@Component(modules = DataModule.class)
public interface ConsumerComponent {
    void inject(DaggerActivity activity);
}
