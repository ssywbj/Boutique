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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RecyclerViewActivity extends AppCompatActivity {
    public static final String TAG = RecyclerViewActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TempData> mOriginData = new ArrayList<>();
    private List<TempData> mComparedData = new ArrayList<>();
    private Map<Long, TempData> mMapData = new HashMap<>();

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
        mRecyclerView = (RecyclerView) findViewById(R.id.view_recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);// 设置布局管理器
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);// 设置adapter
    }


    private void getData() {
        TempData tempData;
        for (char i = 65; i <= 70; i++) {//ABCDEF
            tempData = new TempData(i, i + "");
            mOriginData.add(tempData);
            mMapData.put(tempData.id, tempData);
        }

        mComparedData.add(mOriginData.get(2));//CEDAFB
        mComparedData.add(mOriginData.get(4));
        mComparedData.add(mOriginData.get(3));
        mComparedData.add(mOriginData.get(0));
        mComparedData.add(mOriginData.get(5));
        mComparedData.add(mOriginData.get(1));
    }

    public void addItem(View view) {
        for (int k = 0; k < 10; k++) {


            int count0 = 0, count1 = count0, number = count0, maxLoop = 21, predict, newScale = 4, buy, success = 0;
            //思想：第一次买最后一次的开奖记录，以后每不中一次奖就买刚刚的开奖记录
            Random random = new Random();
            for (int j = 0; j < 6; j++) {
                number = random.nextInt(2);
                //Log.d(TAG, "历史局 = " + number);
            }
            buy = number;
            //Log.d(TAG, "===============预热结束==============");

            int predictSuccess = 0, predictFail = predictSuccess;
            //for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < maxLoop - 1; i++) {
                number = random.nextInt(2);
                if (number == 0) {
                    //Log.d(TAG, "结束局 = " + number);
                    count0++;
                } else {
                    count1++;
                    //Log.i(TAG, "结束局 = " + number);
                }
                if (number == buy) {
                    //Log.d(TAG, "中奖 = " + buy);
                    success++;
                } else {
                    //Log.i(TAG, "不中奖 = " + buy);
                    success--;
                }
                buy = number;
            }
            Log.i(TAG, "最后 = " + success);
        }
        //double rate0 = new BigDecimal(1.0 * count0 / maxLoop).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        //double rate1 = new BigDecimal(1.0 * count1 / maxLoop).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        //Log.d(TAG, "count0 = " + count0 + ", rate0 = " + rate0 + "; " + "count1 = " + count1 + ", rate1 = " + rate1);

        /*if (rate0 < rate1) {
            predict = 0;
        } else {
            predict = 1;
        }
        number = random.nextInt(2);
        if (number == 0) {
            count0++;
        } else {
            count1++;
        }
        rate0 = new BigDecimal(1.0 * count0 / maxLoop).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        rate1 = new BigDecimal(1.0 * count1 / maxLoop).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (predict == number) {
            //Log.d(TAG, "预计成功 = " + predict + ", 实际 = " + number + "; " + "count0 = " + count0 + ", rate0 = " + rate0 + "; " + "count1 = " + count1 + ", rate1 = " + rate1);
            predictSuccess++;
        } else {
            //Log.i(TAG, "预计失败 = " + predict + ", 实际 = " + number + "; " + "count0 = " + count0 + ", rate0 = " + rate0 + "; " + "count1 = " + count1 + ", rate1 = " + rate1);
            predictFail++;
        }
        //}*/

        //Log.d(TAG, "预计成功次数 = " + predictSuccess + ", 预计失败次数 = " + predictFail);

        /*mAdapter.getData().add(new TempData(new Date().getTime(), ((char) mAdapter.getData().size() + 1) + ""));
        mAdapter.notifyDataSetChanged();*/
    }

    public void ChangePst(View view) {
        TempData originData;
        int originPst, newPst;
        int sortCount = 0;
        for (TempData item : mComparedData) {//ABCDEF-->CEDAFB(中间过程：CABDEF-->CEABDF-->CEDABF-->CEDAFB)：以新队列(CEDAFB)的顺序为准
            originData = mMapData.get(item.id);//取出原数据
            if (originData != null) {
                originPst = mOriginData.indexOf(originData);
                newPst = mComparedData.indexOf(item);
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

        public TempData(long id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public String toString() {
            return "TempData{" + "id=" + id + ", title='" + title + '\'' + '}';
        }
    }

    private class MyComparator implements Comparator<TempData> {

        @Override
        public int compare(TempData o1, TempData o2) {
            /*if (o1.pst > o2.pst) {
                return 1;
            } else if (o1.pst < o2.pst) {
                return -1;
            } else {
                return 0;
            }*/
            return 0;
        }

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public List<TempData> getData() {
            return mOriginData;
        }

        public MyAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_aty_adt, parent, false); // 实例化展示的view
            ViewHolder viewHolder = new ViewHolder(v); // 实例化viewholder
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTv.setText(mOriginData.get(position).title);// 绑定数据
        }

        @Override
        public int getItemCount() {
            return mOriginData == null ? 0 : mOriginData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mTv = (TextView) itemView.findViewById(R.id.item_tv);
            }
        }
    }


    private class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
        /**
         * 用于绘制间隔样式
         */
        private Drawable mDivider;
        /**
         * 列表的方向，水平/竖直
         */
        private int mOrientation;


        public MyDividerItemDecoration(Context context, int orientation) {
            // 获取默认主题的属性
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
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
