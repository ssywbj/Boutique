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

    private static final int ANIM_DURATION = 400;
    private static final int ALPHABET = -10;//字母表
    private static final int NUMBER = -11;//数字键
    //private static final int PUNCTUATIONS = -12;//标点符号集
    private KeyboardView mKeyboardView;
    private Keyboard mKeyboardNumber;
    private Keyboard mKeyboardAlphabet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        final EditText editNumber = findViewById(R.id.edit_number);
        final EditText editIdentityCard = findViewById(R.id.edit_identity_card);
        mKeyboardView = findViewById(R.id.keyboard);
        mKeyboardNumber = new Keyboard(this, R.xml.keyboard_number);
        mKeyboardAlphabet = new Keyboard(this, R.xml.keyboard_alphabet);
        mKeyboardView.setKeyboard(mKeyboardNumber);
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
                    mKeyboardView.setKeyboard(mKeyboardAlphabet);
                } else if (primaryCode == NUMBER) {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", number" + ", keyCodes.length = " + keyCodes.length);
                    mKeyboardView.setKeyboard(mKeyboardNumber);
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", shift" + ", keyCodes.length = " + keyCodes.length);
                    mKeyboardAlphabet.setShifted(!mKeyboardAlphabet.isShifted());
                    mKeyboardView.invalidateAllKeys();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", delete" + ", keyCodes.length = " + keyCodes.length);
                    int selectionStart = editNumber.getSelectionStart();//获取光标的位置
                    if (selectionStart > 0) {
                        editNumber.getText().delete(selectionStart - 1, selectionStart);
                    }
                } else if (primaryCode >= 97 && primaryCode <= 97 + 26) {//按下字母键
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", alphabet unit key");
                    this.onText(mKeyboardAlphabet.isShifted() ? Character.toString((char) (primaryCode - 32)) : Character.toString((char) (primaryCode)));
                } else {
                    Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", keyCodes.length = " + keyCodes.length);
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
        if (mKeyboardView.getVisibility() != View.VISIBLE) {
            hideSoftInput();

            ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, View.TRANSLATION_Y, getResources()
                    .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
            });
            animator.setDuration(ANIM_DURATION).start();
        }
    }

    private void hideKeyboard() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, View.TRANSLATION_Y, -getResources()
                .getDimensionPixelOffset(R.dimen.keyboard_margin_bottom));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mKeyboardView.setVisibility(View.GONE);
            }
        });
        animator.setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onBackPressed() {
        if (mKeyboardView.getVisibility() != View.GONE) {
            this.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}
