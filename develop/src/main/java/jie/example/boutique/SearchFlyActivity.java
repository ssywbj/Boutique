package jie.example.boutique;

import java.util.Random;

import jie.example.utils.ToastUtil;
import jie.example.widget.KeywordsFlow;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 自定义FramLayout文字飞入飞出效果
 */
public class SearchFlyActivity extends BasicActivity implements OnClickListener {
	public static final String[] keywords = { "QQ", "BaseAnimation", "APK",
			"GFW", "铅笔", "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛", "Base",
			"笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人", "内存清理", "地图", "导航",
			"闹钟", "主题", "通讯录", "播放器", "CSDN leak", "安全", "Animation", "美女",
			"天气", "4743G", "戴尔", "联想", "欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",
			"iciba", "油水关系", "网游App", "互联网", "365日历", "脸部识别", "Chrome",
			"Safari", "中国版Siri", "苹果", "iPhone5S", "摩托 ME525", "魅族 MX3", "小米" };
	private KeywordsFlow keywordsFlow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle("自定义FramLayout文字飞入飞出效果");
		setContentView(R.layout.activity_custom_serch_fly_main);
		initData();
		loadingData();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			keywordsFlow.rubKeywords();
			// keywordsFlow.rubAllViews();
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		} else if (v.getId() == R.id.button2) {
			keywordsFlow.rubKeywords();
			// keywordsFlow.rubAllViews();
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
		} else if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			ToastUtil.showToast(keyword);
		}
	}

	private void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
		Random random = new Random();
		for (int i = 0; i < KeywordsFlow.MAX; i++) {
			int ran = random.nextInt(arr.length);
			String tmp = arr[ran];
			keywordsFlow.feedKeyword(tmp);
		}
	}

	@Override
	public void initData() {
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		keywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);

	}

	@Override
	public void loadingData() {
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		// 添加
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
	}

}
