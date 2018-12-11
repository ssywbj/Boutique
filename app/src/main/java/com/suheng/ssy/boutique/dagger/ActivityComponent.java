package com.suheng.ssy.boutique.dagger;

import com.suheng.ssy.boutique.SingleTaskActivity;
import com.suheng.ssy.boutique.SingleTopActivity;

import dagger.Component;

@PreActivity
//@Singleton //Component使用dependencies依赖其它Component时，不能与依赖的AppComponent的作用域相同，
// 否则会报错；所以我们自定义一个@PerActivity作用域，用于标识ActivityComponent
@Component(dependencies = AppComponent.class, modules = ActModule.class)
public interface ActivityComponent {

    void inject(SingleTaskActivity singleTaskActivity);

    void inject(SingleTopActivity singleTopActivity);

    //Component使用@Subcomponent包括其它Component时，不能与被包括的ActSubComponent的作用域相同，否则会报错；
    // 所以我们在ActSubComponent中使用作用域@Singleton
    ActSubComponent getActSubComponent();
}
