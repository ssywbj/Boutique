package com.example.wbj;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wbj.service.ClipService;
import com.example.wbj.utils.clip.ClipChangedListener;
import com.example.wbj.utils.clip.ClipManager;
import com.wbj.view.ClipWindow;

import java.util.HashMap;
import java.util.Map;

public class ClipActivity extends AppCompatActivity {
    private ClipManager mClipManager;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_aty);
        mEditText = (EditText) findViewById(R.id.edit_clip);

        mClipManager = new ClipManager(this);
        this.setEditText(mClipManager.getLatestText());

        startService(new Intent(this, ClipService.class));
    }

    private void setEditText(String latestText) {
        if (!latestText.isEmpty()) {
            mEditText.setText(latestText);
            mEditText.setSelection(latestText.length());//将光标放在最后

            /*mEditText.setText("bquake435445");
            Pattern p = Pattern.compile("\\bquake[0-9]*\\b", Pattern.CASE_INSENSITIVE);
            Linkify.addLinks(mEditText, p, "content://com.paad.earthquake/earthquakes/");
            mEditText.setText(this.getClickableSpan());
            mEditText.setText("百度：www.baidu.com，手机号：18819059959 ，邮箱：cxxxx@qq.com");
            mEditText.setMovementMethod(LinkMovementMethod.getInstance());*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mClipManager.registerListener(new ClipChangedListener(this) {
            @Override
            public void latestTextChange(String latestText) {
                setEditText(latestText);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClipManager.unregisterListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {//需要设置Activity的启动方式不为standard
        super.onNewIntent(intent);
        String clipText = intent.getStringExtra(ClipWindow.DATA_CLIP_CONTENT);
        this.setEditText(clipText);
    }

    private SpannableString getClickableSpan() {
        Map<String, String> mapLink = new HashMap<>();
        mapLink.put("使用条款", "");
        mapLink.put("隐私政策", "");
        mapLink.put("韦帮杰", "");

        String source = "使用该软件，即表示您同意该软件的使用条款和隐私政策，百度：www.baidu.com，手机号：18819059959 ，邮箱：cxxxx@qq.com，韦帮杰";
        SpannableString spannableString = new SpannableString(source.trim());
        int start, end;
        for (Map.Entry<String, String> map : mapLink.entrySet()) {
            final String key = map.getKey().trim();
            if (source.contains(key)) {
                start = source.indexOf(key);
                end = start + key.length();
                spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//下划线
                spannableString.setSpan(new ClickableSpan() {//点击事件
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(ClipActivity.this, key, Toast.LENGTH_SHORT).show();
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字颜色
            }
        }
        return spannableString;
    }

}
