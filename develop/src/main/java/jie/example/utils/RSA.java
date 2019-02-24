package jie.example.utils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.Cipher;

import jie.example.boutique.RSAActivity;

/**
 * RSA非对称加解密工具.
 */
public class RSA {
	private static final String TAG = RSAActivity.TAG;

	/**
	 * 生成公钥和私钥
	 */
	public static HashMap<String, Object> getKeys()
			throws NoSuchAlgorithmException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 公钥加密
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int key_len = publicKey.getModulus().bitLength() / 8;// 模长
		String[] datas = splitString(data, key_len - 11);// 加密数据长度 <= 模长-11
		StringBuffer sb = new StringBuffer();
		for (String s : datas) {// 如果明文长度大于模长-11则要分组加密
			sb.append(bcd2Str(cipher.doFinal(s.getBytes())));
		}
		return sb.toString();
	}

	/**
	 * 私钥加密
	 */
	public static String encryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		int key_len = privateKey.getModulus().bitLength() / 8;// 模长
		String[] datas = splitString(data, key_len - 11);// 加密数据长度 <= 模长-11
		StringBuffer sb = new StringBuffer();
		for (String s : datas) {// 如果明文长度大于模长-11则要分组加密
			sb.append(bcd2Str(cipher.doFinal(s.getBytes())));
		}
		return sb.toString();
	}

	/**
	 * 公钥解密
	 */
	public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		int key_len = publicKey.getModulus().bitLength() / 8;// 模长
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		StringBuffer sb = new StringBuffer();// 如果密文长度大于模长则要分组解密
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			sb.append(new String(trimRedundancy(cipher.doFinal(arr))));
		}
		return sb.toString();
	}

	/**
	 * 私钥解密
	 */
	public static String decryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int key_len = privateKey.getModulus().bitLength() / 8;// 模长
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		StringBuffer sb = new StringBuffer();// 如果密文长度大于模长则要分组解密
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			sb.append(new String(trimRedundancy(cipher.doFinal(arr))));
		}
		return sb.toString();
	}

	/**
	 * ASCII码转BCD码
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	/**
	 * 用公钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 * 
	 * @param data
	 *            需加密数据的byte数据
	 * @param pubKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);// 编码前设定编码方式及密钥
			return cipher.doFinal(data);// 传入编码数据并返回编码结果
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 用私钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 * 
	 * @param data
	 *            需加密数据的byte数据
	 * @param privateKey
	 *            私钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);// 编码前设定编码方式及密钥
			return cipher.doFinal(data);// 传入编码数据并返回编码结果
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 用公解密
	 * 
	 * @param encryptedData
	 *            经过decryptByPublicKey()加密返回的byte数据
	 * @param publicKey
	 *            公钥
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData,
			PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过decryptByPrivateKey()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData,
			PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
			return null;
		}
	}

	/**
	 * 修剪冗余字节.
	 */
	private static byte[] trimRedundancy(byte[] data) {
		if (data == null) {
			return null;
		}
		byte[] bb = null;
		boolean isTrim = false;
		int index = 0;
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			if (i == 0 && b == 1) {
				isTrim = true;
				continue;
			}
			if (isTrim && b == -1) {
				continue;
			}
			if (isTrim && b == 0) {
				isTrim = false;
				continue;
			}
			if (bb == null) {
				bb = new byte[data.length - i];
			}
			bb[index] = b;
			index++;
		}
		return bb;
	}

	public static void test(String ming) throws Exception {
		HashMap<String, Object> map = RSA.getKeys();

		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");// 生成公钥和私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

		String modulus = publicKey.getModulus().toString();// 模
		String public_exponent = publicKey.getPublicExponent().toString();// 公钥指数
		String private_exponent = privateKey.getPrivateExponent().toString();// 私钥指数

		RSAPublicKey pubKey = RSA.getPublicKey(modulus, public_exponent);// 使用模和指数生成公钥和私钥
		byte[] pk = pubKey.getEncoded();
		String strpk = new String(Base64Util.encode(pk));
		LogUtil.i(TAG, "公钥 -->" + Arrays.toString(pk));
		LogUtil.i(TAG, "公钥-->" + strpk);
		RSAPrivateKey priKey = RSA.getPrivateKey(modulus, private_exponent);
		byte[] prik = pubKey.getEncoded();
		String strpri = new String(Base64Util.encode(prik));
		LogUtil.i(TAG, "私钥-->" + strpri);

		String mi = RSA.encryptByPrivateKey(ming, priKey);// 加密后的密文
		LogUtil.i(TAG, "加密后的密文-->" + mi);

		ming = RSA.decryptByPublicKey(mi, pubKey);// 解密后的明文
		LogUtil.i(TAG, "解密后的明文-->" + ming);
	}
}