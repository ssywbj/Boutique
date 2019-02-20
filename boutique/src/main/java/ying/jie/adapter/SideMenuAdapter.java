package ying.jie.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ying.jie.boutique.R;

public class SideMenuAdapter extends
        BasicAdapter<String, SideMenuAdapter.ViewHolder> {

    public SideMenuAdapter(Context context, List<String> dataList) {
        super(context, dataList);
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
        vHolder.textItem.setText(object);
    }

    final class ViewHolder {
        TextView textItem;

        public ViewHolder(View view) {
            textItem = (TextView) view.findViewById(R.id.side_menu_item);
            textItem.setSelected(true);
        }
    }

}
