package com.suheng.ssy.boutique.dagger.ab;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wbj on 2018/12/7.
 */
@Module
public class BModule {

    @Provides
    B providerB() {
        return new B();
    }

}
