package com.eebbk.bfc.sdk.version.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.eebbk.bfc.common.app.AppUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;

import java.io.File;
import java.util.List;

/**
 * 类说明：  Apk工具类
 */
public class ApkUtils {
    private static final String TAG = "ApkUtils";

    private ApkUtils() {

    }

    /**
     * 获取已安装apk的PackageInfo
     *
     * @param context     上下文
     * @param packageName 应用包名
     * @return PackageInfo
     */
    public static PackageInfo getInstalledApkPackageInfo(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);

        for (PackageInfo packageinfo : apps) {
            String thisName = packageinfo.packageName;
            if (thisName.equals(packageName)) {
                return packageinfo;
            }
        }

        return null;
    }

    /**
     * 判断apk是否已安装
     *
     * @param context     上下文
     * @param packageName 应用包名
     * @return boolean 安装是否成功，true为成功
     */
    public static boolean isInstalled(Context context, String packageName) {
        return AppUtils.isAppInstalled(context, packageName);
    }

    /**
     * 获取已安装Apk文件的源Apk文件
     * 如：/data/app/com.bbk.studyos.launcher.apk
     *
     * @param context     上下文
     * @param packageName 应用包名
     * @return String apk路径字符串
     */
    public static String getSourceApkPath(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            LogUtils.w("The packageName is Null !");
            return null;
        }

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 安装Apk
     *
     * @param context 上下文
     * @param file    目标安装apk所在路径
     */
    public static void installApk(Context context, File file) {
        if (context == null) {
            return;
        }
        LogUtils.v(TAG, "apkPath:" + file.getAbsolutePath());
        AppUtils.installApp(context, file);
    }

    /**
     * 获取未安装APK的包名
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApkFilePackageName(Context context, String apkPath) {
        if(context == null){
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            return pi == null ? null : pi.packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取未安装APK版本名称(versionName)
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApkFileVersionName(Context context, String apkPath) {
        if(context == null){
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            return pi == null ? null : pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取未安装APK版本号(versionCode)
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static int getApkFileVersionCode(Context context, String apkPath) {
        if(context == null){
            return -1;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            return pi == null ? -1 : pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}