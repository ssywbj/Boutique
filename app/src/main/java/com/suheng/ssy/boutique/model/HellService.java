package com.suheng.ssy.boutique.model;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wbj on 2018/12/12.
 */
public interface HellService extends IProvider {

    String sayHello(String name);
}
