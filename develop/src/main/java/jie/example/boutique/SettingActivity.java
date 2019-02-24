package jie.example.boutique;

import jie.example.manager.ActivityCollector;
import jie.example.utils.StringUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

@SuppressWarnings("deprecation")
public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		addPreferencesFromResource(R.xml.prefs_setting);

		Preference ratePref = findPreference("pref_rate");
		Uri uri = Uri.parse("market://details?id=" + getPackageName());// 进入手机应用市场
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		ratePref.setIntent(goToMarket);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()// 注册Preference变化通知监听器
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()// 注销Preference变化通知监听器
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("pref_username")) {
			updateUserText();
		}
	}

	private void updateUserText() {
		EditTextPreference pref = (EditTextPreference) findPreference("pref_username");
		String user = pref.getText();
		if (!StringUtil.isNotEmpty(user)) {
			user = "?";
		}
		pref.setSummary(String.format("Username:%s", user));// 设置新Summary
	}

}
