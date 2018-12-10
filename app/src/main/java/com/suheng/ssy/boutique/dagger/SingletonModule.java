package com.suheng.ssy.boutique.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wbj on 2018/12/10.
 */
@Module
public class SingletonModule {

    @Provides
    @Singleton
    SingletonTestEntity provideSingletonTestEntity() {
        return new SingletonTestEntity("测试单例");
    }
}
