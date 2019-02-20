package com.wbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wbj.R;
import com.example.wbj.basic.BasicActivity;

import java.lang.ref.WeakReference;

public class TitleBar extends FrameLayout {
    private View mLayoutEditMode;
    private TextView mTVSelectedCount, mTVSelectedStatus;
    private boolean mIsSelectedAll;

    public TitleBar(Context context) {
        super(context);
        this.initView();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.title_bar_height));
        setBackgroundResource(R.color.theme_blue);

        View.inflate(getContext(), R.layout.view_title_bar, this);
        findViewById(R.id.ib_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof BasicActivity) {
                    ((BasicActivity) getContext()).finish();
                }
            }
        });
    }

    public View setLRLeftBtn(int drawableId) {
        ImageView imageBtn = (ImageView) findViewById(R.id.title_bar_edit_ib_left);
        imageBtn.setImageResource(drawableId);
        return imageBtn;
    }

    public View setLRMiddleBtn(int drawableId) {
        ImageView imageBtn = (ImageView) findViewById(R.id.title_bar_edit_ib_middle);
        imageBtn.setImageResource(drawableId);
        return imageBtn;
    }

    public View setLRRightBtn(int drawableId) {
        ImageView imageBtn = (ImageView) findViewById(R.id.title_bar_edit_ib_right);
        imageBtn.setImageResource(drawableId);
        return imageBtn;
    }

    private void showEditLayout() {
        if (mLayoutEditMode == null) {
            mLayoutEditMode = ((ViewStub) findViewById(R.id.stub_edit_mode)).inflate();
            mTVSelectedCount = (TextView) findViewById(R.id.tv_select_count);

            mTVSelectedStatus = (TextView) findViewById(R.id.tv_select_status);
            if (mEditSelectedAllListener != null) {
                mTVSelectedStatus.setOnClickListener(mEditSelectedAllListener);
            }

            if (mExitEditModeListener != null) {
                findViewById(R.id.tv_select_exit).setOnClickListener(mExitEditModeListener);
            }
        } else {
            mLayoutEditMode.setVisibility(VISIBLE);
        }
    }

    private void hideEditLayout() {
        if (mLayoutEditMode != null) {
            mLayoutEditMode.setVisibility(GONE);
            mTVSelectedCount.setText(getResources().getString(R.string.selected_count, 0));
        }
    }

    public void enterEditMode() {
        findViewById(R.id.layout_normal_mode).setVisibility(GONE);
        this.showEditLayout();
    }

    public void setSelectedCount(int selectedCount, int totalCount) {
        if (selectedCount < 0 || totalCount <= 0 || selectedCount > totalCount
                || mTVSelectedCount == null || mTVSelectedStatus == null) {
            return;
        }
        mTVSelectedCount.setText(getResources().getString(R.string.selected_count, selectedCount));

        mIsSelectedAll = (selectedCount == totalCount);
        mTVSelectedStatus.setText(mIsSelectedAll ? R.string.select_none : R.string.select_all);
    }

    public void exitEditMode() {
        findViewById(R.id.layout_normal_mode).setVisibility(VISIBLE);
        this.hideEditLayout();
    }

    private ExitEditModeListener mExitEditModeListener;

    public void setExitEditModeListener(ExitEditModeListener exitEditModeListener) {
        mExitEditModeListener = exitEditModeListener;
    }

    public static abstract class ExitEditModeListener implements View.OnClickListener {
        private WeakReference<TitleBar> mBarWeakReference;

        public ExitEditModeListener(TitleBar titleBar) {
            mBarWeakReference = new WeakReference<>(titleBar);
        }

        @Override
        public void onClick(View v) {
            if (mBarWeakReference != null && mBarWeakReference.get() != null) {
                mBarWeakReference.get().exitEditMode();
                this.exitEditMode();
            }
        }

        public abstract void exitEditMode();
    }

    private EditSelectedAllListener mEditSelectedAllListener;

    public void setEditSelectedAllListener(EditSelectedAllListener editSelectedAllListener) {
        mEditSelectedAllListener = editSelectedAllListener;
    }

    public abstract static class EditSelectedAllListener implements View.OnClickListener {
        private WeakReference<TitleBar> mBarWeakReference;

        public EditSelectedAllListener(TitleBar titleBar) {
            mBarWeakReference = new WeakReference<>(titleBar);
        }

        @Override
        public void onClick(View v) {
            if (mBarWeakReference != null && mBarWeakReference.get() != null) {
                this.setSelectedAll(!mBarWeakReference.get().mIsSelectedAll);
            }
        }

        public abstract void setSelectedAll(boolean isSelectedAll);
    }

}
