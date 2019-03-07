package com.suheng.ssy.boutique.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
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

import com.alibaba.android.arouter.facade.annotation.Route;
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
@Route(path = "/app/activity/fragment_recycler")//也可以通过路由的方式启动Fragment
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

    //List<ItemModel> itemModels = new ArrayList<>();
    ObservableList<ItemModel> modelObservableList = new ObservableArrayList<>();
    MyAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerActivityComponent.builder().appComponent(((BoutiqueApp) getActivity()
                .getApplication()).getAppComponent()).build().getActSubComponent().inject(this);
        Log.d(mTag, this + ", onActivityCreated, " + mActEntity.toString());

        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//设置布局管理器
        mViewBinding.recyclerView.addItemDecoration(new RecyclerDivider(getContext(), LinearLayoutManager.VERTICAL));
        mViewBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.main_item));

        for (String str : stringList) {
            //itemModels.add(new ItemModel(str, this));
            modelObservableList.add(new ItemModel(str, this));
        }
        adapter = new MyAdapter(modelObservableList);
        mViewBinding.recyclerView.setAdapter(adapter);//设置adapter
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

    private class MyAdapter extends RecyclerBindingAdapter<ItemModel> {

        private static final int VIEW_TYPE_BLACK = 0;
        private static final int VIEW_TYPE_RED = 1;

        public MyAdapter(ObservableList<ItemModel> modelObservableList) {
            super(modelObservableList);
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
            return BR.itemModel;
        }

        class BlackHolder extends RecyclerBindingHolder {

            public BlackHolder(View view) {
                super(view);
            }
        }

        class RedHolder extends RecyclerBindingHolder {

            public RedHolder(View view) {
                super(view);
            }
        }
    }

    public static class ItemModel {
        private ObservableField<String> title = new ObservableField<>();
        private RecyclerFragment recyclerFragment;

        public ItemModel(String title, RecyclerFragment recyclerFragment) {
            this.title.set(title);
            this.recyclerFragment = recyclerFragment;
        }

        public void onClickItem(ItemModel itemModel) {
            Log.d("WBJ", "item: " + itemModel.title.get());
            recyclerFragment.modelObservableList.remove(itemModel);
            recyclerFragment.adapter.notifyDataSetChanged();
        }

        public ObservableField<String> getTitle() {
            return title;
        }
    }

}