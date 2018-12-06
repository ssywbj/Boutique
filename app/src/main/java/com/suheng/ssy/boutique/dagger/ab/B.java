package com.suheng.ssy.boutique.dagger.ab;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/6.
 */
public class B {

    @Inject
    public B() {
    }

    public String eat() {
        return "B eat eat eat";
    }
}
