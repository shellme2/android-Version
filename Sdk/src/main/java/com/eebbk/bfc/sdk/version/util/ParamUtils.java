package com.eebbk.bfc.sdk.version.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.eebbk.bfc.common.app.AppUtils;
import com.eebbk.bfc.common.devices.DeviceUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.VersionConstants;

import java.io.File;

/**
 * 参数工具类
 * Created by lcg on 16-4-7.
 */
public class ParamUtils {

    private static String MACHINE_ID;
    private static String PACKAGE_NAME;
    private static int VERSION_CODE = -1;
    private static String DEVICE_MODE;
    private static String DEVICE_OS_VERSION;

    private ParamUtils() {

    }

    public static void clean(){
        MACHINE_ID = null;
        PACKAGE_NAME = null;
        VERSION_CODE = -1;
        DEVICE_MODE = null;
        DEVICE_OS_VERSION = null;
    }

    /**
     * 获取当前版本名
     *
     * @param context 上下文
     * @return String: versionName
     */
    public static String getVersionName(Context context) {
        return AppUtils.getVersionName(context);
    }

    /**
     * 获取当前版本号
     *
     * @param context 上下文
     * @return int: versionCode
     */
    public static int getVersionCode(Context context) {
        if(VERSION_CODE < 0){
            VERSION_CODE = AppUtils.getVersionCode(context);
        }
        return VERSION_CODE;
    }

    /**
     * 获取当前包名
     *
     * @param context 上下文
     * @return String
     */
    public static String getPackageName(Context context) {
        if(TextUtils.isEmpty(PACKAGE_NAME)){
            PACKAGE_NAME = AppUtils.getPackageName(context);
        }
        return PACKAGE_NAME;
    }

    /**
     * 获取机型名
     *
     * @return String
     */
    public static String getDeviceModel() {
        if(TextUtils.isEmpty(DEVICE_MODE)){
            DEVICE_MODE = DeviceUtils.getModel();
        }
        return DEVICE_MODE;
    }

    /**
     * 获取系统版本名
     *
     * @return String
     */
    public static String getDeviceOSVersion() {
        if(TextUtils.isEmpty(DEVICE_OS_VERSION)){
            DEVICE_OS_VERSION = Build.VERSION.RELEASE;
        }
        return DEVICE_OS_VERSION;
    }

    /**
     * 获取机器码
     *
     * @return String
     */
    public static String getMachineId(Context context) {
        if(TextUtils.isEmpty(MACHINE_ID)){
            MACHINE_ID = context == null ? "" : DeviceUtils.getMachineId(context);
        }
        return MACHINE_ID;
    }

    /**
     * 获取当前应用APK的Md5
     *
     * @param context 上下文
     * @return String
     */
    public static String getLocalVersionApkMd5(Context context) {
        return getLocalVersionApkMd5(context, getPackageName(context));
    }

    /**
     * 获取安装的目标应用版本apk的md5
     *
     * @param context     上下文
     * @param packageName 目标应用包名
     * @return String
     */
    public static String getLocalVersionApkMd5(Context context, String packageName) {
        String apkFilePath = ApkUtils.getSourceApkPath(context, packageName);
        String fileMd5 = "";
        if (!TextUtils.isEmpty(apkFilePath)) {
            File oldApk = new File(apkFilePath);
            if (oldApk.exists()) {
                fileMd5 = Md5Utils.getMd5ByFile(oldApk);
            } else {
                LogUtils.w("The apk file isn't exist in this path : " + apkFilePath);
            }
        } else {
            LogUtils.w("The apk file path is Illegality !");
        }

        return fileMd5;
    }

    /**
     * 获取应用名
     *
     * @param context 上下文
     * @return String
     */
    public static String getModuleName(Context context) {
        return AppUtils.getAppName(context);
    }

    /**
     * 获取apk文件名
     *
     * @param context 上下文
     * @return String
     */
    public static String getApkFileName(Context context) {
        return getApkFileName(getModuleName(context));
    }

    /**
     * 获取patch文件名
     *
     * @param context 上下文
     * @return String
     */
    public static String getPatchFileName(Context context) {
        return getPatchFileName(getModuleName(context));
    }

    /**
     * 获取apk文件名
     *
     * @param moduleName 模块名
     * @return String
     */
    public static String getApkFileName(String moduleName) {
        return moduleName + VersionConstants.APK_SUFFIX;
    }

    /**
     * 获取patch文件名
     *
     * @param moduleName 模块名
     * @return String
     */
    public static String getPatchFileName(String moduleName) {
        return moduleName + VersionConstants.PATCH_SUFFIX;
    }

    /**
     * 获取文件md5
     *
     * @param file
     * @return
     */
    public static String getMd5(File file) {
        return Md5Utils.getMd5ByFile(file);
    }

    /**
     * 获取签名md5
     * <br> 百度签名md5 + "#" + 豌豆荚签名md5
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getApkMd5(Context context, String packageName) {
        return TextUtils.concat(
                Md5Utils.getBaiduMd5(context, packageName),
                "#",
                Md5Utils.getWanDouJiaMd5(context, packageName)
        ).toString();
    }

}
