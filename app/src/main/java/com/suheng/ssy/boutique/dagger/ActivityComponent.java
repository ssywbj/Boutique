package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.LaunchTypeActivity;
import com.suheng.ssy.boutique.SingleTopActivity;

import dagger.Component;

@PreActivity
//@Singleton //不能与依赖的AppComponent的作用域相同，否则会报错；所以我们自定义一个@PerActivity作用域
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {

    void inject(LaunchTypeActivity launchTypeActivity);

    void inject(SingleTopActivity singleTopActivity);
}
