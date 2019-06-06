package com.suheng.ssy.boutique;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.suheng.ssy.boutique.model.Constants;
import com.suheng.ssy.boutique.view.BoutiqueKeyboard;

@Route(path = Constants.ROUTER_APP_ACTIVITY_KEYBOARD)//路径至少需要有两级，第一级是Group名称
public class KeyboardActivity extends BasicActivity {

    private static final int ANIM_DURATION = 300;
    private BoutiqueKeyboard mBoutiqueKeyboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        final EditText editNumber = findViewById(R.id.edit_number);
        final EditText editIdentityCard = findViewById(R.id.edit_identity_card);
        mBoutiqueKeyboard = findViewById(R.id.keyboard);
        mBoutiqueKeyboard.setOnKeyListener(new BoutiqueKeyboard.OnKeyListener() {
            @Override
            public void onText(CharSequence text) {
                editNumber.getText().insert(editNumber.getSelectionStart(), text);
            }

            @Override
            public void onDelete() {
                int selectionStart = editNumber.getSelectionStart();//获取光标的位置
                if (selectionStart > 0) {
                    editNumber.getText().delete(selectionStart - 1, selectionStart);
                }
            }

            @Override
            public void onDone() {
                Log.i("WBJ", "on key done");
            }
        });

        //获取焦点后只显示光标不弹出软键盘：https://blog.csdn.net/android_zyf/article/details/80526249
        editNumber.setShowSoftInputOnFocus(false);
        editNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNumber.hasFocus()) {
                    showKeyboard();
                }
            }
        });
        editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard();
                } else {
                    hideKeyboard();
                    if (editIdentityCard.hasFocus()) {
                        showSoftInput(editIdentityCard);
                    }
                }
            }

        });
    }

    private void showKeyboard() {
        if (mBoutiqueKeyboard.getVisibility() != View.VISIBLE) {
            hideSoftInput();

            ObjectAnimator animator = ObjectAnimator.ofFloat(mBoutiqueKeyboard, View.TRANSLATION_Y, getResources()
                    .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mBoutiqueKeyboard.setVisibility(View.VISIBLE);
                }
            });
            animator.setDuration(ANIM_DURATION).start();
        }
    }

    private void hideKeyboard() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBoutiqueKeyboard, View.TRANSLATION_Y, -getResources()
                .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBoutiqueKeyboard.setVisibility(View.GONE);
            }
        });
        animator.setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onBackPressed() {
        if (mBoutiqueKeyboard.getVisibility() != View.GONE) {
            this.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}
