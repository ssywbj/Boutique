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
import java.util.List;

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
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.main_picture_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_COLUMNS, GridLayoutManager.VERTICAL, false));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));//这里用线性宫格显示 类似于瀑布流
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mRecyclerView.addItemDecoration(new DividerDecoration(this, true));//设置Item分隔线
        //mPictureAdapter = new NormalAdapter(this, mDataList);
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
                        setSelectedItem(position);
                    } else {

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
            this.setSelectedItem(pst);
        }
    }

    private void exitEditMode() {
        mIsEditMode = false;
        mSelectedList.clear();
        mTitleBar.exitEditMode();
        mPictureAdapter.notifyDataSetChanged();
    }

    private void setSelectedItem(int pst) {
        if (pst < 0 || pst >= mDataList.size() || mDataList.get(pst).getItemType() != PictureAdapter.VIEW_TYPE_CONTENT) {
            return;
        }

        ImageInfo imageInfo = mDataList.get(pst);
        if (mSelectedList.contains(imageInfo)) {
            mSelectedList.remove(imageInfo);
        } else {
            mSelectedList.add(imageInfo);
        }

        mTitleBar.setSelectedCount(mSelectedList.size(), ImageInfo.getImageInfo().getContentLength());

        mPictureAdapter.notifyDataSetChanged();
    }

    private void setSelectedAll(boolean isSelectedAll) {
        mSelectedList.clear();
        if (isSelectedAll) {
            for (ImageInfo imageInfo : mDataList) {
                if (imageInfo.getItemType() == PictureAdapter.VIEW_TYPE_CONTENT) {
                    mSelectedList.add(imageInfo);
                }
            }
        }
        mTitleBar.setSelectedCount(mSelectedList.size(), ImageInfo.getImageInfo().getContentLength());
        mPictureAdapter.notifyDataSetChanged();
    }

    private ImageInfo mBatchLastItem;

    @Override
    public void onPostExecute(List<ImageInfo> imageInfoList) {
        if ((imageInfoList == null || imageInfoList.size() <= 0) && mDataList.size() <= 0) {
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
            ImageInfo imageSource, imageCompare;
            int nextIndex;
            for (int index = 0; index < contentLength; index++) {
                imageSource = imageInfoList.get(index);
                imageSource.setDate(DateUtils.formatDateTime(this, imageSource.getDateModified(), DateUtils.FORMAT_SHOW_YEAR));
                imageSource.setItemType(PictureAdapter.VIEW_TYPE_CONTENT);
                if (index == 0) {
                    if (mBatchLastItem == null) {
                        mDataList.add(new ImageInfo(imageSource.getDate(), PictureAdapter.VIEW_TYPE_TITLE));
                        mDataList.add(imageSource);
                    } else {//如果上一批的最后一个数据不为空，那么先和它比较是否是同一天
                        this.addInList(mBatchLastItem, imageSource);
                    }
                }

                Log.d(TAG, "index: " + index);
                nextIndex = index + 1;//和本批数据有下一项数据比较是否是同一天
                if (nextIndex < contentLength) {
                    imageCompare = imageInfoList.get(nextIndex);//取出下一项的值
                    imageCompare.setDate(DateUtils.formatDateTime(this, imageCompare.getDateModified(), DateUtils.FORMAT_SHOW_YEAR));
                    imageCompare.setItemType(PictureAdapter.VIEW_TYPE_CONTENT);
                    this.addInList(imageSource, imageCompare);

                    if (nextIndex == (contentLength - 1)) {//保存批量数据中最后一项的值，用于与下一个批量数据的第一项比较
                        mBatchLastItem = imageCompare;
                    }
                }
            }

            this.updateContentLength(contentLength);
        }

        mPictureAdapter.notifyDataSetChanged();
    }

    private void addInList(ImageInfo imageSource, ImageInfo imageCompare) {
        boolean isSameDay = DateUtil.isSameDay(imageSource.getDateModified(), imageCompare.getDateModified());
        Log.d(TAG, "is same day: " + isSameDay
                + ", " + imageSource.getDate() + "--" + imageCompare.getDate() +
                ", " + imageSource.getDateModified() + "--" + imageCompare.getDateModified());
        if (!isSameDay) {
            mDataList.add(new ImageInfo(imageCompare.getDate(), PictureAdapter.VIEW_TYPE_TITLE));
        }
        mDataList.add(imageCompare);
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
        ImageInfo.getImageInfo().setItemFooter(null);
        ImageInfo.getImageInfo().setContentLength(0);
        super.onDestroy();
    }

}
