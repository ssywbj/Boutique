package com.example.wbj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.wbj.utils.ToastUtil;

public class MVCLoginActivity extends AppCompatActivity {
    public static final String TAG = MVCLoginActivity.class.getSimpleName();
    private EditText mEditName, mEditPwd;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_aty);
        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);
    }

    public void clickBtnExit(View view) {
        finish();
    }

    public void clickBtnLogin(View view) {
        String name = mEditName.getText().toString().trim();
        String pwd = mEditPwd.getText().toString().trim();
        if (name.isEmpty()) {
            ToastUtil.shortShow("请输入账号");
            return;
        }
        if (pwd.isEmpty()) {
            ToastUtil.shortShow("请输入密码");
            return;
        }

        this.sendLoginRequest(name, pwd);//模拟请求操作
    }

    private void sendLoginRequest(final String name, final String pwd) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", "正在登录，请稍候", false, false);
        }
        mProgressDialog.show();

        new Handler().postDelayed(new Runnable() {//模拟网络请求
            @Override
            public void run() {
                if (!"Ssywbj".equals(name)) {
                    loginFail("账号不存在");
                    return;
                }
                if (!"123456".equals(pwd)) {
                    loginFail("密码不正确");
                    return;
                }

                if ("Ssywbj".equals(name) && "123456".equals(pwd)) {
                    loginSuccess();
                }
            }
        }, 2000);
    }

    private void loginSuccess() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        ToastUtil.shortShow("登录成功");
    }

    private void loginFail(String reason) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        ToastUtil.shortShow("登录失败：" + reason);
    }

}
