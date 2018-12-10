package com.suheng.ssy.boutique.dagger;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/10.
 */

public class SingletonTestEntity {

    private String desc;

    @Inject
    public SingletonTestEntity(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
