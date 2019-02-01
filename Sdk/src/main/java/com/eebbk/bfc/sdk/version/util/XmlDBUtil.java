package com.eebbk.bfc.sdk.version.util;

import android.content.Context;

import com.eebbk.bfc.common.app.SharedPreferenceUtils;

/**
 * 保存简单的key-value数据到xml文件
 *
 * @author heweiyun
 * @version [1.0.0.0, 2014-4-11]
 */
class XmlDBUtil {
    private static final String PREF_NAME = "commondata";

    public static void put(Context context, String key, String value) {
        SharedPreferenceUtils.getInstance(context, PREF_NAME).put(key, value);
    }

    public static void put(Context context, String key, int value) {
        SharedPreferenceUtils.getInstance(context, PREF_NAME).put(key, value);
    }

    public static void put(Context context, String key, long value) {
        SharedPreferenceUtils.getInstance(context, PREF_NAME).put(key, value);
    }

    public static void put(Context context, String key, float value) {
        SharedPreferenceUtils.getInstance(context, PREF_NAME).put(key, value);
    }

    public static void put(Context context, String key, boolean value) {
        SharedPreferenceUtils.getInstance(context, PREF_NAME).put(key, value);
    }

    public static String get(Context context, String key, String defValue) {
        return SharedPreferenceUtils.getInstance(context, PREF_NAME).get(key, defValue);
    }

    public static long get(Context context, String key, long defValue) {
        return SharedPreferenceUtils.getInstance(context, PREF_NAME).get(key, defValue);
    }

    public static int get(Context context, String key, int defValue) {
        return SharedPreferenceUtils.getInstance(context, PREF_NAME).get(key, defValue);
    }

    public static boolean get(Context context, String key, boolean defValue) {
        return SharedPreferenceUtils.getInstance(context, PREF_NAME).get(key, defValue);
    }

    public static float get(Context context, String key, float defValue) {
        return SharedPreferenceUtils.getInstance(context, PREF_NAME).get(key, defValue);
    }

}
