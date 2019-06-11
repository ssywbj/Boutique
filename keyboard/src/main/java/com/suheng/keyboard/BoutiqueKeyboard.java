package com.suheng.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoutiqueKeyboard extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    private static final String TAG = BoutiqueKeyboard.class.getSimpleName();
    private static final int ALPHABET = -10;//字母表
    private static final int NUMBER = -11;//数字键
    private static final int PUNCTUATIONS = -12;//标点符号
    private static final int SPACE_KEY = -13;//空格键
    private Keyboard mKeyboardNumber;
    private Keyboard mKeyboardAlphabet;
    private Keyboard mKeyboardPunctuation;
    private OnKeyListener mOnKeyListener;
    private List<Character> mKeyNumbers = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    private List<Character> mKeyAlphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'
            , 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

    private int mType = 0;
    private boolean mIsShuffle;
    private String mSpaceKeyText;

    private Paint mTextPaint;

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
        mIsShuffle = typedArray.getBoolean(R.styleable.BoutiqueKeyboard_shuffle, false);
        mSpaceKeyText = typedArray.getString(R.styleable.BoutiqueKeyboard_spaceKeyText);
        typedArray.recycle();

        if (mType == 2) {
            this.initAlphabetKeyboard();
        } else if (mType == 3) {
            this.initPunctuationKeyboard();
        } else {
            this.initNumberKeyboard();
        }

        setPreviewEnabled(false);//回显，默认为true
        setOnKeyboardActionListener(this);

        this.initPaint();
    }

    private void initNumberKeyboard() {
        mKeyboardNumber = new Keyboard(getContext(), R.xml.keyboard_number);
        if (mIsShuffle) {
            this.shuffleNumber(mKeyboardNumber);
        } else {
            setKeyboard(mKeyboardNumber);
        }
    }

    private void initAlphabetKeyboard() {
        mKeyboardAlphabet = new Keyboard(getContext(), R.xml.keyboard_alphabet);
        if (mIsShuffle) {
            this.shuffleAlphabet(mKeyboardAlphabet);
        } else {
            setKeyboard(mKeyboardAlphabet);
        }
    }

    private void initPunctuationKeyboard() {
        mKeyboardPunctuation = new Keyboard(getContext(), R.xml.keyboard_punctuation);
        setKeyboard(mKeyboardPunctuation);
    }

    private void shuffleNumber(Keyboard keyboard) {//随机打乱数字键盘上数字的排列顺序
        if (keyboard == null || keyboard.getKeys() == null || keyboard.getKeys().isEmpty()) {
            return;
        }
        Collections.shuffle(mKeyNumbers);//随机排序数字
        int index = 0;
        for (Keyboard.Key key : keyboard.getKeys()) {
            if (key.codes[0] == ALPHABET) {
                if (mType == 1) {//身份证键盘需要把“ABC”键变成“X”
                    key.label = Character.toString('X');
                    key.text = key.label;
                }
                continue;
            }

            if (key.codes[0] >= 48 && key.codes[0] <= 57) {//ASCII码：0-48
                char code = mKeyNumbers.get(index++);
                //key.codes[0] = code;
                key.label = Character.toString(code);
                key.text = key.label;
            }
        }
        setKeyboard(keyboard);
    }

    private void shuffleAlphabet(Keyboard keyboard) {//随机打乱字母键盘上字母的排列顺序
        if (keyboard == null || keyboard.getKeys() == null || keyboard.getKeys().isEmpty()) {
            return;
        }
        Collections.shuffle(mKeyAlphabet);//随机排序数字
        int index = 0;
        for (Keyboard.Key key : keyboard.getKeys()) {
            if (key.codes[0] >= 97 && key.codes[0] <= 122) {//ASCII码：a-97,z-122
                char code = mKeyAlphabet.get(index++);
                key.codes[0] = code;//char可以自动转为int
                key.label = Character.toString(code);
            }
        }
        setKeyboard(keyboard);
    }

    private void initPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17
                , getResources().getDisplayMetrics()));//17sp
        mTextPaint.setColor(Color.WHITE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == Keyboard.KEYCODE_DONE || key.codes[0] == ALPHABET
                    || key.codes[0] == PUNCTUATIONS || key.codes[0] == NUMBER
                    || key.codes[0] == Keyboard.KEYCODE_SHIFT || key.codes[0] == SPACE_KEY) {
                Drawable drawable;
                if (key.codes[0] == Keyboard.KEYCODE_DONE) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.keyboard_key_done_bg);
                } else if (key.codes[0] == SPACE_KEY) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.keyboard_key_space_bg);
                } else {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.keyboard_key_switch_bg);
                }
                if (drawable != null) {
                    drawable.setState(key.getCurrentDrawableState());
                    drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    drawable.draw(canvas);
                }

                if (key.codes[0] == SPACE_KEY) {
                    if (!TextUtils.isEmpty(mSpaceKeyText)) {
                        key.label = mSpaceKeyText;
                        mTextPaint.setColor(Color.BLACK);
                    }
                } else {
                    mTextPaint.setColor(Color.WHITE);
                }
                if (key.label != null) {
                    Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                    float y = key.y + 1.0f * key.height / 2 + (fontMetrics.bottom
                            - fontMetrics.top) / 2 - fontMetrics.bottom;
                    canvas.drawText(key.label.toString(), key.x + (1.0f * key.width / 2), y, mTextPaint);
                }
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOnKeyListener = null;
        mKeyNumbers.clear();
        mKeyAlphabet.clear();
    }

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
            if (mKeyboardAlphabet == null) {
                initAlphabetKeyboard();
            } else {
                setKeyboard(mKeyboardAlphabet);
            }
        } else if (primaryCode == NUMBER) {
            Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", number" + ", keyCodes.length = " + keyCodes.length);
            if (mKeyboardNumber == null) {
                initNumberKeyboard();
            } else {
                setKeyboard(mKeyboardNumber);
            }
        } else if (primaryCode == PUNCTUATIONS) {
            Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", number" + ", keyCodes.length = " + keyCodes.length);
            if (mKeyboardPunctuation == null) {
                initPunctuationKeyboard();
            } else {
                setKeyboard(mKeyboardPunctuation);
            }
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", shift" + ", keyCodes.length = " + keyCodes.length);
            mKeyboardAlphabet.setShifted(!mKeyboardAlphabet.isShifted());
            invalidateAllKeys();
        } else if (primaryCode == Keyboard.KEYCODE_DONE) {
            Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", delete" + ", keyCodes.length = " + keyCodes.length);
            if (mOnKeyListener != null) {
                mOnKeyListener.onDone();
            }
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            Log.d(TAG, "onKey, primaryCode = " + primaryCode + ", delete" + ", keyCodes.length = " + keyCodes.length);
            if (mOnKeyListener != null) {
                mOnKeyListener.onDelete();
            }
        } else if (primaryCode >= 97 && primaryCode <= 97 + 25) {//按下字母键，ASCII码：A-65,Z-90;a-97,z-122
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

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    public interface OnKeyListener {
        void onText(CharSequence text);

        void onDelete();

        void onDone();
    }
}
