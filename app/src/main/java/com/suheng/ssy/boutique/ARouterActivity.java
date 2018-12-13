package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.suheng.ssy.boutique.model.HellService;

@Route(path = "/module_b/activity_b")//路径至少需要有两级，第一级是Group名称
public class ARouterActivity extends BasicActivity {

    @Autowired(name = "name")
    String school;
    @Autowired
    int age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter);

        ARouter.getInstance().inject(this);
        Log.d(mTag, "name = " + school + ", age = " + age);
    }

    public void onClickARouter(View view) {
        HellService hellService = (HellService) ARouter.getInstance().build("/service/hello").navigation();
        hellService.sayHello(school);
    }
}
