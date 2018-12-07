package com.suheng.ssy.boutique.dagger.coffee;

/**
 * Created by wbj on 2018/12/6.
 */

public class Cooker {

    String name; //咖啡师名字
    String coffeeKind; //制作咖啡的类型

    //@Inject
    public Cooker(String name, String coffeeKind) {
        this.name = name;
        this.coffeeKind = coffeeKind;
    }

    public String make() {
        return name + " make " + coffeeKind; //咖啡师制作Coffee的过程
    }
}
