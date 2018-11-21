package com.suheng.ssy.boutique;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class BoutiqueActivity extends AppCompatActivity {

    public static final String TAG = BoutiqueActivity.class.getSimpleName();
    private EditText mEditPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);

        Log.d(TAG, getString(R.string.digits_test));
        Log.d(TAG, getString(R.string.digits_test2));
        Log.d(TAG, getString(R.string.digits_test3));

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
                        + ", " + pwd.matches("\\(") + ", " + pwd.matches(getString(R.string.regex_res)) + ", " + pwd.matches("\\)")
                        + ", " + pwd.matches("\\?") + ", " + pwd.matches(".")
                        + ", " + pwd.matches("\\.") + ", " + pwd.matches("-")
                        + ", " + pwd.matches("\\\\") + ", " + "\\\\");
                String regex = getString(R.string.regex);
                String regex2 = getString(R.string.regex2);
                Log.d(TAG, "regex = " + regex + ", " + "regex2 = " + regex2);
                Log.d(TAG, "match = " + pwd.matches(regex) + ", " + pwd.matches(regex2));

                Log.d(TAG, "( = " + "\\(" + ", " + getString(R.string.regex_res));
            }
        });
    }

    public void onClickVerify(View view) {
        String pwd = mEditPwd.getText().toString();
        Log.d(TAG, "match = " + pwd.matches("\\*") + pwd.matches("\\|") + pwd.matches("\\("));
        String regex = getString(R.string.regex);
        Log.d(TAG, "regex = " + regex);
        Log.d(TAG, "match = " + pwd.matches(regex));
    }
}
