package jie.example.boutique;

import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import jie.example.utils.Base64Util;
import jie.example.utils.LogUtil;
import jie.example.utils.QRCodeUtil;
import jie.example.utils.RSA;
import jie.example.utils.RSAUtil;
import jie.example.utils.StringUtil;
import jie.example.utils.ToastUtil;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RSAActivity extends BasicActivity {
	@SuppressWarnings("unused")
	private static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhoSOJ5K6zigjZv7W0VUhuXq8X"
			+ "\r"
			+ "WqIx87XJi78gXKnxFnyfC4mybKThz6Plm/+QeOor2ahpYgmMltJhuq9LET7aiwO6"
			+ "\r"
			+ "aXrf9Fme5+vzpL5qMRKvbMz/+3c7Gcsj263JuoAjSeZn6VOLaagaQzEKZgcgGo4B"
			+ "\r" + "+o9rQzCpxXoER+yRhwIDAQAB" + "\r";// 注意字符串的格式
	@SuppressWarnings("unused")
	private static String PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAOGhI4nkrrOKCNm/"
			+ "\r"
			+ "tbRVSG5erxdaojHztcmLvyBcqfEWfJ8LibJspOHPo+Wb/5B46ivZqGliCYyW0mG6"
			+ "\r"
			+ "r0sRPtqLA7ppet/0WZ7n6/OkvmoxEq9szP/7dzsZyyPbrcm6gCNJ5mfpU4tpqBpD"
			+ "\r"
			+ "MQpmByAajgH6j2tDMKnFegRH7JGHAgMBAAECgYEAqbje1NFSoPdKZRGSi05Dp9JN"
			+ "\r"
			+ "fxHMRCsBKdSXNq69nv69S54QPEkBVmMs9ID+IxzxeX7G8k6uxDSHmMAnMB6O2chP"
			+ "\r"
			+ "TiKwEU8j/LRCkXWRu8E0VuaHw1KqQXKoycP5Dudz3ojk7WmlL2ZCVC89sHSBlO3a"
			+ "\r"
			+ "1LPWjHDPuzaW0b+vUyECQQD3sEebzeg7uvtHU2RbiJtd0X/Tomj5HOR/I9Bx7wkE"
			+ "\r"
			+ "NzID5I9UceGj7Or+uc3AjOYLdINgXaGt5i88d99/G4V5AkEA6TNdRg59kTrdCfFs"
			+ "\r"
			+ "FnV5+oXd5iHYhUyo2uN/wa10shMc49KOzDly9XyQ9WuDTNLssq1tXmg/zEB0ykh2"
			+ "\r"
			+ "3GYO/wJBAN3BKmt4z0ni3yP9qmU1CfzWGz6sMpWN45lcxDutSgJHNHU9xYFSMDVm"
			+ "\r"
			+ "YQJL8cVJoJBy8Uhuq9kYtAPMQwH27ukCQQDbs0+Jw/cazvkV+AQb+JttoZbXzEU/"
			+ "\r"
			+ "+GQQrEpdfnw1lHogTOvJjco3ax7qiFrQEtt6zpb+XXrVhFhDhxesNt6jAkEA6z6G"
			+ "\r"
			+ "X5hmSsuyFP8qBfmzQUO7PeQGLYUn9iT6GpJnnZpBvonVQI/GslGOVnj25gMmvKh1"
			+ "\r" + "Gv4uy2IjmeU9RylFSA==" + "\r";// 注意字符串的格式
	@SuppressWarnings("unused")
	private KeyPair mRSAKeyPair;
	public static final String TAG = RSAActivity.class.getSimpleName();
	private static final String REFEX_DECIMAL = "\\d+.{0,1}\\d{0,2}";// 最多有两位小数
	private EditText mEditInput;
	private TextView mTextEncoded, mTextDecoded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle(R.string.rsa_aty);
		setContentView(R.layout.rsa_aty);
		initData();
		loadingData();
	}

	@Override
	public void initData() {
		mEditInput = (EditText) findViewById(R.id.edit_input);
		mEditInput.addTextChangedListener(mTextWatcher);
		mEditInput.post(new Runnable() {// 测量View的宽高
					@Override
					public void run() {
						LogUtil.i(TAG, "width::" + mEditInput.getWidth()
								+ ", heiht::" + mEditInput.getHeight());
					}
				});

		mTextEncoded = (TextView) findViewById(R.id.edit_show_encoded);
		mTextDecoded = (TextView) findViewById(R.id.edit_show_decoded);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mRSAKeyPair = RSAUtil.generateRSAKeyPair();
				} catch (Exception e) {
					LogUtil.e(TAG, e.toString(), new Exception());
				}
			}
		}).start();

		mImageQRcode = (ImageView) findViewById(R.id.qr_code);
	}

	private ImageView mImageQRcode;

	@Override
	public void loadingData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RSA.test("瑕帮灰24343玩玩dgsfggf#@%^&4");// 第一种RSA加解密
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		createQRCode();
	}

	private void createQRCode() {
		final String filePath = getFileRoot(this) + File.separator + "qr_"
				+ System.currentTimeMillis() + ".jpg";
		// 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = QRCodeUtil.createQRImage("http://www.hao123.com/", 1000, 1000,
						BitmapFactory.decodeResource(getResources(),
								R.drawable.ic_launcher), filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mImageQRcode.setImageBitmap(BitmapFactory
									.decodeFile(filePath));
						}
					});
				}
			}
		}).start();
	}

	// 文件存储根目录
	private String getFileRoot(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File external = context.getExternalFilesDir(null);
			if (external != null) {
				return external.getAbsolutePath();
			}
		}
		return context.getFilesDir().getAbsolutePath();
	}

	public void setOnClick(View view) {
		// 第二种RSA加解密--start
		switch (view.getId()) {
		case R.id.text_encode:// 加密使用公钥
			try {
				String source = mEditInput.getText().toString().trim();

				// PublicKey publicKey = RSAUtil.loadPublicKey(PUCLIC_KEY);//
				// 获取公钥方法一：从字符串中提取公钥
				// PublicKey publicKey = mRSAKeyPair.getPublic();//
				// 获取公钥方法二：从自动生成的密钥对中获取公钥
				InputStream inPublic = getResources().getAssets().open(
						"rsa_public_key.pem");// 获取公钥方法三：从文件中提取公钥
				PublicKey publicKey = RSAUtil.loadPublicKey(inPublic);
				RSAUtil.printPublicKeyInfo(publicKey);
				byte[] encryptByte = RSAUtil.encryptData(source.getBytes(),
						publicKey);// RSA加密

				String afterencrypt = Base64Util.encode(encryptByte);// 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
				mTextEncoded.setText(afterencrypt);
			} catch (Exception e) {
				LogUtil.e(TAG, e.toString(), new Exception());
			}
			break;
		case R.id.text_decode:// 解密使用私钥
			try {
				String encryptContent = mTextEncoded.getText().toString()
						.trim();
				byte[] decode = Base64Util.decode(encryptContent);// 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密

				// PrivateKey privateKey =
				// RSAUtil.loadPrivateKey(PRIVATE_KEY);// 获取私钥方法一：从字符串中得到私钥
				// PrivateKey privateKey = mRSAKeyPair.getPrivate();//
				// 获取私钥方法二：从字符串中提取私钥
				InputStream inPrivate = getResources().getAssets().open(
						"pkcs8_rsa_private_key.pem");// 获取私钥方法三：从文件中提取私钥
				PrivateKey privateKey = RSAUtil.loadPrivateKey(inPrivate);
				RSAUtil.printPrivateKeyInfo(privateKey);
				byte[] decryptByte = RSAUtil.decryptData(decode, privateKey);// RSA解密

				String decryptStr = new String(decryptByte);
				mTextDecoded.setText(decryptStr);
			} catch (Exception e) {
				LogUtil.e(TAG, e.toString(), new Exception());
			}
			break;
		default:
			break;
		}
		// 第一种RSA加解密--end
	}

	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String text = s.toString();
			LogUtil.i(TAG, "onTextChanged(CharSequence, in, int, int)::" + text);
			if (StringUtil.isNotEmpty(text) && !text.matches(REFEX_DECIMAL)) {
				ToastUtil.showToast(R.string.input_warn);
				text = text.substring(0, text.length() - 1);
				mEditInput.setText(text);//
				try {
					mEditInput.setSelection(text.length());// 设置光标的位置
				} catch (Exception e) {
					LogUtil.e(TAG, text + "::" + text.length());
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			LogUtil.i(TAG, "beforeTextChanged(CharSequence, int, int, int)-->"
					+ s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			LogUtil.i(TAG, "afterTextChanged(Editable)-->" + text);
			if (text.length() > 5) {
				ToastUtil.showToast(R.string.input_tolen);
				text = text.substring(0, text.length() - 1);
				mEditInput.setText(text);
				try {
					mEditInput.setSelection(text.length());// 设置光标的位置
				} catch (Exception e) {
					LogUtil.e(TAG, text + "::::::" + text.length());
				}
			}
		}
	};

}
