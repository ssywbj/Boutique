package com.suheng.ssy.boutique.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suheng.ssy.boutique.BR;
import com.suheng.ssy.boutique.BoutiqueApp;
import com.suheng.ssy.boutique.R;
import com.suheng.ssy.boutique.dagger.ActEntity;
import com.suheng.ssy.boutique.dagger.DaggerActivityComponent;
import com.suheng.ssy.boutique.databinding.FragmentRecyclerBinding;
import com.suheng.ssy.boutique.view.RecyclerBindingAdapter;
import com.suheng.ssy.boutique.view.RecyclerBindingHolder;
import com.suheng.ssy.boutique.view.RecyclerDivider;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wbj on 2018/12/11.
 */
public class RecyclerFragment extends BasicFragment {

    @Inject
    ActEntity mActEntity;

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
        DaggerActivityComponent.builder().appComponent(((BoutiqueApp) getActivity()
                .getApplication()).getAppComponent()).build().getActSubComponent().inject(this);
        Log.d(mTag, this + ", onActivityCreated, " + mActEntity.toString());

        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//设置布局管理器
        mViewBinding.recyclerView.addItemDecoration(new RecyclerDivider(getContext(), LinearLayoutManager.VERTICAL));
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

    private class MyAdapter extends RecyclerBindingAdapter<String> {

        private static final int VIEW_TYPE_BLACK = 0;
        private static final int VIEW_TYPE_RED = 1;

        public MyAdapter(List<String> dataList) {
            super(dataList);
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? VIEW_TYPE_BLACK : VIEW_TYPE_RED;
        }

        @NonNull
        @Override//在RecyclerView的adapter中使用DataBinding
        public RecyclerBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding dataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),
                    (viewType == VIEW_TYPE_BLACK ? R.layout.fragment_recycler_adt : R.layout.fragment_recycler_adt2), parent, false);
            return (viewType == VIEW_TYPE_BLACK ? new BlackHolder(dataBinding.getRoot()) : new RedHolder(dataBinding.getRoot()));//实例化ViewHolder
        }

        @Override
        public int getVariableId(@NonNull RecyclerBindingHolder bindingHolder) {
            if (bindingHolder.getItemViewType() == VIEW_TYPE_BLACK) {
                return BR.item;
            } else {
                return BR.textInfo;
            }
        }

        class BlackHolder extends RecyclerBindingHolder {

            TextView textItem;

            public BlackHolder(View view) {
                super(view);
                textItem = view.findViewById(R.id.text_item);
            }
        }

        class RedHolder extends RecyclerBindingHolder {

            TextView textItem;

            public RedHolder(View view) {
                super(view);
                textItem = view.findViewById(R.id.text_info);
            }
        }
    }

}