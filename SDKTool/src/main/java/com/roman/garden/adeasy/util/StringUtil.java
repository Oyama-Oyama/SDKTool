package com.roman.garden.adeasy.util;

public class StringUtil {

    public static boolean isEmpty(String src) {
        if (src == null)
            return true;
        if (src.isEmpty())
            return true;
        return false;
    }


}
