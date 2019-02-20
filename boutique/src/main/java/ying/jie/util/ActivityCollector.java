package ying.jie.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import ying.jie.boutique.App;

/**
 * Activity收集器用于保存已经打开但未关闭的Activity，方便退出运用时销毁所有未关闭Activity
 */
public class ActivityCollector {
    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);

    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);

    }

    public static void finishAllActivity() {
        for (Activity activity : activityList) {
            if (activity.isFinishing()) {
                activity.finish();
                LogUtil.i(App.TAG, "finish activity::" + activity);
            }
        }

        activityList.clear();
    }

}
