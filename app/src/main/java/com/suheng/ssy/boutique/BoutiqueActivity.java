package com.suheng.ssy.boutique;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.suheng.ssy.boutique.model.Constants;
import com.suheng.ssy.boutique.oom.MemoryLeakageActivity;

import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Map;

import ying.jie.boutique.MainActivity;

public class BoutiqueActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1
                , getResources().getStringArray(R.array.main_item)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                switch (i) {
                    case 0:
                        intent = new Intent(BoutiqueActivity.this, RegexActivity.class);
                        break;
                    case 1:
                        intent = new Intent(BoutiqueActivity.this, LaunchStandardActivity.class);
                        break;
                    case 2:
                        intent = new Intent(BoutiqueActivity.this, ConstraintLayoutActivity.class);
                        break;
                    case 3:
                        intent = new Intent(BoutiqueActivity.this, DataBindingActivity.class);
                        break;
                    case 4:
                        intent = new Intent(BoutiqueActivity.this, DaggerActivity.class);
                        break;
                    case 5:
                        intent = new Intent(BoutiqueActivity.this, FragmentRecyclerActivity.class);
                        break;
                    case 6:
                        //intent = new Intent(BoutiqueActivity.this, ARouterActivity.class);
                        ARouter.getInstance().build("/module2/activity_demo"/*"/module_b/activity_b"*/)//使用路由跳转
                                .withString("name", "Wbj").withInt("age", 22).navigation(BoutiqueActivity.this,
                                new NavCallback() {
                                    @Override
                                    public void onFound(Postcard postcard) {
                                        super.onFound(postcard);
                                        Log.d(mTag, "onFound, " + postcard.getGroup());
                                    }

                                    @Override
                                    public void onLost(Postcard postcard) {
                                        super.onLost(postcard);
                                        Log.d(mTag, "onLost, " + postcard.getGroup());
                                    }

                                    @Override
                                    public void onArrival(Postcard postcard) {
                                        Log.d(mTag, "onArrival, " + postcard.getGroup());
                                    }

                                    @Override
                                    public void onInterrupt(Postcard postcard) {
                                        super.onInterrupt(postcard);
                                        Log.d(mTag, "onInterrupt, " + postcard.getGroup());
                                    }
                                });
                        break;
                    case 7:
                        intent = new Intent(BoutiqueActivity.this, UnitTestActivity.class);
                        break;
                    case 8:
                        intent = new Intent(BoutiqueActivity.this, OkGoActivity.class);
                        break;
                    case 9:
                        intent = new Intent(BoutiqueActivity.this, MainActivity.class);
                        break;
                    case 10:
                        intent = new Intent(BoutiqueActivity.this, RxJava2Activity.class);
                        break;
                    case 11:
                        intent = new Intent(BoutiqueActivity.this, RoomActivity.class);
                        break;
                    case 12:
                        intent = new Intent(BoutiqueActivity.this, PermissionApplyActivity.class);
                        break;
                    case 13:
                        intent = new Intent(BoutiqueActivity.this, RedDotActivity.class);
                        break;
                    case 14:
                        ARouter.getInstance().build(Constants.ROUTER_APP_ACTIVITY_KEYBOARD).navigation();
                        break;
                    case 15:
                        intent = new Intent(BoutiqueActivity.this, UploadViewActivity.class);
                        break;
                    case 16:
                        intent = new Intent(BoutiqueActivity.this, MemoryLeakageActivity.class);
                        break;
                    case 17:
                        intent = new Intent(BoutiqueActivity.this, PickPictureActivity.class);
                        break;
                    default:
                        intent = new Intent(BoutiqueActivity.this, RegexActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        setContentView(listView);

        startActivity(new Intent(this, PickPictureActivity.class));
    }

    private static class ImageCache extends LruCache<String, Bitmap> {
        private Map<String, SoftReference<Bitmap>> mCacheMap;


        public ImageCache(Map<String, SoftReference<Bitmap>> cacheMap) {
            super((int) (Runtime.getRuntime().maxMemory() / 8));
            mCacheMap = cacheMap;
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if (oldValue != null) {
                SoftReference<Bitmap> softReference = new SoftReference<>(oldValue);
                mCacheMap.put(key, softReference);
            }
        }

        public Map<String, SoftReference<Bitmap>> getCacheMap() {
            return mCacheMap;
        }

    }

    HttpURLConnection mHttpURLConnection;
    URLConnection mURLConnection;
}
