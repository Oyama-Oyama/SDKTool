package com.support.easy;

public class StringUtil {

    public static boolean isEmpty(String src) {
        if (src == null)
            return true;
        if (src.isEmpty())
            return true;
        return false;
    }

    public static boolean equal(String arg1, String arg2) {
        if (isEmpty(arg1) || isEmpty(arg2))
            return false;
        if (arg1.equals(arg2))
            return true;
        return false;
    }

    public static boolean equalIgnoreCase(String arg1, String arg2) {
        if (isEmpty(arg1) || isEmpty(arg2))
            return false;
        if (arg1.equalsIgnoreCase(arg2))
            return true;
        return false;
    }

}