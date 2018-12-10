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

    /*@Component不仅可以注解接口也可以注解抽象类，为了方便测试单例，把Component改为抽象类，
      实际开发中可以在Application中创建单例。 */
    public abstract void inject(LaunchStandardActivity activity);

    /**
     * SingletonComponent必须是单例的，否则怎么能保证不同的Component对象提供同一个依赖实例呢？
     */
    private static SingletonComponent sComponent;

    public static SingletonComponent getInstance() {
        if (sComponent == null) {
            sComponent = DaggerSingletonComponent.builder().build();
        }
        return sComponent;
    }
}
