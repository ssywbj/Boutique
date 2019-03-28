package com.suheng.ssy.boutique;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions//注解在需要调用运行时权限的Activity或Fragment上(必须使用的注解)
public class PermissionApplyActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_apply);
    }

    public void onClickWriteExternalStorage(View view) {
        PermissionApplyActivityPermissionsDispatcher.requestExternalStoragePermissionWithCheck(this, -1);
    }

    public void onClickReadExternalStorage(View view) {
        //PermissionApplyActivityPermissionsDispatcher.readExternalStoragePermissionWithCheck(this);
    }

    @Override//必须覆写此方法
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionApplyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /*危险权限都属于权限组，应用在向用户申请危险权限时，系统会弹对出话框描述应用要访问的权限组。用户如果同意授权，则权限组包含
    的所有权限都会被系统授予。比如应用被授予READ_EXTERNAL_STORAGE权限之后，如果再申请WRITE_EXTERNAL_STORAGE权限，系统会立即授予该权限*/
    //获取多个权限，注解在需要调用运行时权限的方法上，当用户给予权限时会执行该方法（必须使用的注解）
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void requestExternalStoragePermission(int requestCode) {//如果没有权限，会弹出系统要求申请相应权限的对话框，点击允许后执行该方法；如果已经有权限，则直接执行该方法
        Log.d(mTag, "requestExternalStoragePermission, thread: " + Thread.currentThread().getName() + ", requestCode: " + requestCode);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void deniedExternalStoragePermission() {//点击拒绝后执行该方法
        Log.d(mTag, "deniedExternalStoragePermission" );
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void neverAskAgainExternalStoragePermission() {//点击不再询问后执行的方法
        Log.d(mTag, "neverAskAgainExternalStoragePermission");
    }

    //一般用于展示点击取消后向用户说明原因，在确定按钮上监听执行request.proceed()方法，在取消按钮上监听执行request.cancel()方法
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showRationaleExternalStoragePermission(final PermissionRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("存取权限用于上传和下载文件；\n如果没有该权限则会影响App的使用！");
        builder.setPositiveButton("我明白了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.proceed();//继续往下申请的流程
            }
        });
        builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.cancel();//取消申请
            }
        });
        builder.create().show();
    }
}
