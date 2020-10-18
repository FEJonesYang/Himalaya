package com.example.himalaya.utils;

import android.util.Log;

/**
 * @author JonesYang
 * @Data 2020-10-12
 * @Function 打印日志
 */
public class LogUtil {

    public static String sTAG = "LogUtil";
    //控制是否需要控制输出
    public static boolean sIsRelease = false;

    /**
     * 如果是要发布的话，在application里面把这里release一下
     */
    public static void init(String baseTag, boolean isRelease) {
        sTAG = baseTag;
        sIsRelease = isRelease;
    }

    public static void d(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]", content);
        }
    }

    public static void e(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]", content);
        }
    }

    public static void v(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]", content);
        }
    }

    public static void i(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]", content);
        }
    }

    public static void w(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]", content);
        }
    }

}
