package com.suheng.ssy.boutique.dagger.ab;

import com.suheng.ssy.boutique.BoutiqueApp;

/**
 * Created by wbj on 2018/12/6.
 */
//@Component(modules = {MainModule.class, SimpleModule.class})
public interface MainComponent {

    //void inject(RegexActivity activity);
    /*void inject(DaggerActivity activity);//多个Component中，一个activity只能被inject一次*/

    void inject(BoutiqueApp boutiqueApp);
}
