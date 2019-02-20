package ying.jie.boutique.menu_function;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

public class ExpressionActivity extends BasicActivity implements ExpressionPanel.OnPanelItemClickListener {
    private static final String TAG = ExpressionActivity.class.getSimpleName();
    private RelativeLayout mParentLayout;
    private ExpressionPanel mExpressionPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.hideTopBar();
    }

    @Override
    public int getLayoutId() {
        return R.layout.expression_aty;
    }

    @Override
    public void initData() {
        findViewById(R.id.ibtn_show_express_panel).setOnClickListener(this);
        findViewById(R.id.ibtn_expression_item).setOnClickListener(this);

        mParentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        mExpressionPanel = (ExpressionPanel) findViewById(R.id.expression_panel);
        mExpressionPanel.setOnPanelItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ibtn_show_express_panel) {
            mExpressionPanel.showByAnimation();
        } else if (i == R.id.ibtn_expression_item) {
            ToastUtil.showToast("Expression Item");
        }
    }

    @Override
    public void onBackPressed() {
        if (mExpressionPanel != null && mExpressionPanel.isVisible()) {
            mExpressionPanel.hideByAnimation();
        } else {
            super.onBackPressed();//退出当前Activity
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "expression activity were destroyed");
    }

    @Override
    public void onPanelItemClick(Drawable drawable, int pst) {
        final ExpressionFloatView expressionFloatView = new ExpressionFloatView(this);
        expressionFloatView.setImageExpressionAndBeginAnim(drawable, true);
        mParentLayout.addView(expressionFloatView);
    }

}

