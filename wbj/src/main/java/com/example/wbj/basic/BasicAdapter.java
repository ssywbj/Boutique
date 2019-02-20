package com.example.wbj.basic;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BasicAdapter<T, ViewHolder> extends ArrayAdapter<T> {
    private int mLayoutId;

    public BasicAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        mLayoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutId, null);
            viewHolder = this.loadViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        this.bindView(position, getItem(position), viewHolder);
        return convertView;
    }

    protected abstract ViewHolder loadViewHolder(View view);

    protected abstract void bindView(int position, T object, ViewHolder vHolder);
}
