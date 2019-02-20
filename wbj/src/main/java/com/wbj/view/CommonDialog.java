package com.wbj.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wbj.R;
import com.example.wbj.basic.BasicDialog;

public class CommonDialog extends BasicDialog implements View.OnClickListener {
    private TextView mBtnTitle, mTextContent, mBtnLeft, mBtnRight;
    private View mViewBtnDivideLine;
    private OnClickDialog mOnClickDialog;

    public CommonDialog(Context context, String title, String content, String leftBtn, String rightBtn) {
        super(context);
        this.setData(title, content, leftBtn, rightBtn);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_dialog;
    }

    @Override
    protected void init() {
        mBtnTitle = (TextView) findViewById(R.id.text_title);
        mTextContent = (TextView) findViewById(R.id.content);
        mBtnLeft = (TextView) findViewById(R.id.btn_left);
        mBtnRight = (TextView) findViewById(R.id.btn_right);
        mViewBtnDivideLine = findViewById(R.id.line_divide_lr_btn);
    }

    private void setData(String title, String content, String leftBtn, String rightBtn) {
        if (this.isEmpty(content)) {
            return;
        }
        mTextContent.setText(content);
        mTextContent.setMovementMethod(ScrollingMovementMethod.getInstance());//如果内容超过一定高度，设置可以垂直滚动

        if (this.isEmpty(title)) {
            mBtnTitle.setVisibility(View.GONE);

            mTextContent.setMinLines(2);
            int paddingLeft = mTextContent.getPaddingLeft();
            mTextContent.setPadding(paddingLeft, paddingLeft, paddingLeft, paddingLeft);
        } else {
            mBtnTitle.setSelected(true);
            mBtnTitle.setText(title);
        }

        if (this.isEmpty(leftBtn) && this.isEmpty(rightBtn)) {
            return;
        }

        if (this.isEmpty(leftBtn)) {
            mBtnLeft.setVisibility(View.GONE);
            mViewBtnDivideLine.setVisibility(View.GONE);
        } else {
            mBtnLeft.setText(leftBtn);
            mBtnLeft.setSelected(true);
            mBtnLeft.setOnClickListener(this);

            if (this.isEmpty(rightBtn)) {
                mBtnLeft.setBackgroundResource(R.drawable.dialog_radius_left_right_bottom);
            }
        }

        if (this.isEmpty(rightBtn)) {
            mBtnRight.setVisibility(View.GONE);
            mViewBtnDivideLine.setVisibility(View.GONE);
        } else {
            mBtnRight.setText(rightBtn);
            mBtnRight.setSelected(true);
            mBtnRight.setOnClickListener(this);

            if (this.isEmpty(leftBtn)) {
                mBtnRight.setBackgroundResource(R.drawable.dialog_radius_left_right_bottom);
            }
        }
    }

    @Override
    public void show() {
        if (this.isEmpty(mTextContent.getText().toString())) {
            Toast.makeText(getContext(), "Please Input Dialog Content !!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.isEmpty(mBtnLeft.getText().toString()) && this.isEmpty(mBtnRight.getText().toString())) {
            Toast.makeText(getContext(), "At Lease One Button", Toast.LENGTH_SHORT).show();
            return;
        }
        super.show();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (mOnClickDialog != null) {
            switch (v.getId()) {
                case R.id.btn_left:
                    mOnClickDialog.clickLeftBtn();
                    break;
                case R.id.btn_right:
                    mOnClickDialog.clickRightBtn();
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isEmpty(String text) {
        return text == null || "".equals(text.trim());
    }

    public void setOnClickDialog(OnClickDialog onClickDialog) {
        mOnClickDialog = onClickDialog;
    }

    public interface OnClickDialog {
        void clickLeftBtn();

        void clickRightBtn();
    }

}
