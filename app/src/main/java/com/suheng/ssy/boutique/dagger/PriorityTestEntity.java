package com.suheng.ssy.boutique.dagger;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/7.
 */
public class PriorityTestEntity {
    private String name;

    @Inject
    public PriorityTestEntity() {
        name = "我是@Inject注解提供的对象";
    }

    public PriorityTestEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
