package ying.jie.boutique;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nantian.tts.TTS;

import ying.jie.entity.GsonUser;
import ying.jie.util.LogUtil;
import ying.jie.util.VoiceToText;

/**
 * Created by Wbj on 2016/2/16.
 */
public class BankVoiceActivity extends BasicActivity {
    private EditText mEditNumber;
    private TextView mTextMatch;
    private TTS mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));

        mTTS = new TTS(this);
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.bank_voice_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mEditNumber = (EditText) findViewById(R.id.edit_number);

        findViewById(R.id.text_play).setOnClickListener(this);
        mTextMatch = (TextView) findViewById(R.id.text_match);
    }

    private int count = 0;

    @Override
    public void onClick(View v) {
        count++;
        Log.d("TAG", "count =" + count + ", user info = " + new GsonUser(23, "Ssywbj"));
        int i = v.getId();
        if (i == R.id.text_play) {
            String speak;
            try {
                speak = VoiceToText.styleCNRead(mEditNumber.getText().toString());
            } catch (Exception e) {
                LogUtil.e(mLogTag, e.toString());
                speak = mEditNumber.getText().toString();
            }
            mTextMatch.setText(speak);
            mTTS.Read(mEditNumber.getText().toString(), 1);
        }
    }

}
