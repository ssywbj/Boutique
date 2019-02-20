package com.example.wbj;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wbj.utils.FileUtil;
import com.wbj.view.CommonDialog;
import com.wbj.view.NumberDynamicView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberDynamicActivity extends AppCompatActivity {
    public static final String TAG = NumberDynamicActivity.class.getSimpleName();
    private NumberDynamicView mNumberDynamicView;

    private LinearLayout mLayoutLayout;
    private ViewPager mViewPager;
    private List<TextView> mViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_dynamic_aty);
        mNumberDynamicView = (NumberDynamicView) findViewById(R.id.number_dynamic_view);
        mNumberDynamicView.setNewNumber(new Random().nextInt(100));

        mLayoutLayout = (LinearLayout) findViewById(R.id.layout_label);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        int max = 4;
        for (int index = 0; index < max; index++) {
            TextView textView = new TextView(this);
            textView.setText("Text" + (index + 1));
            textView.setTextSize(20f);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLayoutLayout.removeView(view);
                    mViewList.remove(view.getTag());
                    setPagerAdapter();
                }
            });
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            mLayoutLayout.addView(textView, index, new LinearLayout.LayoutParams(0
                    , LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            TextView textPager = new TextView(this);
            textPager.setText("Page" + (index + 1));
            textPager.setTextSize(23f);
            textPager.setGravity(Gravity.CENTER);
            textPager.setTextColor(getResources().getColor(R.color.colorAccent));
            textView.setTag(textPager);
            mViewList.add(textPager);
        }

        this.setPagerAdapter();

        try {
            AssetManager assetManager = getAssets();
            String encodeFileDir = "encode";
            String[] files = assetManager.list(encodeFileDir);
            for (String fileName : files) {
                //File.pathSeparator：多个路径字符串的路径分隔符，如:java -cp test.jar;abc.jar，值为“;”
                //File.separator：同一个路径字符串的目录分隔符，如：C:\Programs\Files，值为“\”
                String fileEncode = FileUtil.getCharsetName(assetManager.open(encodeFileDir + File.separator + fileName));
                Log.d(TAG, "file encode: " + fileEncode);
                Log.d(TAG, "file content: " + FileUtil.readString(assetManager.open(encodeFileDir + File.separator + fileName)));
            }
        } catch (Exception e) {
            Log.e(TAG, "get encode files exception: " + e.toString());
        }

        CommonDialog commonDialog = new CommonDialog(this, "标题", "内容内容内容内容内容内容内容内容" +
                "内容内容内容内容内容内容内容内容内容内容内容内容", "", "我知道了");
        commonDialog.setOnClickDialog(new CommonDialog.OnClickDialog() {
            @Override
            public void clickLeftBtn() {
            }

            @Override
            public void clickRightBtn() {
                Toast.makeText(NumberDynamicActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
        commonDialog.show();
    }

    private void setPagerAdapter() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public View instantiateItem(ViewGroup container, int position) {
                View view = mViewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }

    public void clickRefresh(View view) {
        mNumberDynamicView.setNewNumber(new Random().nextInt(100));
    }

}
