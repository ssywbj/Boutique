package com.example.wbj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.wbj.mvp.presenter.LoginPresenter;
import com.example.wbj.mvp.presenter.LoginPresenterImpl;
import com.example.wbj.mvp.view.LoginView;
import com.example.wbj.utils.ToastUtil;

public class MVPLoginActivity extends AppCompatActivity implements LoginView {
    public static final String TAG = MVPLoginActivity.class.getSimpleName();
    private EditText mEditName, mEditPwd;
    private ProgressDialog mProgressDialog;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_aty);

        mEditName = (EditText) findViewById(R.id.edit_name);
        mEditPwd = (EditText) findViewById(R.id.edit_pwd);

        mLoginPresenter = new LoginPresenterImpl(this);
    }

    public void clickBtnExit(View view) {
        finish();
    }

    public void clickBtnLogin(View view) {
        mLoginPresenter.validate(mEditName.getText().toString().trim(), mEditPwd.getText().toString().trim());
    }

    private void hideProgress() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void emptyName() {
        ToastUtil.shortShow("请输入用户名");
    }

    @Override
    public void emptyPwd() {
        ToastUtil.shortShow("请输入密码");
    }

    @Override
    public void loginSuccess() {
        this.hideProgress();
        ToastUtil.shortShow("登录成功");
    }

    @Override
    public void loginFail(String reason, int code) {
        this.hideProgress();
        ToastUtil.shortShow("登录失败：" + reason);
    }

    @Override
    public void loginProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", "正在登录，请稍候", false, false);
        }
        mProgressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.destroy();
    }

}
