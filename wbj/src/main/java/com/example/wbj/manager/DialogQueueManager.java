package com.example.wbj.manager;

import android.content.DialogInterface;
import android.util.Log;

import com.example.wbj.basic.BasicDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogQueueManager {
    public static final String TAG = DialogQueueManager.class.getSimpleName();
    public static final int PRIORITY_NO = 5;
    public static final int PRIORITY_1 = PRIORITY_NO - 1;
    public static final int PRIORITY_2 = PRIORITY_1 - 1;
    public static final int PRIORITY_3 = PRIORITY_2 - 1;
    public static final int PRIORITY_4 = PRIORITY_3 - 1;
    private static List<BasicDialog> sDialogList = new ArrayList<>();

    private static void addDialog(BasicDialog dialog) {
        if (dialog == null || dialog.getPriority() >= PRIORITY_NO) {
            return;
        }
        Log.w(TAG, "add dialog = " + dialog);
        if (sDialogList.size() == 0) {
            dialog.show();
        } else {
            BasicDialog currentDialog = sDialogList.get(0);
            //Log.i(TAG, "compare: " + currentDialog.getPriority() + ", " + dialog.getPriority());
            if (currentDialog.getPriority() < dialog.getPriority()) {
                /*currentDialog.dismiss();
                sDialogList.add(currentDialog);*/
                dialog.show();
            } /*else if (currentDialog.getPriority() == dialog.getPriority()) {
                dialog.show();
                dialog.setOnDismissListener(new DialogDismissLister(dialog));
            }*/
        }

        if (!sDialogList.contains(dialog)) {
            sDialogList.add(dialog);
        }
        Collections.sort(sDialogList);
        /*for (BasicDialog basicDialog : sDialogList) {
            Log.e(TAG, "list dialog = " + basicDialog);
        }
        Log.e(TAG, "list dialog = " + sDialogList.size());*/
    }

    private static void removeDialog(BasicDialog dialog) {
        Log.w(TAG, "remove dialog = " + dialog);
        if (dialog != null && sDialogList.contains(dialog)) {
            sDialogList.remove(dialog);
        }
    }

    public static void show(BasicDialog dialog) {
        if (dialog == null) {
            return;
        }
        if (dialog.getPriority() >= PRIORITY_NO) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } else {
            addDialog(dialog);
        }
    }

    public static void clearDialogList() {
        for (BasicDialog dialog : sDialogList) {
            if (dialog.isShowing()) {
                dialog.setOnDismissListener(null);
                dialog.dismiss();
            }
        }
        sDialogList.clear();
        //Log.w(TAG, "clear all dialogs = " + sDialogList);
    }

    public static class DialogDismissLister implements DialogInterface.OnDismissListener {
        private BasicDialog mBasicDialog;

        public DialogDismissLister(BasicDialog basicDialog) {
            mBasicDialog = basicDialog;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            removeDialog(mBasicDialog);
            //Log.e(TAG, "remove after size = " + sDialogList.size());
            /*for (BasicDialog basicDialog : sDialogList) {
                Log.e(TAG, "remove after dialog = " + basicDialog);
            }*/

            if (sDialogList.size() <= 0) {
                return;
            }
            BasicDialog basicDialog = sDialogList.get(0);
            if (!basicDialog.isShowing()) {
                basicDialog.show();
                //Log.d(TAG, "dismiss after show = " + sDialogList.get(0));
            }
        }
    }

}
