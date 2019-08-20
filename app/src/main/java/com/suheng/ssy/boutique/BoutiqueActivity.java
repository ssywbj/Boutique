package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.suheng.ssy.boutique.model.Constants;
import com.suheng.ssy.boutique.oom.MemoryLeakageActivity;

import ying.jie.boutique.MainActivity;

public class BoutiqueActivity extends BasicActivity {

    private static SparseArray<Class> sMapAty = new SparseArray<>();

    static {
        sMapAty.put(0, RegexActivity.class);
        sMapAty.put(1, LaunchStandardActivity.class);
        sMapAty.put(2, ConstraintLayoutActivity.class);
        sMapAty.put(3, DataBindingActivity.class);
        sMapAty.put(4, DaggerActivity.class);
        sMapAty.put(5, FragmentRecyclerActivity.class);
        sMapAty.put(7, UnitTestActivity.class);
        sMapAty.put(8, OkGoActivity.class);
        sMapAty.put(9, MainActivity.class);
        sMapAty.put(10, RxJava2Activity.class);
        sMapAty.put(11, RoomActivity.class);
        sMapAty.put(12, PermissionApplyActivity.class);
        sMapAty.put(13, RedDotActivity.class);
        sMapAty.put(15, UploadViewActivity.class);
        sMapAty.put(16, MemoryLeakageActivity.class);
        sMapAty.put(17, PickPictureActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1
                , getResources().getStringArray(R.array.main_item)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pst, long l) {
                openActivity(pst);
            }
        });

        setContentView(listView);

        startActivity(new Intent(this, PickPictureActivity.class));
    }

    private void openActivity(int pst) {
        try {
            if (pst == 14) {
                ARouter.getInstance().build(Constants.ROUTER_APP_ACTIVITY_KEYBOARD).navigation();
            } else if (pst == 6) {
                ARouter.getInstance().build("/module2/activity_demo"/*"/module_b/activity_b"*/)//使用路由跳转
                        .withString("name", "Wbj").withInt("age", 22).navigation(BoutiqueActivity.this,
                        new NavCallback() {
                            @Override
                            public void onFound(Postcard postcard) {
                                super.onFound(postcard);
                                Log.d(mTag, "onFound, " + postcard.getGroup());
                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                super.onLost(postcard);
                                Log.d(mTag, "onLost, " + postcard.getGroup());
                            }

                            @Override
                            public void onArrival(Postcard postcard) {
                                Log.d(mTag, "onArrival, " + postcard.getGroup());
                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {
                                super.onInterrupt(postcard);
                                Log.d(mTag, "onInterrupt, " + postcard.getGroup());
                            }
                        });
            } else {
                Intent intent = new Intent(this, sMapAty.get(pst));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sMapAty.clear();
    }
}
