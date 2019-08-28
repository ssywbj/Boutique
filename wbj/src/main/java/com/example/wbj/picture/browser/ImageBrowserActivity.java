package com.example.wbj.picture.browser;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.wbj.R;
import com.example.wbj.basic.BasicActivity;
import com.example.wbj.basic.RecyclerAdapter;
import com.example.wbj.info.ImageInfo;
import com.example.wbj.info.ScreenInfo;
import com.example.wbj.mvp.model.QueryTask;
import com.example.wbj.utils.DateUtil;
import com.example.wbj.utils.ToastUtil;
import com.wbj.view.DividerDecoration;
import com.wbj.view.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageBrowserActivity extends BasicActivity implements QueryTask.TaskResultListener {
    private static final String TAG = ImageBrowserActivity.class.getSimpleName();
    public static final int DADA_OBTAIN_BATCH = 90;
    private static final int NUM_COLUMNS = 4;
    private TitleBar mTitleBar;
    private View mGridEmptyView;
    protected List<ImageInfo> mDataList, mSelectedList;
    protected int mItemWidth = 280;
    private int mLastVisibleItemPosition;
    private RecyclerView mRecyclerView;
    private PictureAdapter mPictureAdapter;
    private boolean mHasLoadCompleted;
    protected boolean mIsEditMode;
    private Map<String, List<ImageInfo>> mMapDateImage = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compress_aty);

        mGridEmptyView = findViewById(R.id.grid_empty_view);

        mDataList = Collections.synchronizedList(new ArrayList<ImageInfo>());
        mSelectedList = Collections.synchronizedList(new ArrayList<ImageInfo>());

        this.initTitleBar();
        this.initRecyclerView();
    }

    private void initTitleBar() {
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setLRRightBtn(R.drawable.icon_sort_btn);
        mTitleBar.setLRMiddleBtn(R.drawable.icon_edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterEditMode(-1);
            }
        });
        mTitleBar.setExitEditModeListener(new TitleBar.ExitEditModeListener(mTitleBar) {
            @Override
            public void exitEditMode() {
                ImageBrowserActivity.this.exitEditMode();
            }
        });

        mTitleBar.setEditSelectedAllListener(new TitleBar.EditSelectedAllListener(mTitleBar) {
            @Override
            public void setSelectedAll(boolean isSelectedAll) {
                ImageBrowserActivity.this.setSelectedAll(isSelectedAll);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.main_picture_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMNS, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mRecyclerView.addItemDecoration(new DividerDecoration(this, true));//设置Item分隔线
        mPictureAdapter = new DateSortAdapter(this, mDataList);
        mPictureAdapter.setHasStableIds(true);//解决adapter闪烁问题
        mRecyclerView.setAdapter(mPictureAdapter);

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果是FooterView或TitleView则占所有列，否则只占自己列
                    return (mPictureAdapter.getItemViewType(position) == PictureAdapter.VIEW_TYPE_FOOTER)
                            || (mPictureAdapter.getItemViewType(position) == PictureAdapter.VIEW_TYPE_TITLE)
                            ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, lastVisibleItemPosition: " + mLastVisibleItemPosition);
                    if (!mIsEditMode && !mHasLoadCompleted && (mLastVisibleItemPosition >= mPictureAdapter.getItemCount() - 1)) {
                        Log.d(TAG, "load , load ,load ");
                        initQueryTask();
                    }
                }
            }
        });

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics realMetrics = ScreenInfo.getScreenInfo().getRealMetrics();
                mItemWidth = (realMetrics.widthPixels - mRecyclerView.getPaddingLeft() - mRecyclerView.getPaddingRight() -
                        (NUM_COLUMNS - 1) * DividerDecoration.LIST_DIVIDER_DIMEN) / NUM_COLUMNS;
                Log.i(TAG, " mItemWidth: " + mItemWidth);

                initQueryTask();
            }
        });

        mPictureAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                ImageInfo imageInfo = (ImageInfo) mPictureAdapter.getItem(position);
                if (mIsEditMode) {
                    if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                        setSelectedItem(imageInfo);
                    } else if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_TITLE) {
                        final String infoTitle = imageInfo.getTitle();
                        if (mMapDateImage.containsKey(infoTitle)) {
                            setSelectedTitle(imageInfo);
                        }
                    }
                } else {
                    if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                        ToastUtil.shortShow(position + "_click");
                    }
                }
            }
        });

        mPictureAdapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position, long id) {
                ToastUtil.shortShow(position + "_long");
                enterEditMode(position);
                return true;
            }
        });
    }

    private void initQueryTask() {
        QueryTask queryTask = new QueryTask(this);
        queryTask.setTaskResultListener(this);
        queryTask.execute(ImageInfo.getImageInfo().getContentLength(), DADA_OBTAIN_BATCH);
    }

    private void enterEditMode(int pst) {
        mIsEditMode = true;

        mTitleBar.enterEditMode();

        if (pst < 0) {
            mTitleBar.setSelectedCount(mSelectedList.size(), ImageInfo.getImageInfo().getContentLength());
            mPictureAdapter.notifyDataSetChanged();
        } else {
            this.setSelectedItem(mDataList.get(pst));
        }
    }

    private void exitEditMode() {
        for (ImageInfo imageInfo : mSelectedList) {
            if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                Log.d(TAG, "selected item: " + imageInfo);
            }
        }
        mIsEditMode = false;
        mSelectedList.clear();
        mTitleBar.exitEditMode();
        mPictureAdapter.notifyDataSetChanged();
    }

    private void setSelectedItem(ImageInfo imageInfo) {
        if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_FOOTER) {
            return;
        }

        if (mSelectedList.contains(imageInfo)) {
            mSelectedList.remove(imageInfo);
        } else {
            mSelectedList.add(imageInfo);
        }

        int dateSelectedCount = 0;
        for (ImageInfo info : mSelectedList) {
            if (imageInfo.getDate().equals(info.getDate())) {
                ++dateSelectedCount;
            }
        }
        ImageInfo imageTitle = imageInfo.getImageTitle();
        if (mMapDateImage.containsKey(imageTitle.getTitle())) {
            if (mMapDateImage.get(imageTitle.getTitle()).size() == dateSelectedCount) {
                mSelectedList.add(imageTitle);
            } else {
                mSelectedList.remove(imageTitle);
            }
        }


        int selectedCount = 0;
        for (ImageInfo info : mSelectedList) {
            if (info.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                selectedCount++;
            }

        }
        mTitleBar.setSelectedCount(selectedCount, ImageInfo.getImageInfo().getContentLength());

        mPictureAdapter.notifyDataSetChanged();
    }

    private void setSelectedTitle(ImageInfo imageInfo) {
        if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_FOOTER) {
            return;
        }

        List<ImageInfo> imageInfos = mMapDateImage.get(imageInfo.getTitle());
        if (mSelectedList.contains(imageInfo)) {
            mSelectedList.remove(imageInfo);

            for (ImageInfo image : imageInfos) {
                mSelectedList.remove(image);
            }
        } else {
            mSelectedList.add(imageInfo);

            for (ImageInfo image : imageInfos) {
                if (!mSelectedList.contains(image)) {
                    mSelectedList.add(image);
                }
            }
        }

        int selectedCount = 0;
        for (ImageInfo info : mSelectedList) {
            if (info.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                selectedCount++;
            }

        }
        mTitleBar.setSelectedCount(selectedCount, ImageInfo.getImageInfo().getContentLength());

        mPictureAdapter.notifyDataSetChanged();
    }

    private void setSelectedAll(boolean isSelectedAll) {
        mSelectedList.clear();
        if (isSelectedAll) {
            for (ImageInfo imageInfo : mDataList) {
                if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_TITLE) {
                    setSelectedTitle(imageInfo);
                }
            }
        } else {
            mTitleBar.setSelectedCount(0, ImageInfo.getImageInfo().getContentLength());
            mPictureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostExecute(List<ImageInfo> imageInfoList) {
        if ((imageInfoList == null || imageInfoList.size() <= 0)) {
            mGridEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mGridEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mHasLoadCompleted = imageInfoList.size() < DADA_OBTAIN_BATCH;
        if (mHasLoadCompleted) {
            ToastUtil.shortShow("加载完毕！");
        }

        final int contentLength = imageInfoList.size();
        if (contentLength > 0) {
            boolean isNotSameDay;
            List<ImageInfo> dateImages = null;
            ImageInfo imageTitle = null;
            /*
             * 算法：在for循环中，如果Item集合为空，那么先加入日期Title项再加入第一条数据项；往后的数据项与
             * 前一项比较，如果是同一日期则直接加在集合后面，如果是不同日期则先加入日期Title项再加入数据项。
             */
            for (ImageInfo currentImage : imageInfoList) {
                currentImage.setDate(DateUtils.formatDateTime(this, currentImage.getDateModified(), DateUtils.FORMAT_SHOW_YEAR));
                if (mDataList.size() > 0) {
                    isNotSameDay = !DateUtil.isSameDay(mDataList.get(mDataList.size() - 1).getDateModified()
                            , currentImage.getDateModified());//mDataList.get(size - 1)：取出前一项数据
                } else {
                    isNotSameDay = true;
                }
                if (isNotSameDay) {//如果不是同一天
                    imageTitle = new ImageInfo(currentImage.getDate(), PictureAdapter.VIEW_TYPE_TITLE);
                    mDataList.add(imageTitle);//先加入日期Title项

                    dateImages = new ArrayList<>();
                    mMapDateImage.put(currentImage.getDate(), dateImages);
                }
                if (imageTitle != null) {
                    currentImage.setImageTitle(imageTitle);
                }
                mDataList.add(currentImage);//再加入数据项

                if (dateImages != null) {
                    dateImages.add(currentImage);
                }
            }

            this.updateContentLength(contentLength);
        } else {
            Log.d(TAG, "没有数据了");
        }

        mPictureAdapter.notifyDataSetChanged();
    }

    private void updateContentLength(int contentLength) {
        ImageInfo itemFooter = ImageInfo.getImageInfo().getItemFooter();
        if (itemFooter == null) {
            ImageInfo.getImageInfo().setItemFooter(new ImageInfo(contentLength, PictureAdapter.VIEW_TYPE_FOOTER));
        } else {
            ImageInfo.getImageInfo().getItemFooter().setContentLength(itemFooter.getContentLength() + contentLength);
        }

        itemFooter = ImageInfo.getImageInfo().getItemFooter();
        if (mDataList.contains(itemFooter)) {
            mDataList.remove(itemFooter);
        }
        mDataList.add(itemFooter);

        ImageInfo.getImageInfo().setContentLength(itemFooter.getContentLength());
    }

    @Override
    public void onBackPressed() {
        if (mIsEditMode) {
            this.exitEditMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mDataList.clear();
        mSelectedList.clear();
        mMapDateImage.clear();
        ImageInfo.getImageInfo().setItemFooter(null);
        ImageInfo.getImageInfo().setContentLength(0);
        super.onDestroy();
    }

}
