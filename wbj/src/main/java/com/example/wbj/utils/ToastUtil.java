package com.example.wbj.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.example.wbj.WbjApp;

public class ToastUtil {

    private static Toast toast;

    public static void shortShow(String text) {
        if (toast == null) {
            toast = Toast.makeText(WbjApp.getContext(), text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void shortShow(int textId) {
        shortShow(WbjApp.getContext().getResources().getString(textId));
    }

    public static void longShow(String text) {
        if (toast == null) {
            toast = Toast.makeText(WbjApp.getContext(), text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void longShow(int textId) {
        longShow(WbjApp.getContext().getResources().getString(textId));
    }

}
