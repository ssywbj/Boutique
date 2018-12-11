package com.suheng.ssy.boutique.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suheng.ssy.boutique.BR;
import com.suheng.ssy.boutique.R;
import com.suheng.ssy.boutique.databinding.FragmentRecyclerAdtBinding;
import com.suheng.ssy.boutique.databinding.FragmentRecyclerBinding;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wbj on 2018/12/11.
 */
public class RecyclerFragment extends BasicFragment {

    private FragmentRecyclerBinding mViewBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(mTag, this + ", onCreate");
    }

    @Nullable
    @Override//在Fragment中使用Databinding
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(mTag, this + ", onCreateView");
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(mTag, this + ", onActivityCreated");

        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//设置布局管理器
        mViewBinding.recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mViewBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mViewBinding.recyclerView.setAdapter(new MyAdapter(Arrays.asList(getResources().getStringArray(R.array.main_item))));//设置adapter
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(mTag, this + ", onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(mTag, this + ", onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(mTag, this + ", onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(mTag, this + ", onDetach");
    }

    private class MyAdapter extends RecyclerView.Adapter {

        private List<String> mDataList;

        private MyAdapter(List<String> dataList) {
            mDataList = dataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FragmentRecyclerAdtBinding dataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                    R.layout.fragment_recycler_adt, parent, false);
            //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recycler_adt, parent, false); // 实例化展示的view
            RecyclerView.ViewHolder viewHolder = new ViewHolder(dataBinding.getRoot()); //实例化ViewHolder
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pst) {
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).setBinding(BR.item, mDataList.get(pst));

                //FragmentRecyclerAdtBinding dataBinding = DataBindingUtil.getBinding(viewHolder.itemView);
                /*dataBinding.setItem(mDataList.get(pst));
                dataBinding.executePendingBindings();*/

                //((ViewHolder) viewHolder).textItem.setText(mDataList.get(pst));// 绑定数据
            }
        }


        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final ViewDataBinding mDataBinding;

            TextView textItem;

            public ViewHolder(View view) {
                super(view);
                textItem = view.findViewById(R.id.text_item);
                mDataBinding = DataBindingUtil.bind(view);
            }

            public void setBinding(int variableId, Object object) {
                mDataBinding.setVariable(variableId, object);
                mDataBinding.executePendingBindings();
            }
        }
    }


    private class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{android.R.attr.listDivider};
        public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL = LinearLayoutManager.VERTICAL;
        /**
         * 用于绘制间隔样式
         */
        private Drawable mDivider;
        /**
         * 列表的方向，水平/竖直
         */
        private int mOrientation;

        public MyDividerItemDecoration(Context context, int orientation) {
            // 获取默认主题的属性
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();

            mOrientation = (orientation == HORIZONTAL ? HORIZONTAL : VERTICAL);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            // 绘制间隔
            if (mOrientation == VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }

        /**
         * 绘制间隔
         */
        private void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        /**
         * 绘制间隔
         */
        private void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
