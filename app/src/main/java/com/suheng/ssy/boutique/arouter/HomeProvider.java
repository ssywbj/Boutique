package com.suheng.ssy.boutique.arouter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * 作者：黎伟杰-子然 on 2017/4/16.
 * 邮箱：liweijie@linghit.com
 * description：
 * update by:
 * update day:
 */
@Route(path = IHomeProvider.HOME_MAIN_SERVICE)
public class HomeProvider implements IHomeProvider {
    private Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectedTab(Activity activity, int position) {
        /*if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).selectedTab(position);
        }*/
    }
}
