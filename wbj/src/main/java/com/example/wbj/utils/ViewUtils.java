package com.example.wbj.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

public final class ViewUtils {

    private ViewUtils() {
    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取底部导航栏的高度
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 判断是否存在底部导航栏
     */
    public static boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier != 0) {
            boolean hasNavigationBar = resources.getBoolean(identifier);

            //获取底部导航栏被重写的状态
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method method = c.getDeclaredMethod("get", String.class);
                method.setAccessible(true);
                String overrideStatus = (String) method.invoke(null, "qemu.hw.mainkeys");
                if ("1".equals(overrideStatus)) { //注意：不能写成“if...else...”判断形式
                    hasNavigationBar = false;
                } else if ("0".equals(overrideStatus)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hasNavigationBar;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    private static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        int[] colors = new int[]{color};
        int[][] states = new int[1][];
        states[0] = new int[]{};

        ColorStateList colorList = new ColorStateList(states, colors);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], drawable);

        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();

        DrawableCompat.setTintList(drawable, colorList);

        return drawable;
    }
}
