package com.suheng.ssy.boutique.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class ActModule {

    @Provides
    ActEntity provideActEntity() {
        return new ActEntity("我是ActEntity");
    }
}
