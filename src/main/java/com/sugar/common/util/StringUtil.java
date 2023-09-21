package com.sugar.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author author
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * ip正则表达式
     */
    public static final String IP_REGEX = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." + "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    public static final int MAIL_LIMIT_SIZE = 240;

    public static boolean isIp(String address) {
        Pattern pat = Pattern.compile(IP_REGEX);
        Matcher mat = pat.matcher(address);
        return mat.find();
    }

    public static long castLong(String string) {
        if (string == null || "".equals(string)) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean nameIsLegal(String name) {
        String nameRex = "^[0-9a-zA-Z\\u3400-\\u4DB5\\u4E00-\\u9FA5\\u9FA6-\\u9FBB\\uF900-\\uFA2D\\uFA30-\\uFA6A\\uFA70-\\uFAD9\\u20000-\\u2A6D6\\u2F800-\\u2FA1D\\uFF00-\\uFFEF\\u2E80-\\u2EFF\\u3001-\\u303F\\u31C0-\\u31EF\\u3100-\\u312F]+$";
        Pattern p = Pattern.compile(nameRex);
        Matcher m = p.matcher(name);
        return m.find();
    }

    /**
     * 包含除汉字、英文、数字以外的特殊字符
     */
    public static boolean containsSpecialChar(String str) {
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";
        return !Pattern.matches(pattern, str);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object obj, T defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        return (T) obj;
    }

    public static long[] strArrToLongArr(String[] numbers) {
        long[] longArr = new long[numbers.length];

        for (int i = 0; i < numbers.length; ++i) {
            longArr[i] = Long.parseLong(numbers[i]);
        }
        return longArr;
    }

    public static String strArrToString(String[] contents) {
        StringBuilder sb = new StringBuilder();
        for (String content : contents) {
            sb.append(content);
        }
        return sb.toString();
    }


    public static String intMapToStr(Map<Integer, Integer> map, String sp1, String sp2) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append(sp1).append(v).append(sp2));
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    public static String mapToStr(Map<String, Object> map, String sp1, String sp2) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k.toString()).append(sp1).append(v.toString()).append(sp2));
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    public static List<String> mapToStrList(Map<Integer, Integer> map, String sp1, String sp2) {
        List<String> ret = new ArrayList<>();
        if (map.isEmpty()) {
            return ret;
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (sb.length() <= MAIL_LIMIT_SIZE) {
                sb.append(entry.getKey()).append(sp1).append(entry.getValue()).append(sp2);
            } else {
                ret.add(sb.substring(0, sb.length() - 1));
                sb = new StringBuilder();
            }
        }
        return ret;
    }

    public static String intLongMapToStr(Map<Integer, Long> map, String sp1, String sp2) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append(sp1).append(v).append(sp2));
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    public static String intArrToStr(int[] arr, String split) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i : arr) {
            sb.append(i).append(split);
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    public static String intArrListToStr(List<int[]> list, String sp1, String sp2) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        list.forEach(arr -> sb.append(intArrToStr(arr, sp1)).append(sp2));
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 分割字符串为 int 类型list
     *
     * @param str         : 目标字符串
     * @param delemiter1  : 内部的分割符
     * @param delemiter2: 外部的分隔符
     * @return 二维数组
     */
    public static List<String[]> stringToDoubleList(String str, String delemiter1, String delemiter2) {
        if (null == str || "".equals(str) || "-1".equals(str)) {
            return null;
        }
        List<String[]> arrgs = new ArrayList<>();
        String[] newStrs = str.split(delemiter2);
        for (int i = 0; i < newStrs.length; i++) {
            if ("".equals(newStrs[i].trim())) continue;

            String[] str2 = newStrs[i].trim().split(delemiter1);
            String[] arrg = new String[str2.length];
            for (int j = 0; j < str2.length; j++) {
                arrg[j] = str2[j].trim();
            }
            arrgs.add(arrg);
        }
        return arrgs;
    }


    /**
     * streing转map
     *
     * @param str
     * @param delemiter1
     * @param delemiter2
     * @return
     */
    public static Map<Integer, Integer> stringToIntegerMap(String str, String delemiter1, String delemiter2) {
        if (null == str || "".equals(str) || "-1".equals(str)) return null;
        Map<Integer, Integer> map = new HashMap<>();
        String[] newStrs = str.split(delemiter2);
        for (int i = 0; i < newStrs.length; i++) {
            if ("".equals(newStrs[i].trim())) continue;
            String[] str2 = newStrs[i].trim().split(delemiter1);
            map.put(Integer.parseInt(str2[0]),Integer.parseInt(str2[1]));
        }
        return map;
    }

}
