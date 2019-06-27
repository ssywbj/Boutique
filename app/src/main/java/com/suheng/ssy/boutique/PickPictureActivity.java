package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.suheng.pickpicture.PickCallback;
import com.suheng.pickpicture.PickerListener;
import com.suheng.pickpicture.PicturePicker;

public class PickPictureActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_picture_aty);
    }

    public void openAlbum(View view) {
        /*PicturePicker.openAlbum(this, new PickCallback() {
            @Override
            public void obtainPicture(String path) {
                Log.d("WBJ", "choose picture = " + path);
            }
        });*/

        /*PicturePicker.openAlbum(this, new PickCallback() {
            @Override
            protected void obtainPicture(String path) {
                Log.d("WBJ", "choose picture = " + path);
            }
        });*/

        PicturePicker.openAlbum(this, new PickerListener() {
            @Override
            public void obtainPicture(String path) {
                Log.d("WBJ", "choose picture = " + path);
            }
        });
    }

}
