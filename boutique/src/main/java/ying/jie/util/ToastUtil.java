package ying.jie.util;

import android.view.Gravity;
import android.widget.Toast;

import ying.jie.boutique.App;

public class ToastUtil {

    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getContext(), text,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(int textId) {
        String text = App.getContext().getResources().getString(textId);
        showToast(text);
    }

}
