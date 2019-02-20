package ying.jie.util;

/**
 * 实现思想，按照中国人的读法，每四位分一组
 */
public class VoiceToText {
    private static final String[] SOURCE = {"零", "一", "二", "三", "四", "五", "六",
            "七", "八", "九"};
    private static final String[] BASIC = {"千", "百", "十", ""};
    private static final String[] UNIT = {"亿", "万", "元"};

    public static String voiceToText(String voiceText) {
        /*if (TextUtils.isEmpty(voiceText)) {
            return "";
        }*/

        if (voiceText.matches("[1-9]\\d*")) {
            //System.out.println("整数匹配(不能以零开头)");
        } else if (voiceText.matches("[1-9]\\d*\\.\\d{1,2}")) {
            //System.out.println("最多带两位小数的匹配");
        } else if (voiceText.matches("[1-9]\\d{0,2}(,\\d{3})+(|\\.\\d{1,2})")) {
            if (voiceText.contains(".")) {
                //System.out.println("逗号小数匹配");
                voiceText = voiceText.replace(",", "");
            } else {
                //System.out.println("逗号整数匹配");
                voiceText = voiceText.replace(",", "");
            }
        } else {
            //System.out.println("不匹配");
            return "Match Nothing";
        }

        return styleCNRead(voiceText);
    }

    private static String styleCNRead(String voiceText) {
        String[] child = StringUtil.divideByLen(voiceText, 4, false);
        if (child != null && child.length > 0) {
            StringBuilder total = new StringBuilder();
            int basicLen = BASIC.length;
            StringBuilder stringBuilder;
            boolean meetZero;
            int len, count;
            char ch;

            for (int j = 0; j < child.length; j++) {
                stringBuilder = new StringBuilder();
                len = child[j].length();
                meetZero = false;
                count = 0;

                for (int i = len; i > 0; i--, count++) {
                    ch = child[j].charAt(count);
                    if (ch == '0') {
                        meetZero = true;
                    } else {
                        if (meetZero) {
                            stringBuilder.append(SOURCE[0]);
                        }
                        stringBuilder.append(SOURCE[Integer.parseInt("" + ch)])
                                .append(BASIC[basicLen - i]);
                        meetZero = false;
                    }
                }

                total.append(stringBuilder.toString()).append(
                        UNIT[UNIT.length - child.length + j]);
            }

            return total.toString();
        }

        return "";
    }

}
