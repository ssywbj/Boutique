package com.suheng.ssy.boutique.dagger;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wbj on 2018/12/7.
 */
@Module
public class DataModule {

    public static final String MALE = "male";
    public static final String FEMALE = "female";

    //一个默认的Consumer
    @Provides
    Consumer provideConsumer() {
        return new Consumer();
    }

    @Provides
    PriorityTestEntity providePriorityTestEntity() {//@Module和@Inject均可以提供实例化对象，如果两者同时存在@Module的优先级高
        return new PriorityTestEntity("我是module提供的对象");
    }

    //采用@Qualifier注解，表示我可以提供这种标识符的Consumer
    @Provides
    @Named(MALE)
    Consumer provideConsumerMale() {
        return new Consumer("汉子");
    }

    @Provides
    @Named(FEMALE)
    Consumer provideConsumerFemale() {
        return new Consumer("妹子");
    }

    @Provides
    @ConsumerQualifier
    Consumer provideConsumerByQualifier() {
        return new Consumer("qualifier sex");
    }

}
