package com.suheng.ssy.boutique.model;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Created by wbj on 2018/12/12.
 */
@Route(path = "/service/hello", name = "测试服务")
public class HellServiceImpl implements HellService {

    @Override
    public String sayHello(String name) {
        Log.d("WBJ", "Hello, " + name);
        return "Hello, " + name;
    }

    @Override
    public void init(Context context) {
        Log.d("WBJ", "init, HellServiceImpl");//在整个应用中，只会初始化一次
    }

}
