package com.suheng.ssy.boutique;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.suheng.pickpicture.PicturePicker;

public class PickPictureActivity extends BasicActivity {
    private static final String TAG = "PicturePicker";
    private ImageView mViewAlbum;
    private ImageView mViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_picture_aty);
        mViewAlbum = findViewById(R.id.image_from_album);
        mViewPhoto = findViewById(R.id.image_from_camera);
    }

    public void openAlbum(View view) {
        /*PicturePicker.openAlbum(this, new PickerListener() {
            @Override
            public void obtainPicture(String path) {
                Log.d(TAG, "album picture: " + path);
                mViewAlbum.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }, 50 * 1024 * 1024);*/
        this.openGaodeMap(39.945136, 116.346983, "北京动物园");
    }

    public void openCamera(View view) {
        /*PicturePicker.openCamera(this, new PickerListener() {
            @Override
            public void obtainPicture(String path) {
                Log.d(TAG, "camera picture: " + path);
                mViewPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }, 1024 * 1024);*/

        this.openBaiduMap(39.945136, 116.346983, "北京动物园");
    }

    public void openTencentMap(View view) {
        this.openTencentMap(39.945136, 116.346983, "北京动物园");
    }

    /**
     * 打开百度地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     */
    private void openBaiduMap(double dlat, double dlon, String dname) {
        if (this.checkAppExist("com.baidu.BaiduMap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                    + dname + "|latlng:" + dlat + "," + dlon + "&mode=driving" + "&src=" + getPackageName()));
            startActivity(intent);
        } else {
            Log.d(TAG, "Baidu map is not install");
        }
    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     */
    private void openGaodeMap(double dlat, double dlon, String dname) {
        if (this.checkAppExist("com.autonavi.minimap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                    + "&sname=我的位置&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dname + "&dev=0&t=0"));
            startActivity(intent);
        } else {
            Log.d(TAG, "Gaode map is not install");
        }
    }

    /**
     * 打开腾讯地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * 公交：type=bus，policy有以下取值
     * 0：较快捷 、 1：少换乘 、 2：少步行 、 3：不坐地铁
     * 驾车：type=drive，policy有以下取值
     * 0：较快捷 、 1：无高速 、 2：距离短
     * policy的取值缺省为0
     */
    private void openTencentMap(double dlat, double dlon, String dname) {
        if (this.checkAppExist("com.tencent.map")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&from=我的位置" + "&to=" + dname
                    + "&tocoord=" + dlat + "," + dlon + "&policy=0&referer=" + R.string.app_name));
            startActivity(intent);
        } else {
            Log.d(TAG, "Tencent map is not install");
        }
    }

    private boolean checkAppExist(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PicturePicker.releaseSource();
    }
}
