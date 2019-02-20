package ying.jie.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ying.jie.boutique.R;

/**
 * Created by wbj on 2016/10/26.
 */
public final class ItemAdapter extends
        BasicAdapter<String, ItemAdapter.ViewHolder> {
    private Context mContext;

    public ItemAdapter(Context context, List<String> dataList) {
        super(context, dataList);
        mContext = context;
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.boutique_aty_adt_side;
    }

    @Override
    protected ViewHolder loadViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindView(int position, String object, ViewHolder vHolder) {
        vHolder.textItem.setTextColor(mContext.getResources().getColor(R.color.black));
        vHolder.textItem.setText(object);
    }

    final class ViewHolder {
        TextView textItem;

        public ViewHolder(View view) {
            textItem = (TextView) view.findViewById(R.id.side_menu_item);
        }
    }
}