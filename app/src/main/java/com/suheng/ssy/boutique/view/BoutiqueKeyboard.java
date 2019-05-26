package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import com.suheng.ssy.boutique.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoutiqueKeyboard extends KeyboardView {
    private static final String TAG = BoutiqueKeyboard.class.getSimpleName();
    private static final int ALPHABET = -10;//字母表
    private static final int NUMBER = -11;//数字键
    //private static final int PUNCTUATIONS = -12;//标点符号集
    private Keyboard mKeyboardNumber;
    private Keyboard mKeyboardAlphabet;
    private OnKeyListener mOnKeyListener;
    private List<Character> mKeyNumbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private int mType = 0;

    public BoutiqueKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0);
    }

    public BoutiqueKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BoutiqueKeyboard, defStyleAttr, 0);
        mType = typedArray.getInt(R.styleable.BoutiqueKeyboard_type, 0);
        typedArray.recycle();

        mKeyboardNumber = new Keyboard(context, R.xml.keyboard_number);
        mKeyboardAlphabet = new Keyboard(context, R.xml.keyboard_alphabet);

        if (mType == 0 || mType == 1) {
            setKeyboard(mKeyboardNumber);
            this.shuffleNumbers();
        } else {
            setKeyboard(mKeyboardAlphabet);
        }

        setPreviewEnabled(false);//回显，默认为true
        setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {//设置isRepeatable为true的按键长按后只会执行一次onPress方法，然后重复执行onKey、onRelease方法
                Log.d(TAG, "onPress, primaryCode = " + primaryCode);
            }

            @Override
            public void onRelease(int primaryCode) {//只要设置了keyOutputText属性，输出primaryCode都为-1
                Log.d(TAG, "onRelease, primaryCode = " + primaryCode);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {//只要设置keyOutputText属性，就不会执行该方法，而是执行onText方法
                if (primaryCode == ALPHABET) {
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", alphabet" + ", keyCodes.length = " + keyCodes.length);
                    setKeyboard(mKeyboardAlphabet);
                } else if (primaryCode == NUMBER) {
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", number" + ", keyCodes.length = " + keyCodes.length);
                    setKeyboard(mKeyboardNumber);
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", shift" + ", keyCodes.length = " + keyCodes.length);
                    mKeyboardAlphabet.setShifted(!mKeyboardAlphabet.isShifted());
                    invalidateAllKeys();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", delete" + ", keyCodes.length = " + keyCodes.length);
                    if (mOnKeyListener != null) {
                        mOnKeyListener.onDelete();
                    }
                } else if (primaryCode >= 97 && primaryCode <= 97 + 26) {//按下字母键
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", alphabet unit key");
                    this.onText(mKeyboardAlphabet.isShifted() ? Character.toString((char) (primaryCode - 32)) : Character.toString((char) (primaryCode)));
                } else {
                    Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", keyCodes.length = " + keyCodes.length);
                }
            }

            @Override
            public void onText(CharSequence text) {//配合keyOutputText属性使用，配置了则调用，没有则不调用
                Log.d(TAG, "onText, text = " + text);
                if (mOnKeyListener != null) {
                    mOnKeyListener.onText(text);
                }
            }

            @Override
            public void swipeLeft() {
                Log.d(TAG, "swipeLeft");
            }

            @Override
            public void swipeRight() {
                Log.d(TAG, "swipeRight");
            }

            @Override
            public void swipeDown() {
                Log.d(TAG, "swipeDown");
            }

            @Override
            public void swipeUp() {
                Log.d(TAG, "swipeUp");
            }
        });
    }


    private void shuffleNumbers() {//随机打乱数字键盘上键位的排列顺序
        Keyboard keyboard = getKeyboard();
        if (keyboard == null || keyboard.getKeys() == null || keyboard.getKeys().isEmpty()) {
            return;
        }
        Collections.shuffle(mKeyNumbers); // 随机排序数字
        int index = 0;
        for (Keyboard.Key key : keyboard.getKeys()) {
            if (key.codes[0] != Keyboard.KEYCODE_DELETE) {
                if (key.codes[0] == ALPHABET) {
                    if (mType == 1) {//身份证键盘需要把“ABC”键变成“X”
                        key.label = Character.toString('X');
                        key.text = key.label;
                    }
                    continue;
                }

                char code = mKeyNumbers.get(index++);
                //key.codes[0] = code;//char可以自动转为int
                key.label = Character.toString(code);
                key.text = key.label;
            }
        }
        setKeyboard(keyboard);
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    public interface OnKeyListener {
        void onText(CharSequence text);

        void onDelete();
    }
}
