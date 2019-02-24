package jie.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	/**
	 * 判断字符串不为空
	 * 
	 * @param stringValue
	 *            需要判断的字符串
	 * @return boolean：如果不为空，返回true；反之，返回false。
	 */
	public static boolean isNotEmpty(String stringValue) {
		if (stringValue != null && !"".equals(stringValue.trim())
				&& !"null".equals(stringValue)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param originalString
	 *            需要在前面添加零的字符串
	 * @param expectStringToLen
	 *            期望字符串达到的长度
	 * @return String
	 */
	public static String addZeroBefortText(String originalString,
			final int expectStringToLen) {
		StringBuffer stringBuffer = new StringBuffer(originalString.trim());
		while (stringBuffer.length() < expectStringToLen) {
			stringBuffer.insert(0, "0");
		}
		return stringBuffer.toString();
	}

	/**
	 * 一行一行读取文件信息
	 * 
	 * @param inStream
	 *            文件输入流
	 * @return 带有换行分隔符的字符串
	 */
	public static String readStrByLine(InputStream inStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			// readLine = readLine.trim();
			if (readLine.length() > 0) {
				if (readLine.charAt(0) == '-') {// 以'-'开头的一行不读取
					continue;
				} else {
					sb.append(readLine);
					sb.append("\r\n");
				}
			}
		}
		return sb.toString();
	}

}
