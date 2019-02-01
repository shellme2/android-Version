/*
 * 文 件 名:  PatchParam.java
 * 版    权:  广东小天才科技有限公司
 * 描    述:  <文件主要内容描述>
 * 作    者:  lcg
 * 创建时间:  2016-3-3 下午2:53:58
 */
package com.eebbk.bfc.sdk.version.patch;

import android.content.Context;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.download.DownloadInfo;
import com.eebbk.bfc.sdk.version.util.Utils;

import java.io.File;


/**
 * 增量升级必须的参数集合类
 *
 * @author lcg
 * @version [1.0.0.0, 2016-3-3]
 */
public class PatchParam {

    private Context context;
    /**
     * 要进行增量升级的应用包名
     */
    private String pkg;

    /*
     * 目标版本MD5值，从服务器获取
     */
    private String remoteVersionMD5;
    /*
     * 合成新APK保存路径
     */
    private String newApkPath;
    /*
     * 下载的差分包路径
     */
    private String patchPath;

    private DownloadInfo downloadInfo;

    /**
     * 私有化
     */
    private PatchParam() {

    }

    public PatchParam(Context context, DownloadInfo downloadInfo) {
        this.context = context;
        this.downloadInfo = downloadInfo;
        this.pkg = downloadInfo.getPackageName();
        this.remoteVersionMD5 = downloadInfo.getPatchMd5();
        this.patchPath = downloadInfo.getPatchFilePath();
        this.newApkPath = getNewApkFilePath(downloadInfo);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    /**
     * 返回 升级目标应用包名
     *
     * @return String
     */
    public String getPkg() {
        return pkg;
    }

    /**
     * 对升级目标应用包名进行赋值
     *
     * @param pkg 包名
     */
    public void setPkg(String pkg) {
        this.pkg = pkg;
    }


    /**
     * 返回升级目标版本MD5值
     *
     * @return 服务器apk md5
     */
    public String getRemoteVersionMD5() {
        return remoteVersionMD5;
    }

    /**
     * 对升级目标版本MD5进行赋值
     *
     * @param remoteVersionMD5 服务器apk md5
     */
    public void setRemoteVersionMD5(String remoteVersionMD5) {
        this.remoteVersionMD5 = remoteVersionMD5;
    }

    /**
     * 返回 合成新APK的存储路径
     *
     * @return 新apk存储路径
     */
    public String getNewApkPath() {
        return newApkPath;
    }

    /**
     * 对合成新APK的存储路径进行赋值
     *
     * @param newApkPath 新apk存储路径
     */
    public void setNewApkPath(String newApkPath) {
        this.newApkPath = newApkPath;
    }

    /**
     * 返回 差分包存储路径
     *
     * @return 差分包存储路径
     */
    public String getPatchPath() {
        return patchPath;
    }

    /**
     * 对差分包存储路径进行赋值
     *
     * @param patchPath 差分包存储路径
     */
    public void setPatchPath(String patchPath) {
        this.patchPath = patchPath;
    }


    /**
     * 检查Task参数是否可用
     *
     * @return true:参数正确，可用；false:参数错误，不可用
     */
    public boolean checkParamsAvailable() {

        if (TextUtils.isEmpty(pkg)) {
            LogUtils.w("PatchApkTask need package name");
            return false;
        }

        if (TextUtils.isEmpty(patchPath)) {
            LogUtils.w("PatchApkTask need patchPath");
            return false;
        }

        if (TextUtils.isEmpty(newApkPath)) {
            LogUtils.w("PatchApkTask need remote newApkPath");
            return false;
        }

        if (TextUtils.isEmpty(remoteVersionMD5)) {
            LogUtils.w("PatchApkTask need remote version md5");
            return false;
        }

        return true;
    }

    private String getNewApkFilePath(DownloadInfo downloadInfo) {
        String apkName = downloadInfo.getApkFileName();
        LogUtils.d(VersionConstants.TAG, "apkName = " + apkName);
        return Utils.creatorNewApkDirectory(downloadInfo) + File.separator + apkName;
    }

}
