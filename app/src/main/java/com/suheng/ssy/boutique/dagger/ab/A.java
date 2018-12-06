package com.suheng.ssy.boutique.dagger.ab;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/6.
 */
public class A {//A类会在多处被new出来引用到，如果此时构造方法改变，必将涉及到多处的修改

    @Inject
    B b;

    /*@Inject
    public A(B b) {
        this.b = b;
    }*/

    @Inject
    public A() {
        //this.b = b;
    }

    public String eat() {
        return b == null ? "A eat eat eat" : b.eat();
    }
}
