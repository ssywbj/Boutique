package ying.jie.boutique;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ying.jie.adapter.ItemAdapter;
import ying.jie.util.LogUtil;

public abstract class BoutiqueMenuMainBasicFragment extends Fragment {
    protected MainActivity mActivity;
    protected String mLogTag = getClass().getSimpleName();

    protected ListView mListView;
    protected List<String> mDataList = new ArrayList<>();
    protected SparseArray<String> mAtyPackages = new SparseArray<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(mLogTag, "Parent Frg onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
        return inflater.inflate(this.getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d(mLogTag, "Parent Frg onViewCreated(View view, Bundle savedInstanceState)");
        view.setOnTouchListener(new View.OnTouchListener() {//防止点击事件穿透到下层
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(mLogTag, "Parent Frg onActivityCreated(Bundle savedInstanceState)");

        mActivity = (MainActivity) getActivity();//在这里能保证获取到Activity的实例
        this.initData();

        if (mListView == null) {
            return;
        }
        mDataList.addAll(Arrays.asList(mActivity.getResources().getStringArray(this.getArrayId())));
        mListView.setAdapter(new ItemAdapter(mActivity, mDataList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });
    }

    protected void openActivity(final int position) {
        try {
            if (position > mAtyPackages.size() - 1) {
                return;
            }
            Intent intent = new Intent(mActivity, Class.forName(mAtyPackages.get(position)));
            intent.putExtra(App.INT_ACT_TITLE, mDataList.get(position));
            startActivity(intent);
        } catch (Exception e) {
            LogUtil.e(mLogTag, e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (String itemData : mDataList) {
            LogUtil.d(mLogTag, this + "-->" + mDataList + "-->" + itemData);
        }
        mDataList.clear();
        mAtyPackages.clear();
    }

    public abstract int getLayoutId();

    public abstract int getArrayId();

    public abstract void initData();
}
