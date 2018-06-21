package main.java.com.buildsim.util;

public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String checkNullAndEmpty(String str, String defVal) {
        if (isNullOrEmpty(str)) {
            return defVal;
        }
        return str;
    }
}
