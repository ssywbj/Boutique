package ying.jie.boutique.menu_framework;

import android.widget.ListView;

import ying.jie.boutique.R;
import ying.jie.boutique.BoutiqueMenuMainBasicFragment;

public class BoutiqueMenuMainFragmentRight extends BoutiqueMenuMainBasicFragment {

    @Override
    public int getLayoutId() {
        return R.layout.boutique_menu_main_fragment_right;
    }

    @Override
    public int getArrayId() {
        return R.array.frg_framework_items;
    }

    @Override
    public void initData() {
        mAtyPackages.put(0, "ying.jie.boutique.menu_framework.NettyActivity");
        mAtyPackages.put(1, "ying.jie.boutique.menu_framework.ProtoBufferActivity");
        mAtyPackages.put(2, "ying.jie.boutique.menu_framework.JsonActivity");

        mListView = (ListView) mActivity.findViewById(R.id.frg_framework_lv);
    }


}
