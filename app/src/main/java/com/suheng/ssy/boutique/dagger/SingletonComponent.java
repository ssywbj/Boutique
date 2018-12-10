package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.LaunchStandardActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wbj on 2018/12/10.
 * SingletonModule中的SingletonTestEntity使用@Singleton标注，那么对应的Component也必须采用@Singleton标注，
 * 表明它们的作用域一致，否则编译的时候会报作用域不同的错误.
 */
@Component(modules = SingletonModule.class)
@Singleton
public abstract class SingletonComponent {

    public abstract void inject(LaunchStandardActivity activity);

    private static SingletonComponent sComponent;

    public static SingletonComponent getInstance() {
        if (sComponent == null) {
            sComponent = DaggerSingletonComponent.builder().build();
        }
        return sComponent;
    }
}
