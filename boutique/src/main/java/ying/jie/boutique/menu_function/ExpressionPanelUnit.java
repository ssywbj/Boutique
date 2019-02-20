package ying.jie.boutique.menu_function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import ying.jie.boutique.R;
import ying.jie.util.LogUtil;

public class ExpressionPanelUnit extends FrameLayout {
    public static final int UNIT_ITEMS = 6 * 3;
    public GridView mGridView;
    private int mImgBeginIndex = R.drawable.expression_item_01;
    private int mImgTotal = 0;

    public ExpressionPanelUnit(Context context, int imgBeginIndex, int imgTotal) {
        super(context);
        if (imgBeginIndex > 0 && imgBeginIndex <= R.drawable.expression_item_28) {
            mImgBeginIndex = imgBeginIndex;
        }
        if (imgTotal > 0 && imgTotal <= UNIT_ITEMS) {
            mImgTotal = imgTotal;
        }

        this.initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expression_panel_unit_view, this);

        mGridView = (GridView) findViewById(R.id.gv_expression);
        mGridView.setAdapter(new PanelUnitAdapter());
    }


    private final class PanelUnitAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImgTotal;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.expression_panel_unit_view_adt, null);

                vHolder = new ViewHolder();
                vHolder.iBtnExpression = (ImageView) convertView.findViewById(R.id.expression_unit_item);

                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            try {
                vHolder.iBtnExpression.setImageResource(mImgBeginIndex + position);
            } catch (Exception e) {
                LogUtil.e(ExpressionPanel.TAG, "set expression image fail-->" + e.toString());
            }
            return convertView;
        }
    }

    private final class ViewHolder {
        ImageView iBtnExpression;
    }

}
