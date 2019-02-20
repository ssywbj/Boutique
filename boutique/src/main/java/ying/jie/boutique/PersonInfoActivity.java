package ying.jie.boutique;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ying.jie.adapter.PersonInfoAdapter;
import ying.jie.entity.PersonInfo;
import ying.jie.util.ToastUtil;

/**
 * Created by Weibj on 2016/1/3.
 */
public class PersonInfoActivity extends BasicActivity {
    private int[] images = {R.drawable.lv_person_andy,
            R.drawable.lv_person_turing, R.drawable.lv_person_bill,
            R.drawable.lv_person_edgar, R.drawable.lv_person_andy,
            R.drawable.lv_person_turing, R.drawable.lv_person_bill,
            R.drawable.lv_person_wbj};
    private int[] imageDescs = {R.string.andy, R.string.turing, R.string.bill,
            R.string.edgar, R.string.andy, R.string.turing, R.string.bill,
            R.string.wbj};
    private String[] mails = {"1271829012@qq.com", "fafafdafdfd532@sina.com",
            "wetafafadfada@126.com", "1520df4erefaf@163.com",
            "weibangjie309@163.com", "fafafdafdfd532@sina.com",
            "fafafdafdfd532@sina.com", "1271829012@qq.com"};
    private float[] ratings = {4.5f, 2.0f, 3.5f, 3.0f, 4f, 3.5f, 3.0f, 5.0f};
    private String[] tels = {"18819059959", "13768131341", "18776868237",
            "15949309305", "13299626255", "13768131341", "13768131341",
            "18819059959"};
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }


    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.person_info_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mListView = (ListView) findViewById(R.id.person_info_lv);
        List<PersonInfo> dataList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            dataList.add(new PersonInfo(mails[i], imageDescs[i], images[i], ratings[i], tels[i]));
        }
        mListView.setAdapter(new PersonInfoAdapter(this, dataList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(String.valueOf(position));
            }
        });
    }

}
