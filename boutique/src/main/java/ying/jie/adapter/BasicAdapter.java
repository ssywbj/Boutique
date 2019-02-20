package ying.jie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BasicAdapter<T, ViewHolder> extends BaseAdapter {
    private Context mContext;
    private List<T> mDataList;

    public BasicAdapter(Context context, List<T> dataList) {
        mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList == null ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(getItemViewLayoutId(), null);
            vHolder = loadViewHolder(convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        T object = getItem(position);
        if (object != null) {
            bindView(position, object, vHolder);
        }

        return convertView;
    }

    protected abstract int getItemViewLayoutId();

    protected abstract void bindView(int position, T object, ViewHolder vHolder);

    protected abstract ViewHolder loadViewHolder(View view);

}
