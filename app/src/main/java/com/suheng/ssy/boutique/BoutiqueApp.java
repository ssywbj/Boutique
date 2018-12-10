package com.suheng.ssy.boutique;

import android.app.Application;

import com.suheng.ssy.boutique.dagger.AppComponent;
import com.suheng.ssy.boutique.dagger.AppModule;
import com.suheng.ssy.boutique.dagger.DaggerAppComponent;

/**
 * Created by wbj on 2018/12/7.
 */
public class BoutiqueApp extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();//强调：AppComponent只能初始化一次
    }

    public AppComponent getAppComponent() {
        //向外界的依赖提供这个AppComponent
        return sAppComponent;
    }

}
