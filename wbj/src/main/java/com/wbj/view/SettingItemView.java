package com.wbj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wbj.R;

public class SettingItemView extends RelativeLayout {
    private static final String TAG = SettingItemView.class.getSimpleName();
    private ImageView mImageLeft;
    private Drawable mLeftDrawable;

    private TextView mTextMainTitle, mTextSubTitle;
    private String mMainTitle, mSubTitle;

    private ViewGroup mLayoutRight;
    private ImageView mImageRight;
    private Drawable mRightDrawable;
    private TextView mTextRight;
    private String mRightText;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SettingItemView, defStyleAttr, defStyleAttr);
        int indexCount = typedArray.getIndexCount();
        for (int at = 0; at < indexCount; at++) {
            int index = typedArray.getIndex(at);
            switch (index) {
                case R.styleable.SettingItemView_leftDrawable:
                    try {
                        mLeftDrawable = typedArray.getDrawable(index);
                    } catch (Exception e) {
                        Log.e(TAG, "read left drawable exception: " + e.toString(), new Exception());
                    }
                    break;
                case R.styleable.SettingItemView_mainTitle:
                    mMainTitle = typedArray.getString(index);
                    break;
                case R.styleable.SettingItemView_subTitle:
                    mSubTitle = typedArray.getString(index);
                    break;
                case R.styleable.SettingItemView_rightText:
                    mRightText = typedArray.getString(index);
                    break;
                case R.styleable.SettingItemView_rightDrawable:
                    try {
                        mRightDrawable = typedArray.getDrawable(index);
                    } catch (Exception e) {
                        Log.e(TAG, "read right drawable exception: " + e.toString(), new Exception());
                    }
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();

        this.initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.setting_item_view, this);

        mImageLeft = (ImageView) findViewById(R.id.image_left);
        if (mLeftDrawable != null) {
            mImageLeft.setImageDrawable(mLeftDrawable);
            mImageLeft.setVisibility(VISIBLE);
        }

        mTextMainTitle = (TextView) findViewById(R.id.text_main_title);
        mTextMainTitle.setText(mMainTitle);
        mTextSubTitle = (TextView) findViewById(R.id.text_sub_title);
        if (!this.isEmpty(mSubTitle) && mLeftDrawable == null) {//标题配有图片时，不显示副标题
            mTextSubTitle.setText(mSubTitle);
            mTextSubTitle.setVisibility(VISIBLE);
        }

        mLayoutRight = (ViewGroup) findViewById(R.id.layout_right);
        mTextRight = (TextView) findViewById(R.id.text_right);
        if (!this.isEmpty(mRightText)) {
            mTextRight.setText(mRightText);
            mTextRight.setVisibility(VISIBLE);

            mLayoutRight.setVisibility(VISIBLE);
        }
        mImageRight = (ImageView) findViewById(R.id.image_right);
        if (mRightDrawable != null) {
            mImageRight.setImageDrawable(mRightDrawable);
            mImageRight.setVisibility(VISIBLE);

            mLayoutRight.setVisibility(VISIBLE);
        }

    }

    private boolean isEmpty(String text) {
        return text == null || "".equals(text.trim());
    }

}
