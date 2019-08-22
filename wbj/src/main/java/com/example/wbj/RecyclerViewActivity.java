package com.example.wbj;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    public static final String TAG = RecyclerViewActivity.class.getSimpleName();
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TempData> mListOrigin = new ArrayList<>();
    private List<TempData> mListCompared = new ArrayList<>();
    private LongSparseArray<TempData> mMapData = new LongSparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_aty);
        initData();
        initView();
    }

    private void initData() {
        this.getData();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyAdapter();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.view_recycler);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    private void getData() {
        TempData tempData;
        for (char i = 65; i <= 70; i++) {//ABCDEF
            tempData = new TempData(i, i + "");
            mListOrigin.add(tempData);
            mMapData.put(tempData.id, tempData);
        }

        mListCompared.add(mListOrigin.get(2));//CEDAFB
        mListCompared.add(mListOrigin.get(4));
        mListCompared.add(mListOrigin.get(3));
        mListCompared.add(mListOrigin.get(0));
        mListCompared.add(mListOrigin.get(5));
        mListCompared.add(mListOrigin.get(1));
    }

    public void addItem(View view) {
        mAdapter.getData().add(new TempData(new Date().getTime(), ((char) mAdapter.getData().size() + 1) + ""));
        mAdapter.notifyDataSetChanged();
    }

    public void ChangePst(View view) {
        TempData originData;
        int originPst, newPst;
        int sortCount = 0;
        for (TempData item : mListCompared) {//ABCDEF-->CEDAFB(中间过程：CABDEF-->CEABDF-->CEDABF-->CEDAFB)：以新队列(CEDAFB)的顺序为准
            originData = mMapData.get(item.id);//取出原数据
            if (originData != null) {
                originPst = mListOrigin.indexOf(originData);
                newPst = mListCompared.indexOf(item);
                if (originPst != newPst) {
                    Log.i(TAG, "原队列：" + this.printList(mAdapter.getData()));
                    mAdapter.notifyItemMoved(originPst, newPst);
                    mAdapter.getData().remove(originData);
                    mAdapter.getData().add(newPst, originData);
                    Log.d(TAG, "新队列：" + this.printList(mAdapter.getData()) + ", sortCount = " + (++sortCount) + "-->originPst: " + originPst + ", newPst: " + newPst);
                }
            }
        }
    }

    private String printList(List<TempData> tempDataList) {
        StringBuilder sBuilder = new StringBuilder();
        for (TempData tempData : tempDataList) {
            sBuilder.append(tempData.title).append("-");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    private class TempData {
        private long id;
        private String title;

        TempData(long id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public String toString() {
            return "TempData{" + "id=" + id + ", title='" + title + '\'' + '}';
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public List<TempData> getData() {
            return mListOrigin;
        }

        MyAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_aty_adt, parent, false); // 实例化展示的view
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTv.setText(mListOrigin.get(position).title);// 绑定数据
        }

        @Override
        public int getItemCount() {
            return mListOrigin == null ? 0 : mListOrigin.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTv;

            ViewHolder(View itemView) {
                super(itemView);
                mTv = itemView.findViewById(R.id.item_tv);
            }
        }
    }


    private class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
        static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
        /**
         * 用于绘制间隔样式
         */
        private Drawable mDivider;
        /**
         * 列表的方向，水平/竖直
         */
        private int mOrientation;


        MyDividerItemDecoration(Context context, int orientation) {
            // 获取默认主题的属性
            final TypedArray a = context.obtainStyledAttributes(new int[]{
                    android.R.attr.listDivider
            });
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            // 绘制间隔
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }

        private void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        /**
         * 绘制间隔
         */
        private void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        /**
         * 绘制间隔
         */
        private void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin +
                        Math.round(ViewCompat.getTranslationX(child));
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
