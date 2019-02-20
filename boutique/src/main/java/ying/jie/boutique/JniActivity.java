package ying.jie.boutique;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import ying.jie.jni.Jni;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;
import ying.jie.widget.DialogConfirm;

/**
 * Created by Wbj on 2016/2/19.
 */
public class JniActivity extends BasicActivity {
    private static Context mContext;
    private Jni mJni = new Jni();

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
        return R.layout.jni_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mContext = JniActivity.this;
        findViewById(R.id.text_call_cpp).setOnClickListener(this);
        findViewById(R.id.text_call_java_static).setOnClickListener(this);
        findViewById(R.id.text_call_java_instance).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_call_cpp) {
            ToastUtil.showToast(mJni.getCString() + mJni.getCInt());
        } else if (i == R.id.text_call_java_static) {
            try {
                // C/C++调用静态方法：responseInfo(String, String)后返回的状态码
                int status = mJni.getStatus("Static(From C/C++)",
                        "C/C++代码调用Java类中的静态方法(From C/C++)");
                ToastUtil.showToast("status::" + status);
            } catch (Exception e) {
                LogUtil.e(mLogTag, e.toString());
            }
        } else if (i == R.id.text_call_java_instance) {
            try {
                // C/C++调用responseInfo(String)后返回的状态码
                int responseCode = mJni
                        .getResponseCode("Normal Method(From C/C++)");
                ToastUtil.showToast("responseCode::" + responseCode);
            } catch (Exception e) {
                LogUtil.e(mLogTag, e.toString());
            }
        }
    }

    /**
     * 该静态函数用于提供给C/C++代码直接调用
     *
     * @return status：状态码
     */
    public static int responseInfo(String title, String Message) {
        new DialogConfirm(mContext, title, Message, "", "").show();
        return 1;
    }

    /**
     * 该函数用于提供给C/C++代码调用
     */
    public void responseInfo(String title) {
        responseInfo(title,
                "C/C++代码调用Java类中的普通方法，该普通方法的对访问权限没有要求!!(From C/C++)");
    }

}
