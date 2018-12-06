package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.suheng.ssy.boutique.dagger.ab.A;
import com.suheng.ssy.boutique.dagger.ab.DaggerMainComponent;

import javax.inject.Inject;

public class RegexActivity extends BasicActivity {

    public static final String TAG = RegexActivity.class.getSimpleName();
    private EditText mEditPwd;

    @Inject
    A a;//A的构造方法改变了，此处要修改（第三处要修改）
    /*@Inject
    CoffeeMachine mCoffeeMachine;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regex);

        DaggerMainComponent.create().inject(this);

        Log.d(TAG, getString(R.string.digits_test) + ", " + getString(R.string.digits_test2) + ", " + getString(R.string.digits_test3));
        Log.d(TAG, "\\( = " + getString(R.string.regex_test) + " ,\\ = " + getString(R.string.regex_test2)
                + " ,\\\\ = " + getString(R.string.regex_test3) + ", " + a.eat());

        mEditPwd = findViewById(R.id.edit_pwd);
        mEditPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString();
                Log.d(TAG, "match = " + pwd.matches("\\*") + ", " + pwd.matches("\\|")
                        + ", " + pwd.matches("\\(") + ", " + pwd.matches(getString(R.string.regex_test)) + ", " + pwd.matches("\\)")
                        + ", " + pwd.matches("\\?") + ", " + pwd.matches(".")
                        + ", " + pwd.matches("\\.") + ", " + pwd.matches("-")
                        + ", " + pwd.matches("\\\\") + ", " + "\\\\");
                String regex = getString(R.string.regex);
                Log.d(TAG, "regex = " + regex + ", match = " + pwd.matches(regex));
            }
        });

        /*Log.d(TAG, "make coffee info: " + mCoffeeMachine.makeCoffee());*/
    }

    public void onClickVerify(View view) {
        String pwd = mEditPwd.getText().toString();
        Log.d(TAG, "match = " + pwd.matches("\\*") + pwd.matches("\\|") + pwd.matches("\\("));
        String regex = getString(R.string.regex);
        Log.d(TAG, "regex = " + regex);
        Log.d(TAG, "match = " + pwd.matches(regex));
    }
}
