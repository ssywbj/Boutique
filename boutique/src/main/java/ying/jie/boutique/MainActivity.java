package ying.jie.boutique;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ying.jie.adapter.SideMenuAdapter;
import ying.jie.boutique.menu_framework.BoutiqueMenuMainFragmentRight;
import ying.jie.boutique.menu_function.BoutiqueMenuMainFragmentLeft;
import ying.jie.boutique.menu_view.BoutiqueMenuMainFragmentCenter;
import ying.jie.util.ActivityCollector;
import ying.jie.util.LogUtil;
import ying.jie.view.SideSlidingMenu;

/**
 * 主页Activity包括左侧侧滑菜单和包含三个Fragment的主界面
 */
public class MainActivity extends BasicActivity {
    private SideSlidingMenu mSideSlidingMenu;

    private BoutiqueMenuMainBasicFragment mMenuMainLeftFrg = new BoutiqueMenuMainFragmentLeft();
    private BoutiqueMenuMainBasicFragment mMenuMainCenterFrg, mMenuMainRightFrg;
    /**
     * 当前显示的主菜单Fragment
     */
    private BoutiqueMenuMainBasicFragment mMenuMainCurrentFrg;
    private TextView mBtnSelected;
    private int mIBtnSelectedId;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private ListView mLVSideMenu;
    private List<String> mDataList;

    protected SparseArray<String> mAtyPackages = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.onCreate(this);
        LogUtil.globalLogInfo(mLogTag + "::onCreate(Bundle)");
        super.setLeftBtnImage(R.drawable.top_bar_main);
        super.setActivityTitle(R.string.main_index);
        super.setRightBtn1Text(R.string.more);

        mAtyPackages.put(0, "ying.jie.boutique.PersonInfoActivity");
        mAtyPackages.put(1, "ying.jie.boutique.VideoViewActivity");
        mAtyPackages.put(2, "ying.jie.boutique.LEDActivity");
        mAtyPackages.put(3, "ying.jie.boutique.BankVoiceActivity");
        mAtyPackages.put(4, "ying.jie.boutique.JniActivity");
        mAtyPackages.put(5, "ying.jie.boutique.CommunicateServiceActivity");
        mAtyPackages.put(6, "ying.jie.boutique.AIDLActivity");
        mAtyPackages.put(7, "ying.jie.boutique.EventBusActivity");
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.boutique_aty;
    }

    @Override
    public void initData() {
        findViewById(R.id.show_fragment_left).setOnClickListener(this);
        findViewById(R.id.show_fragment_center).setOnClickListener(this);
        findViewById(R.id.show_fragment_right).setOnClickListener(this);

        mSideSlidingMenu = (SideSlidingMenu) findViewById(R.id.menu_layout);
        mLVSideMenu = (ListView) findViewById(R.id.boutique_side_menu_lv);
        mDataList = Arrays.asList(getResources().getStringArray(R.array.side_menu_items));
        mLVSideMenu.setAdapter(new SideMenuAdapter(this, mDataList));
        mLVSideMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.openActivity(position);
            }
        });

        mFragmentManager = getFragmentManager();
        this.showFragment(R.id.show_fragment_left);
    }

    private void openActivity(final int position) {
        try {
            Intent intent = new Intent(this, Class.forName(mAtyPackages.get(position)));
            intent.putExtra(App.INT_ACT_TITLE, mDataList.get(position));
            this.startActivity(intent);
        } catch (Exception e) {
            LogUtil.e(mLogTag, e.toString());
        }
    }

    @Override
    public void clickLeftBtn(View view) {
        mSideSlidingMenu.switchMenu();
    }

    @Override
    public void clickRightBtn(View view) {
        startActivity(new Intent(this, MoreActivity.class));
        mSideSlidingMenu.closeLeftMenu();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.show_fragment_left || i == R.id.show_fragment_center || i == R.id.show_fragment_right) {
            this.showFragment(v.getId());
        }
    }

    private void showFragment(int viewId) {
        if (mIBtnSelectedId == viewId) {
            return;//如果点击的是同一个Fragment，则直接返回
        }
        mIBtnSelectedId = viewId;

        if (mBtnSelected != null) {
            mBtnSelected.setSelected(false);
            mBtnSelected.setTextColor(getResources().getColor(R.color.main_menu_btm_text_normal));
        }
        mBtnSelected = (TextView) findViewById(viewId);

        BoutiqueMenuMainBasicFragment menuMainCurrentFragment = null;
        if (viewId == R.id.show_fragment_left) {
            if (mMenuMainLeftFrg == null) {
                mMenuMainLeftFrg = new BoutiqueMenuMainFragmentLeft();
            }
            menuMainCurrentFragment = mMenuMainLeftFrg;
        } else if (viewId == R.id.show_fragment_center) {
            if (mMenuMainCenterFrg == null) {
                mMenuMainCenterFrg = new BoutiqueMenuMainFragmentCenter();
            }
            menuMainCurrentFragment = mMenuMainCenterFrg;
        } else if (viewId == R.id.show_fragment_right) {
            if (mMenuMainRightFrg == null) {
                mMenuMainRightFrg = new BoutiqueMenuMainFragmentRight();
            }
            menuMainCurrentFragment = mMenuMainRightFrg;
        }

        if (menuMainCurrentFragment == null) {
            return;
        }

        mBtnSelected.setSelected(true);
        mBtnSelected.setTextColor(getResources().getColor(R.color.main_menu_btm_text_selected));

        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (mMenuMainCurrentFrg != null) {//如果当前Fragment不为空，则先隐藏当前Fragment
            mFragmentTransaction.hide(mMenuMainCurrentFrg);
        }
        if (!menuMainCurrentFragment.isAdded()) {//如果要显示的Fragment未曾添加，则先添加；执行Add方法后Fragment也会显示
            mFragmentTransaction.add(R.id.boutique_main_menu_fragments_container, menuMainCurrentFragment);
        } else {
            mFragmentTransaction.show(menuMainCurrentFragment);//如果已经添加，则直接显示
        }
        mFragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();

        mMenuMainCurrentFrg = menuMainCurrentFragment;

        if (mMenuMainCurrentFrg == mMenuMainCenterFrg) {
            super.hideTopBar();
        } else {
            super.showTopBar();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.globalLogInfo(mLogTag + "::onStart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.globalLogInfo(mLogTag + "::onDestroy()");
        mAtyPackages.clear();
        ActivityCollector.finishAllActivity();
    }

}
