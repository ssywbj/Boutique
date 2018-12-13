package com.suheng.ssy.boutique.arouter;

/**
 * 作者：黎伟杰-子然 on 2017/4/16.
 * 邮箱：liweijie@linghit.com
 * description：
 * update by:
 * update day:
 */
public class HomeIntent {
    private static boolean hasModule() {
        return ModuleManager.getInstance().hasModule(IHomeProvider.HOME_MAIN_SERVICE);
    }
    public static void launchHome(int tabType) {
        //HomeActivity
        MyBundle bundle = new MyBundle();
        bundle.put(IHomeProvider.HOME_TABTYPE, tabType);
        MyRouter.newInstance(IHomeProvider.HOME_ACT_HOME)
                .withBundle(bundle)
                .navigation();
    }
}
