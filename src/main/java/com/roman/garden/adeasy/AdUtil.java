package com.roman.garden.adeasy;

import androidx.annotation.NonNull;

final class AdUtil {

    public static boolean isAdIdEmpty(@NonNull AdItem src){
        if (src != null && src.getAdId() != null && src.getAdId().length() > 0)
            return false;
        return true;
    }

    public static boolean isEmpty(@NonNull String src){
        if (src != null && src.length() > 0)
            return false;
        return true;
    }


}
