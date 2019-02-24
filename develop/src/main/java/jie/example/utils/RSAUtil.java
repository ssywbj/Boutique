package jie.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import jie.example.boutique.RSAActivity;

public final class RSAUtil {
	private static final String TAG = RSAActivity.TAG;
	private static String RSA = "RSA";

	/**
	 * 随机生成RSA密钥对(默认密钥长度为1024)
	 * 
	 * @return KeyPair：密钥对
	 */
	public static KeyPair generateRSAKeyPair() throws Exception {
		return generateRSAKeyPair(1024);
	}

	/**
	 * 随机生成RSA密钥对
	 * 
	 * @param keyLength
	 *            密钥长度，范围：512～2048，一般1024。
	 * @return KeyPair：密钥对
	 */
	public static KeyPair generateRSAKeyPair(int keyLength) throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
		kpg.initialize(keyLength);
		return kpg.genKeyPair();
	}

	/**
	 * 将输入流转换成字符串
	 * 
	 * @param inStream
	 *            公钥输入流
	 * @return 字符串
	 */
	private static String readKey(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return sb.toString();
	}
	
	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 */
	public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
		byte[] buffer = Base64Util.decode(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param publicKeyInStream
	 *            公钥输入流
	 * @return PublicKey：公钥
	 */
	public static PublicKey loadPublicKey(InputStream publicKeyInStream)
			throws Exception {
		return loadPublicKey(readKey(publicKeyInStream));
	}
	
	/**
	 * 用公钥加密：每次加密的字节数，不能超过密钥的长度值减去11
	 * 
	 * @param data
	 *            需要加密数据的数据
	 * @param publicKey
	 *            公钥
	 * @return 加密后的数据
	 */
	public static byte[] encryptData(byte[] data, PublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);// 编码前设定编码方式及密钥
		cipher.init(Cipher.ENCRYPT_MODE, publicKey); // 传入编码数据并返回编码结果
		return cipher.doFinal(data);
	}
	
	/**
	 * 从字符串中加载私钥：加载时使用的是PKCS8EncodedKeySpec(PKCS#8编码的Key指令)
	 * 
	 * @param privateKeyStr
	 *            ：私钥字符串
	 * @return PrivateKey：私钥
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		byte[] buffer = Base64Util.decode(privateKeyStr);
		// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}
	
	/**
	 * 从文件中加载私钥
	 * 
	 * @param privateKeyInStream
	 *            私钥文件名
	 * @return PrivateKey：私钥
	 */
	public static PrivateKey loadPrivateKey(InputStream privateKeyInStream)
			throws Exception {
		return loadPrivateKey(readKey(privateKeyInStream));
	}

	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过加密后的数据
	 * @param privateKey
	 *            私钥
	 * @return byte[]：解密后的数据
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(encryptedData);
	}
	
	/**
	 * 打印公钥信息
	 * 
	 * @param publicKey
	 */
	public static void printPublicKeyInfo(PublicKey publicKey) {
		RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
		LogUtil.i(TAG, "==========RSAPublicKey=========");
		LogUtil.i(TAG, "Modulus.length::"
				+ rsaPublicKey.getModulus().bitLength());
		LogUtil.i(TAG, "Modulus::" + rsaPublicKey.getModulus().toString());
		LogUtil.i(TAG, "PublicExponent.length::"
				+ rsaPublicKey.getPublicExponent().bitLength());
		LogUtil.i(TAG, "PublicExponent::"
				+ rsaPublicKey.getPublicExponent().toString());
	}

	public static void printPrivateKeyInfo(PrivateKey privateKey) {
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		LogUtil.i(TAG, "==========RSAPrivateKey=========");
		LogUtil.i(TAG, "Modulus.length::"
				+ rsaPrivateKey.getModulus().bitLength());
		LogUtil.i(TAG, "Modulus::" + rsaPrivateKey.getModulus().toString());
		LogUtil.i(TAG, "PrivateExponent.length::"
				+ rsaPrivateKey.getPrivateExponent().bitLength());
		LogUtil.i(TAG, "PrivateExponent::"
				+ rsaPrivateKey.getPrivateExponent().toString());
	}

	/**
	 * 通过私钥byte[]将公钥还原，适用于RSA算法
	 * 
	 * @param keyBytes
	 * @return PrivateKey：私钥
	 */
	@SuppressWarnings("unused")
	private static PrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 使用N、e值还原公钥
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @return PublicKey：公钥
	 */
	@SuppressWarnings("unused")
	private static PublicKey getPublicKey(String modulus, String publicExponent)
			throws Exception {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 使用N、d值还原私钥
	 * 
	 * @param modulus
	 * @return PrivateKey：私钥
	 */
	@SuppressWarnings("unused")
	private static PrivateKey getPrivateKey(String modulus,
			String privateExponent) throws Exception {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

}
