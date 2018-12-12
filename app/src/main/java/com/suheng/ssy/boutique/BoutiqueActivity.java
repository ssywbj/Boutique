package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;

public class BoutiqueActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1
                , getResources().getStringArray(R.array.main_item)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                switch (i) {
                    case 0:
                        intent = new Intent(BoutiqueActivity.this, RegexActivity.class);
                        break;
                    case 1:
                        intent = new Intent(BoutiqueActivity.this, LaunchStandardActivity.class);
                        break;
                    case 2:
                        intent = new Intent(BoutiqueActivity.this, ConstraintLayoutActivity.class);
                        break;
                    case 3:
                        intent = new Intent(BoutiqueActivity.this, DataBindingActivity.class);
                        break;
                    case 4:
                        intent = new Intent(BoutiqueActivity.this, DaggerActivity.class);
                        break;
                    case 5:
                        intent = new Intent(BoutiqueActivity.this, FragmentRecyclerActivity.class);
                        break;
                    case 6:
                        //intent = new Intent(BoutiqueActivity.this, ARouterActivity.class);
                        /*ARouter.getInstance().build("/module_b/activity_b")
                                .withString("name", "Wbj").withInt("age", 22).navigation();*/
                        ARouter.getInstance().build("/module2/activity_demo")
                                .withString("name", "Wbj").withInt("age", 22).navigation();
                        break;
                    default:
                        intent = new Intent(BoutiqueActivity.this, RegexActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        setContentView(listView);

        startActivity(new Intent(this, FragmentRecyclerActivity.class));
    }

}
