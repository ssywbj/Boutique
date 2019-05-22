package com.suheng.ssy.boutique;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.suheng.ssy.boutique.model.Constants;

@Route(path = Constants.ROUTER_APP_ACTIVITY_KEYBOARD)//路径至少需要有两级，第一级是Group名称
public class KeyboardActivity extends BasicActivity {

    public static final int ALPHABET = -10;//字母表
    //public static final int PUNCTUATIONS = -12;//标点符号集
    private KeyboardView mKeyboardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        final EditText editNumber = findViewById(R.id.edit_number);
        final EditText editIdentityCard = findViewById(R.id.edit_identity_card);
        mKeyboardView = findViewById(R.id.keyboard);
        Keyboard keyboard = new Keyboard(this, R.xml.keyboard_number);
        mKeyboardView.setKeyboard(keyboard);
        mKeyboardView.setPreviewEnabled(false);//回显，默认为true
        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {//设置isRepeatable为true的按键长按后只会执行一次onPress方法，然后重复执行onKey、onRelease方法
                Log.d(mTag, "onPress, primaryCode = " + primaryCode);
            }

            @Override
            public void onRelease(int primaryCode) {//只要设置keyOutputText属性，输出primaryCode都为-1
                Log.d(mTag, "onRelease, primaryCode = " + primaryCode);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {//只要设置keyOutputText属性，就不会执行该方法，而是执行onText方法
                if (primaryCode == ALPHABET) {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", alphabet" + ", keyCodes.length = " + keyCodes.length);
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", delete" + ", keyCodes.length = " + keyCodes.length);
                    int selectionStart = editNumber.getSelectionStart();//获取光标的位置
                    if (selectionStart > 0) {
                        editNumber.getText().delete(selectionStart - 1, selectionStart);
                    }
                }
            }

            @Override
            public void onText(CharSequence text) {//配合keyOutputText属性使用，配置了则调用，没有则不调用
                Log.d(mTag, "onText, text = " + text);
                editNumber.getText().insert(editNumber.getSelectionStart(), text);
            }

            @Override
            public void swipeLeft() {
                Log.d(mTag, "swipeLeft");
            }

            @Override
            public void swipeRight() {
                Log.d(mTag, "swipeRight");
            }

            @Override
            public void swipeDown() {
                Log.d(mTag, "swipeDown");
            }

            @Override
            public void swipeUp() {
                Log.d(mTag, "swipeUp");
            }
        });

        //获取焦点后只显示光标不弹出软键盘：https://blog.csdn.net/android_zyf/article/details/80526249
        editNumber.setShowSoftInputOnFocus(false);
        editNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNumber.hasFocus()) {
                    //mKeyboardView.setVisibility(View.VISIBLE);
                    hideSoftKeyboard();
                }
            }
        });
        editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    hideSoftKeyboard();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, View.TRANSLATION_Y, getResources()
                            .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            mKeyboardView.setVisibility(View.VISIBLE);
                        }
                    });
                    animator.setDuration(400).start();
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, View.TRANSLATION_Y, -getResources()
                            .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mKeyboardView.setVisibility(View.GONE);
                        }
                    });
                    animator.setDuration(400).start();
                    if (editIdentityCard.hasFocus()) {
                        showSoftKeyboard(editIdentityCard);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mKeyboardView.getVisibility() != View.GONE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, View.TRANSLATION_Y, -getResources()
                    .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mKeyboardView.setVisibility(View.GONE);
                }
            });
            animator.setDuration(400).start();
        } else {
            super.onBackPressed();
        }
    }
}
