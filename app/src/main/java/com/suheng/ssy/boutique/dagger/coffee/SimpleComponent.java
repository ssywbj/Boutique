package com.suheng.ssy.boutique.dagger.coffee;

import com.suheng.ssy.boutique.ConstraintLayoutActivity;
import com.suheng.ssy.boutique.DaggerActivity;

import dagger.Component;

/**
 * Created by wbj on 2018/12/6.
 */
@Component(modules = SimpleModule.class)
public interface SimpleComponent {
    /*
    inject方法包含哪些activity，就需要在那些activity里执行inject方法，不然没有inject到activity使用到
    该实例时，会报空指针异常
     */
    void inject(DaggerActivity activity);

    void inject(ConstraintLayoutActivity activity);
}
