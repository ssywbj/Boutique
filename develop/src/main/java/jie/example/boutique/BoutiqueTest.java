package jie.example.boutique;

import java.io.IOException;

import jie.example.utils.AppUtil;
import jie.example.utils.LogUtil;
import jie.example.utils.StringUtil;
import android.test.AndroidTestCase;

public class BoutiqueTest extends AndroidTestCase {
	private static final String TAG = BoutiqueTest.class.getSimpleName();

	public void testGetAppName() {
		LogUtil.i(TAG, "AppUtil.getAppName() = " + AppUtil.getAppName());
	}

	public void testReadKey() {
		String str = null;
		try {
			str = StringUtil.readStrByLine(getContext().getAssets().open("a.txt"));
		} catch (IOException e) {
			LogUtil.e(TAG, e.toString(), new Exception());
		}
		LogUtil.i(TAG, str);
	}

}
