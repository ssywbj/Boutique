package com.suheng.ssy.boutique;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.suheng.ssy.boutique.model.Constants;

@Route(path = Constants.ROUTER_APP_ACTIVITY_KEYBOARD)//路径至少需要有两级，第一级是Group名称
public class KeyboardActivity extends BasicActivity {

    public static final int ALPHABET = -10;//字母表
    public static final int PUNCTUATIONS = -12;//标点符号集

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        KeyboardView keyboardView = findViewById(R.id.keyboard);
        Keyboard keyboard = new Keyboard(this, R.xml.keyboard_number);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setPreviewEnabled(false);//回显，默认为true
        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {//只要设置了keyOutputText属性，输出primaryCode都为-1
                if (primaryCode == ALPHABET) {
                    Log.d(mTag, "onPress, primaryCode = " + primaryCode + ", alphabet");
                } else if (primaryCode == PUNCTUATIONS) {
                    Log.d(mTag, "onPress, primaryCode = " + primaryCode + ", punctuations");
                }
            }

            @Override
            public void onRelease(int primaryCode) {
                Log.d(mTag, "onRelease, primaryCode = " + primaryCode);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Log.d(mTag, "onKey, primaryCode = " + primaryCode + ", keyCodes.length = " + keyCodes.length);
            }

            @Override
            public void onText(CharSequence text) {//配合keyOutputText使用，有则调用，没有则不调用
                Log.d(mTag, "onText, text = " + text);
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
    }

}
