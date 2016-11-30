package com.xiaoluo.michaelutil.utils;

import android.text.TextUtils;

/**
 * Log工具打印类 使用前设置是否打印isDebug()
 */
public class ILog {
    public static boolean LOGON = true;
    public static String Tag = "ILog";

    /**
     * 是否打印日志
     *
     * @param isDebug void
     */
    public static void isDebug(boolean isDebug) {
        LOGON = isDebug;
    }

    public static void d(String msg) {
        d(Tag, msg);
    }

    public static void d(String Tag, String msg) {
        if (LOGON) {
            if (!TextUtils.isEmpty(msg)) {
                android.util.Log.d(Tag, msg);
            }
        }
    }

    public static void e(String msg) {
        e(Tag, msg);
    }

    public static void e(String Tag, String msg) {
        if (LOGON) {
            if (!TextUtils.isEmpty(msg)) {
                android.util.Log.e(Tag, msg);
            }
        }
    }

    public static void e(Throwable e) {
        e(Tag, "", e);
    }

    public static void e(String Tag, String msg, Throwable e) {
        if (e == null) return;
        if (LOGON) {
            android.util.Log.e(Tag, msg, e);
        } else {//写入文件
            write();
        }
    }

    private static void write() {
    }


    public static void i(String msg) {
        i(Tag, msg);
    }

    public static void i(String Tag, String msg) {
        if (LOGON) {
            if (!TextUtils.isEmpty(msg)) {
                android.util.Log.i(Tag, msg);
            }
        }
    }

    public static void v(String msg) {
        v(Tag, msg);
    }

    public static void v(String Tag, String msg) {
        if (LOGON) {
            if (!TextUtils.isEmpty(msg)) {
                android.util.Log.v(Tag, msg);
            }
        }
    }


    public static void w(String msg) {
        w(Tag, msg);
    }

    public static void w(String Tag, String msg) {
        if (LOGON) {
            if (!TextUtils.isEmpty(msg)) {
                android.util.Log.w(Tag, msg);
            }
        }
    }
}
