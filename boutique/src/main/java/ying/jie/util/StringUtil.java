package ying.jie.util;

import android.text.TextUtils;

public class StringUtil {

    /**
     * @param value             需要在前面添加零的数字
     * @param expectStringToLen 期望字符串达到的长度
     * @return String
     */
    public static String addZero(int value, int expectStringToLen) {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(value));
        while (stringBuffer.length() < expectStringToLen) {
            stringBuffer.insert(0, "0");
        }
        return stringBuffer.toString();
    }

    public static String regexString(String source) {
        if (TextUtils.isEmpty(source)) {
            return "Null";
        }

        StringBuffer stringBuffer = new StringBuffer();
        if (source.matches("[\\u4e00-\\u9fa5]+\\d+(\\.)?\\d+")) {//匹配“中文+数字”
            String[] sources = source.split("[\\u4e00-\\u9fa5]+");//通过汉字正则拆分字符串

            //sources只有两个元素：第一个元素是空字符，第二个是数字字符串。
            if (sources.length != 2) {
                return stringBuffer.append("Match Error!!").toString();
            }

            String textCN = source.substring(0, source.length() - sources[1].length());//截取中文字符
            stringBuffer.append(textCN);
        } else if (source.matches("[\\u4e00-\\u9fa5]+[￥$][1-9]\\d*")) {//中文+￥/$数字
            //String regEx2="(([$￥])([1-9]\\d*(\\.\\d{1,2})?|0\\.\\d{1,2}))";
            stringBuffer.append("现金");
            if (source.contains("￥")) {//人民币
                String[] split = source.split("￥");
                stringBuffer.append(transMoney(split[1]));
            } else if (source.contains("$")) {//美元
                String[] split = source.split("\\$");
                stringBuffer.append(transMoney(split[1]));
            }
        } else if (source.matches("[\\u4e00-\\u9fa5]+[￥$][1-9]\\d*\\.\\d+")) {//中文+￥/$数字带小数
            stringBuffer.append("现金");
            if (source.contains("￥")) {//人民币
                String[] split = source.split("￥");
                stringBuffer.append(transMoney(split[1]));
            } else if (source.contains("$")) {//美元
                String[] split = source.split("\\$");//将美元符号转义，因为它在正则表示行的结尾
                stringBuffer.append(transMoney(split[1]));
                stringBuffer.append("美元");
            }
        } else if (source.matches("[\\u4e00-\\u9fa5]+[￥$][1-9]\\d*[-][1-9]\\d*")) {//中文+￥/$数字-数字，“-”读至
            stringBuffer.append("中文+￥/$数字-数字，“-”读至");
        } else if (source.matches("[\\u4e00-\\u9fa5]+[￥$][1-9]\\d{0,2}(,\\d{0,3})*")) {//中文+￥/$数字中带逗号
            //String regEx1="(([$￥])(([1-9]\\d{0,2}(,\\d+)*(\\.\\d{1,2})?)|[0]\\.\\d{1,2}))";
            stringBuffer.append("中文+￥/$数字中带逗号");
        } else if (source.matches("[\\u4e00-\\u9fa5]+[￥$][1-9]\\d{0,2}(,\\d{0,3})+\\.\\d+")) {//中文+￥/$数字中带逗号带小数
            stringBuffer.append("中文+￥/$数字中带逗号带小数");
        } else {
            stringBuffer.append("Match Nothing!!");
        }
        return stringBuffer.toString();
    }

    public static String transMoney(String money) {
        StringBuffer result = new StringBuffer();
        String unit = "千百十亿千百十万千百十元角分";
        String number = "零壹贰叁肆伍陆柒捌玖";

        String moneyStr = String.format("%.2f", Double.parseDouble(money)).replace(".", "");
        int pos = unit.length() - moneyStr.length();
        boolean zero = false;
        for (int i = 0; i < moneyStr.length(); i++) {
            if (moneyStr.charAt(i) == '0') {
                zero = true;
                if (((pos + i + 1) % 4) == 0) {
                    result.append(unit.charAt(pos + i));
                    zero = false;
                }
            } else {
                if (zero) {
                    result.append(number.charAt(0));
                }
                zero = false;
                result.append(number.charAt(moneyStr.charAt(i) - '0')).append(
                        unit.charAt(pos + i));
            }
        }
        if (moneyStr.endsWith("00")) {
            //rsult.append('整');
        } else if (zero) {
            result.append("零分");
        }
        return result.toString();
    }

    /**
     * 按照指定的长度拆分字符串
     *
     * @param source   需要拆分的字符串
     * @param len      按照指定的长度拆分，必须大于0
     * @param fromHead 从字符串的头部或者尾部开始拆分：true表示从头部，否则从尾部
     * @return 拆分后的子串数组
     */
    public static String[] divideByLen(String source, int len, boolean fromHead) {
        if (len > 0 && source != null && source.length() > 0) {
            int strLen = source.length();
            int arrLen = (strLen % len == 0 ? strLen / len : strLen / len + 1);
            String[] child = new String[arrLen];

            if (fromHead) {
                for (int i = 0; i < arrLen; i++) {
                    child[i] = source.substring(len * i,
                            len * (i + 1) > strLen ? strLen : len * (i + 1));
                }
            } else {
                int beginIndex;
                for (int i = 0; i < arrLen; i++) {
                    beginIndex = strLen - len * (i + 1);
                    child[arrLen - i - 1] = source.substring(
                            beginIndex > 0 ? beginIndex : 0, strLen - len * i);
                }
            }

            return child;
        }

        return null;
    }

}
