package com.wbj.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wbj.R;
import com.example.wbj.basic.BasicAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DragListActivity extends Activity {
    private static final String TAG = DragListActivity.class.getSimpleName();
    private DragSortListView mDragSortListView;
    private SortAdapter mSortAdapter;
    private List<Body> mBodyList = new ArrayList<>();

    private class SortAdapter extends BasicAdapter<Body, SortAdapter.ViewHolder> {

        public SortAdapter(Context context, List<Body> objects) {
            super(context, R.layout.drag_list_activity_adt, objects);
        }

        @Override
        protected ViewHolder loadViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void bindView(int position, Body data, ViewHolder vHolder) {
            vHolder.tvTitle.setText(data.text);
            vHolder.ivCountryLogo.setImageResource(data.src);
        }

        class ViewHolder {
            TextView tvTitle;
            ImageView ivCountryLogo;

            public ViewHolder(View view) {
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                ivCountryLogo = (ImageView) view.findViewById(R.id.ivCountryLogo);
            }
        }
    }

    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
            if (from != to) {
                autoDrop(from, to);
            }
        }
    };

    //删除监听器，点击左边差号就触发。删除item操作
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            mSortAdapter.remove(mSortAdapter.getItem(which));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_list_activity);
        initData();
        mDragSortListView.setDropListener(onDrop);
        mDragSortListView.setRemoveListener(onRemove);
        mSortAdapter = new SortAdapter(this, mBodyList);
        mDragSortListView.setAdapter(mSortAdapter);
        mDragSortListView.setDragEnabled(true); //设置是否可拖动。

        new WeakHandler(this).sendEmptyMessageDelayed(11, 2000);
    }

    private void initData() {
        mDragSortListView = (DragSortListView) findViewById(R.id.dslvList);

        String[] array = getResources().getStringArray(R.array.main_item_list);
        Body body;
        for (int i = 0; i < array.length; i++) {
            body = new Body();
            body.text = array[i];
            body.src = R.drawable.list_icon;
            mBodyList.add(body);
        }
    }

    public void autoDrop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
        if (from != to) {
            Body item = mSortAdapter.getItem(from);//得到listview的适配器
            mSortAdapter.remove(item);//在适配器中”原位置“的数据。
            mSortAdapter.insert(item, to);//在目标位置中插入被拖动的控件。
        }
    }

    public class Body {
        int src;
        String text;
    }

    private static class WeakHandler extends Handler {
        private WeakReference<DragListActivity> mWeakReference;

        private WeakHandler(DragListActivity instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DragListActivity instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case 11:
                    instance.autoDrop(0, 3);
                    break;
                default:
                    break;
            }
        }
    }

}