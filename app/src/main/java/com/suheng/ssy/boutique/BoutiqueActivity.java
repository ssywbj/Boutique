package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
                Intent intent;
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
                    default:
                        intent = new Intent(BoutiqueActivity.this, RegexActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });

        setContentView(listView);

        startActivity(new Intent(this, DaggerActivity.class));
    }

}
