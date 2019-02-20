package ying.jie.boutique.menu_framework;

import android.os.Bundle;
import android.view.View;
import ying.jie.boutique.App;
import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;

public class ProtoBufferActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    @Override
    public int getLayoutId() {
        return R.layout.proto_buffer_aty;
    }

    @Override
    public void initData() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

}
