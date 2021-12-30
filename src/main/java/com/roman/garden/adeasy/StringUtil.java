package com.roman.garden.adeasy;

final class StringUtil {

    public static boolean isEmpty(String src){
        if (src == null)
            return true;
        if (src.trim().length() == 0)
            return true;
        return false;
    }

    public static boolean equal(String param1, String param2){
        if (!isEmpty(param1) && !isEmpty(param2)){
            if (param1.equals(param2))
                return true;
        }
        return false;
    }

}
