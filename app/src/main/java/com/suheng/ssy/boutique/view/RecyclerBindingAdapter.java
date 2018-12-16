package com.suheng.ssy.boutique.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerBindingAdapter<T> extends RecyclerView.Adapter<RecyclerBindingHolder> {

    private List<T> mDataList;

    public RecyclerBindingAdapter(List<T> dataList) {
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerBindingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerBindingHolder bindingHolder, int pst) {
        bindingHolder.setBinding(this.getVariableId(bindingHolder), mDataList.get(pst));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public abstract int getVariableId(@NonNull RecyclerBindingHolder bindingHolder);
}
