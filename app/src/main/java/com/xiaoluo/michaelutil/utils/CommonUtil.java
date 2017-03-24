package com.xiaoluo.michaelutil.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 描述：通用方法
 * 作者：Micheal
 * 修改时间：2016/11/30
 */

public class CommonUtil {
    /**
     * 判断是否锁屏
     *
     * @param context
     * @return
     */
    public static boolean isScreenLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return !powerManager.isScreenOn();
    }

    /**
     * 判断应用是否在后台运行
     *
     * @param context
     */
    public static boolean isAppRuningBack(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得指定时间的指定格式日期
     *
     * @param style yyyy-MM-dd HH:mm:ss.SSS
     * @param time
     * @return String
     */
    public static String getDate(String style, long time) {
        if (style == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(style, Locale.CHINA);
        return sdf.format(new Date(time));
    }

    /**
     * 获得软件当前版本
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            versionName = "";
        }
        return versionName;
    }

    /**
     * 获取应用VersionCode
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            versionCode = -1;
        }
        return versionCode;
    }

    /**
     * 隐藏软键盘
     *
     * @param context void
     */
    public static void hideSoftInput(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        View focus = context.getCurrentFocus();
        if (focus != null) {
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        return wifiMac;
    }

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void makeCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 获取资源文件
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(String fileName, Application context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder Result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                Result.append(line);
            }
            return Result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
