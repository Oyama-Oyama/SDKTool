package com.roman.garden.adeasy.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

public class CacheUtil {

    public static void init(@NonNull Context context){
        MMKV.initialize(context);
    }

    private static MMKV getCacheInstance(){
        return MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, "cache");
    }

    public static void putValue(@NonNull String key, @NonNull Object value){
        if (value instanceof Boolean){
            getCacheInstance().encode(key, (boolean)value);
        } else if (value instanceof String){
            getCacheInstance().encode(key, String.valueOf(value));
        } else if (value instanceof Integer){
            getCacheInstance().encode(key, (int)value);
        } else if(value instanceof Long){
            getCacheInstance().encode(key, (long)value);
        } else if (value instanceof Float){
            getCacheInstance().encode(key, (float)value);
        } else if (value instanceof Double){
            getCacheInstance().encode(key, (double)value);
        }
    }

    public static boolean getValue(@NonNull String key, boolean def){
        return getCacheInstance().decodeBool(key, def);
    }

    public static String getValue(@NonNull String key, String def){
        return getCacheInstance().decodeString(key, def);
    }

    public static int getValue(@NonNull String key, int def){
        return getCacheInstance().decodeInt(key, def);
    }

    public static long getValue(@NonNull String key, long def){
        return getCacheInstance().decodeLong(key, def);
    }

    public static float getValue(@NonNull String key, float def){
        return getCacheInstance().decodeFloat(key, def);
    }

    public static double getValue(@NonNull String key, double def){
        return getCacheInstance().decodeDouble(key, def);
    }




}
