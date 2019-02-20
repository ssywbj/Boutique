package ying.jie.boutique.menu_function;

import android.widget.ListView;

import ying.jie.boutique.R;
import ying.jie.boutique.BoutiqueMenuMainBasicFragment;

public class BoutiqueMenuMainFragmentLeft extends BoutiqueMenuMainBasicFragment {

    @Override
    public int getLayoutId() {
        return R.layout.boutique_menu_main_fragment_left;
    }

    @Override
    public int getArrayId() {
        return R.array.frg_function_items;
    }

    @Override
    public void initData() {
        mAtyPackages.put(0, "ying.jie.boutique.menu_function.ScreenCaptureActivity");
        mAtyPackages.put(1, "ying.jie.boutique.menu_function.ScreenRecordActivity");
        mAtyPackages.put(2, "ying.jie.boutique.menu_function.ExpressionActivity");
        mListView = (ListView) mActivity.findViewById(R.id.frg_function_lv);
    }

}
