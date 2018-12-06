package com.suheng.ssy.boutique.dagger.coffee;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wbj on 2018/12/6.
 */
@Module
public class SimpleModule {

    @Provides
    Cooker provideCooker() {
        return new Cooker("Wbj", "XXXXXCoffee");
    }

    @Provides
    CoffeeMaker provideCoffeeMaker(Cooker cooker) {
        return new SimpleMaker(cooker);
        //return new SimpleMaker(new Cooker("Ssy", "YYYYYCoffee"));
    }
}
