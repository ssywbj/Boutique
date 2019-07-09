package com.suheng.ssy.boutique;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.suheng.pickpicture.PickerListener;
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
        PicturePicker.openAlbum(this, new PickerListener() {
            @Override
            public void obtainPicture(String path) {
                Log.d(TAG, "album picture: " + path);
                mViewAlbum.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }, 50 * 1024 * 1024);
    }

    public void openCamera(View view) {
        PicturePicker.openCamera(this, new PickerListener() {
            @Override
            public void obtainPicture(String path) {
                Log.d(TAG, "camera picture: " + path);
                mViewPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }, 1024 * 1024);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PicturePicker.releaseSource();
    }
}
