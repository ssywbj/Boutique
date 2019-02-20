package ying.jie.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumToChinese {
	private static String formatFractionalPart(int decimal) {
		String number = "零一二三四五六七八九";
		char[] val = String.valueOf(decimal).toCharArray();
		int len = val.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int n = Integer.valueOf(val[i] + "");
			sb.append(number.charAt(n));
		}
		return sb.toString();
	}

	public static String transMoney(String money) throws Exception {
		System.out.println(money);
		StringBuffer rsult = new StringBuffer();
		
		if (money.matches("[$￥][0-9,\\.]+[\\-][0-9,\\.]+")){
			boolean isYuan = money.startsWith("￥");
			System.out.println(money);
			String newMoney = money.replaceAll("[,$￥]", "");
			String [] nums = newMoney.split("-");
			for (int i = 0; i < nums.length; i++) {
				System.out.println(nums[i]);
				if (isYuan) {
					numToChinese(nums[i], rsult);
				} else {
					numToDolor(nums[i], rsult);
				}
				rsult.append("到");
			}
			rsult.deleteCharAt(rsult.length()-1);
		}
		else if (money.matches("\\d+.*")) {
			int decIndex = money.indexOf(".");
			if (decIndex == -1) {

			} else {
				int integ = Integer.valueOf(money.substring(0, decIndex));
				int dec = Integer.valueOf(money.substring(decIndex + 1));
				rsult.append(formatFractionalPart(integ) + "点"
						+ formatFractionalPart(dec));
			}
		} else if (money.trim().matches("\\$\\d+.*")) {
			// String [] unit =
			// {"千","百","十","亿","千","百","十","万","千","百","十","美元"};

			numToDolor(money, rsult);
		} else if (money.matches("￥\\d+.*")) {
			numToChinese(money, rsult);
		}

		return rsult.toString();
	}

	private static void numToDolor(String money, StringBuffer rsult) {
		String allNum = money.replace("$", "").replace(",", "");
		int decIndex = allNum.indexOf(".");
		if (decIndex == -1) {
			rsult.append(formatInteger(Integer.valueOf(allNum)) + "美元");
		} else {
			int integ = Integer.valueOf(allNum.substring(0, decIndex));
			int dec = Integer.valueOf(allNum.substring(decIndex + 1));
			rsult.append(formatInteger(integ) + "点"
					+ formatFractionalPart(dec) + "美元");
		}
	}

	private static void numToChinese(String money, StringBuffer rsult) {
		String unit = "千百十亿千百十万千百十元角分";
		String number = "零一二三四五六七八九";
		String allNum = money.replace("￥", "").replace(",", "");
		System.out.println(allNum);

		String moneyStr = String.format("%.2f", Double.valueOf(allNum))
				.replace(".", "");
		System.out.println(moneyStr);

		int pos = unit.length() - moneyStr.length();
		boolean zero = false;
		for (int i = 0; i < moneyStr.length(); i++) {
			if (moneyStr.charAt(i) == '0') {
				zero = true;
				if (((pos + i + 1) % 4) == 0) {
					rsult.append(unit.charAt(pos + i));
					zero = false;
				}
			} else {
				if (zero) {
					rsult.append(number.charAt(0));
				}
				zero = false;
				rsult.append(number.charAt(moneyStr.charAt(i) - '0'))
						.append(unit.charAt(pos + i));
			}
		}
		if (moneyStr.endsWith("00")) {
			rsult.append('整');
		} else if (zero) {
			rsult.append("零分");
		}
	}

	private static String formatInteger(int integ) {
		String[] unit = { "千", "百", "十", "亿", "千", "百", "十", "万", "千", "百",
				"十", "" };
		String numStr = "零一二三四五六七八九";
		String moneyStr = String.valueOf(integ);
		StringBuffer rsult = new StringBuffer();
		int pos = unit.length - moneyStr.length();
		boolean zero = false;
		for (int i = 0; i < moneyStr.length(); i++) {
			if (moneyStr.charAt(i) == '0') {
				zero = true;
				if (((pos + i + 1) % 4) == 0) {
					rsult.append(unit[pos + i]);
					zero = false;
				}
			} else {
				if (zero) {
					rsult.append(numStr.charAt(0));
				}
				zero = false;
				rsult.append(numStr.charAt(moneyStr.charAt(i) - '0')).append(
						unit[pos + i]);
			}
		}
		return rsult.toString();
	}

	public static String match(String money) throws Exception {
		String regEx = "";
		if (money.matches(".*[$￥][0-9,\\.]+[\\-][0-9,\\.]+.*")) {
			System.out.println("包含[----]");
			regEx = "([$￥][0-9,\\.]+[\\-][0-9,\\.]+)";
		} else if (money.matches(".*[$￥][0-9]+,.*")) {
			System.out.println("包含逗号");
			regEx = "(([\\$￥])(([1-9]\\d{0,2}(,\\d+)*(\\.\\d+)?)|[0]\\.\\d+))";
		} else if (money.matches(".*[$￥]\\d+.*")) {
			System.out.println("不含逗号");
			regEx = "(([\\$￥])([1-9]\\d*(\\.\\d+)?|0\\.\\d+))";
		} else if (money.matches(".*[^$￥]\\d+.*")) {
			System.out.println("不含金钱符号");
			regEx = "(([1-9]\\d*(\\.\\d{1,})?|0\\.\\d+))";
		}
		if ("".equals(regEx)) {
			return money;
		} else {
			// |([$￥]？[1-9]\\d*(.\\d+)*)
			money.matches(regEx);
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(money);
			String num = "";
			if (matcher.find()) {
				num = matcher.group();
			}
			String numMoey = money.replace(num, transMoney(num));
//			return numMoey;
			 return match(numMoey);
		}
	}
}
