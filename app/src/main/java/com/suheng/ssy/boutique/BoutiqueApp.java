package com.suheng.ssy.boutique;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
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
        sAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();//强调：AppComponent只能初始化一次

        //if (isDebug()) {
        //这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();//打印日志
        ARouter.openDebug();//开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        //}
        ARouter.init(this);//尽可能早，推荐在Application中初始化
    }

    public AppComponent getAppComponent() {
        //向外界的依赖提供这个AppComponent
        return sAppComponent;
    }

}
