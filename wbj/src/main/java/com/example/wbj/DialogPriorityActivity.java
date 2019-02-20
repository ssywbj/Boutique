package com.example.wbj;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.wbj.basic.BasicDialog;
import com.example.wbj.manager.DialogQueueManager;
import com.wbj.view.CommonDialog;

public class DialogPriorityActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_priority_aty);

        findViewById(R.id.btn_priority_1).setOnClickListener(this);
        findViewById(R.id.btn_priority_2).setOnClickListener(this);
        findViewById(R.id.btn_priority_3).setOnClickListener(this);
        findViewById(R.id.btn_priority_4).setOnClickListener(this);

        CommonDialog commonDialog = new CommonDialog(this, "", "内容内容内容内容", "我知道了", "");
        commonDialog.setOnClickDialog(new CommonDialog.OnClickDialog() {
            @Override
            public void clickLeftBtn() {
                Toast.makeText(DialogPriorityActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickRightBtn() {
            }
        });
        commonDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogQueueManager.clearDialogList();
    }

    @Override
    public void onClick(View v) {
        TempDialog tempDialog = null;

        switch (v.getId()) {
            case R.id.btn_priority_1:
                tempDialog = new TempDialog(this);
                tempDialog.setPriority(DialogQueueManager.PRIORITY_1);
                break;
            case R.id.btn_priority_2:
                tempDialog = new TempDialog(this);
                tempDialog.setPriority(DialogQueueManager.PRIORITY_2);
                //如果加入控制器的Dialog需要在外部监听它的消失，则需要调用setOutsideDismissListener
                //方法并传入控制器里自定义OnDismissListener即DialogDismissLister的实例对象，如下：
                tempDialog.setOutsideDismissListener(new DialogQueueManager.DialogDismissLister(tempDialog) {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        super.onDismiss(dialog);
                        Log.i(DialogQueueManager.TAG, "listen dialog dismiss outside");
                    }
                });
                break;
            case R.id.btn_priority_3:
                tempDialog = new TempDialog(this);
                tempDialog.setPriority(DialogQueueManager.PRIORITY_3);
                break;
            case R.id.btn_priority_4:
                tempDialog = new TempDialog(this);
                tempDialog.setPriority(DialogQueueManager.PRIORITY_4);
                break;
            default:
                break;
        }

        if (tempDialog != null) {
            tempDialog.setContent(((Button) v).getText().toString());
            DialogQueueManager.show(tempDialog);
        }
    }

    private class TempDialog extends BasicDialog implements View.OnClickListener {

        public TempDialog(Context context) {
            super(context, R.style.TempDialog);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.temp_dialog;
        }

        @Override
        protected void init() {
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            attributes.gravity = Gravity.BOTTOM;
            attributes.height = 780;
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(attributes);
        }

        @Override
        public void onClick(View v) {
            dismiss();
        }

        private void setContent(String content) {
            Button btnPriority = (Button) findViewById(R.id.text_priority);
            btnPriority.setOnClickListener(this);
            btnPriority.setText(content);
        }
    }

}
