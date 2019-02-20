package ying.jie.boutique;

import android.os.Bundle;
import android.view.View;

import ying.jie.util.ActivityCollector;
import ying.jie.util.LogUtil;
import ying.jie.widget.DialogAd;

public class MoreActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(R.string.more);
        super.setRightBtn1Text(R.string.close);
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.gson_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mDialogAd = new DialogAd(this);
    }

    @Override
    public void clickLeftBtn(View view) {
        finish();
    }

    @Override
    public void clickRightBtn(View view) {
        finish();
    }

    private DialogAd mDialogAd;

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.globalLogInfo(mLogTag + "::onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mDialogAd != null) {
//            if (mDialogAd.isShowing()) {
//                mDialogAd.dismiss();
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.globalLogInfo(mLogTag + "::onDestroy()");
        if (mDialogAd != null) {
            if (mDialogAd.isShowing()) {
                mDialogAd.dismiss();
            }
            mDialogAd = null;
        }

        ActivityCollector.finishAllActivity();
    }

}
