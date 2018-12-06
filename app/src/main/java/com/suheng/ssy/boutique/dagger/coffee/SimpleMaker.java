package com.suheng.ssy.boutique.dagger.coffee;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/6.
 */

public class SimpleMaker implements CoffeeMaker {

    @Inject
    Cooker mCooker;

    @Inject
    public SimpleMaker(Cooker cooker) {
        mCooker = cooker;
    }

    @Override
    public String makeCoffee() {
        //return "Coffee is made by SimperMarker";
        return mCooker.make();
    }
}
