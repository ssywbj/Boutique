package com.example.wbj.picture.browser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wbj.R;
import com.example.wbj.info.ImageInfo;

import java.util.List;

public class DateSortAdapter extends PictureAdapter<ImageInfo> {
    private ImageBrowserActivity mActivity;

    public DateSortAdapter(ImageBrowserActivity activity, List<ImageInfo> dataList) {
        super(dataList);
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTENT) {
            return new ContentHolder(getItemLayout(parent.getContext(), R.layout.compress_aty_adapter_content));
        } else if (viewType == VIEW_TYPE_TITLE) {
            return new TitleHolder(getItemLayout(parent.getContext(), R.layout.compress_aty_adapter_title));
        } else {
            return new FooterHolder(getItemLayout(parent.getContext(), R.layout.compress_aty_adapter_footer));
        }
    }

    @Override
    protected void bindView(final RecyclerView.ViewHolder viewHolder, final int position, ImageInfo data) {
        if (mActivity == null) {
            return;
        }
        if (viewHolder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) viewHolder;
            Object tag = contentHolder.ivShowImage.getTag(R.id.glide_item_tag_key);
            if (data.getPath() != null && !data.getPath().equals(tag)) {
                contentHolder.ivShowImage.setTag(R.id.glide_item_tag_key, data.getPath());
                Glide.with(mActivity).load(data.getPath())/*.placeholder(R.color.main_menu_frg_bg)
                            .error(R.color.colorPrimaryDark)*/.into(contentHolder.ivShowImage);
            }

            if (mActivity.mIsEditMode) {
                contentHolder.cBox.setVisibility(View.VISIBLE);
                contentHolder.cBox.setChecked(mActivity.mSelectedList.contains(data));
                contentHolder.cBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.itemView.performClick();
                    }
                });
            } else {
                contentHolder.cBox.setVisibility(View.GONE);
            }
        } else if (viewHolder instanceof FooterHolder) {
            ((FooterHolder) viewHolder).tvShowNumber.setText(mActivity.getString(R.string.picture_bottom_number, data.getContentLength()));
            viewHolder.itemView.setOnClickListener(null);
        } else if ((viewHolder instanceof TitleHolder)) {
            ((TitleHolder) viewHolder).tvTitle.setText(data.getTitle());
        }
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        ImageView ivShowImage;
        CheckBox cBox;

        public ContentHolder(View view) {
            super(view);
            ivShowImage = (ImageView) view.findViewById(R.id.image_item);
            if (mActivity != null) {
                ViewGroup.LayoutParams layoutParams = ivShowImage.getLayoutParams();
                layoutParams.width = mActivity.mItemWidth;
                layoutParams.height = mActivity.mItemWidth;
                ivShowImage.setLayoutParams(layoutParams);
            }

            cBox = (CheckBox) view.findViewById(R.id.cb_select);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        TextView tvShowNumber;

        public FooterHolder(View view) {
            super(view);
            tvShowNumber = (TextView) view.findViewById(R.id.tv_show_number);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public TitleHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
