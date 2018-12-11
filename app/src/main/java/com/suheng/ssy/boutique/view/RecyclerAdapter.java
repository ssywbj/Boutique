package com.suheng.ssy.boutique.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    private List<T> mDataList;

    public RecyclerAdapter(List<T> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder viewHolder, int pst) {
        viewHolder.setBinding(this.getVariableId(viewHolder), mDataList.get(pst));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public abstract int getVariableId(@NonNull RecyclerHolder viewHolder);
}
