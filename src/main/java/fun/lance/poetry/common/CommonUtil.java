package fun.lance.poetry.common;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    private static final Map<Integer, String> hourCharMap = new HashMap<>();

    static {
        hourCharMap.put(1, "一");
        hourCharMap.put(2, "二");
        hourCharMap.put(3, "三");
        hourCharMap.put(4, "四");
        hourCharMap.put(5, "五");
        hourCharMap.put(6, "六");
        hourCharMap.put(7, "七");
        hourCharMap.put(8, "八");
        hourCharMap.put(9, "九");
        hourCharMap.put(10, "十");
        hourCharMap.put(11, "十一");
        hourCharMap.put(12, "十二");
        hourCharMap.put(13, "十三");
        hourCharMap.put(14, "十四");
        hourCharMap.put(15, "十五");
        hourCharMap.put(16, "十六");
        hourCharMap.put(17, "十七");
        hourCharMap.put(18, "十八");
        hourCharMap.put(19, "十九");
        hourCharMap.put(20, "二十");
        hourCharMap.put(21, "二一");
        hourCharMap.put(22, "二二");
        hourCharMap.put(23, "二三");
        hourCharMap.put(24, "二四");
    }

    /**
     * 将小时的阿拉伯数字转为汉字
     * 1 -> 一
     * 5 -> 五
     * 23 -> 二三
     */
    public static String hourIntToChar(Integer hourInt) {
        return hourCharMap.get(hourInt);
    }
}
