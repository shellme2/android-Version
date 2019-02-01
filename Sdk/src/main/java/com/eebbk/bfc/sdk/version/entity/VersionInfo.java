/*
 * 文 件 名:  VersionInfo.java
 * 版    权:  广东小天才科技有限公司
 * 描    述:  <文件主要内容描述>
 * 作    者:  lcg
 * 创建时间:  2016-6-16 上午11:49:44
 */
package com.eebbk.bfc.sdk.version.entity;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;

import java.io.File;

/**
 * 回调传递给app的安装apk版本信息封装类
 *
 * @author lcg
 * @version [1.0.0.0, 2016-6-16]
 */
public class VersionInfo {
    private int downloadId = -1;
    private String curVersionName;
    private int curVersionCode;
    private String remoteVersionName;
    private int remoteVersionCode;
    private File apkFile;
    private String information;
    private String updateinformation;
    private Context context;
    private String md5;
    private String packageName;
    /**
     * 升级方式：2为普通提示升级 3静默升级
     */
    private int updateMode;

    public VersionInfo(Context context, int downloadId, String remoteVersionName, int remoteVersionCode,
                       String information, File apkFile, int curVersionCode, String curVersionName,
                       String md5, String packageName, int updateMode) {
        this.context = context;
        this.curVersionCode = curVersionCode;
        this.curVersionName = curVersionName;
        this.downloadId = downloadId;
        this.remoteVersionCode = remoteVersionCode;
        this.remoteVersionName = remoteVersionName;
        this.information = information;
        this.apkFile = apkFile;
        this.md5 = md5;
        this.packageName = packageName;
        this.updateMode = updateMode;
    }

    public VersionInfo(Context context, int downloadId, String remoteVersionName, int remoteVersionCode,
                       String information, File apkFile, int curVersionCode, String curVersionName,
                       String md5, String packageName, int updateMode, String updateinformation) {
        this.context = context;
        this.curVersionCode = curVersionCode;
        this.curVersionName = curVersionName;
        this.downloadId = downloadId;
        this.remoteVersionCode = remoteVersionCode;
        this.remoteVersionName = remoteVersionName;
        this.information = information;
        this.apkFile = apkFile;
        this.md5 = md5;
        this.packageName = packageName;
        this.updateMode = updateMode;
        this.updateinformation = updateinformation;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getCurVersionName() {
        return curVersionName;
    }

    public void setCurVersionName(String curVersionName) {
        this.curVersionName = curVersionName;
    }

    public int getCurVersionCode() {
        return curVersionCode;
    }

    public void setCurVersionCode(int curVersionCode) {
        this.curVersionCode = curVersionCode;
    }

    public String getRemoteVersionName() {
        return remoteVersionName;
    }

    public void setRemoteVersionName(String remoteVersionName) {
        this.remoteVersionName = remoteVersionName;
    }

    public int getRemoteVersionCode() {
        return remoteVersionCode;
    }

    public void setRemoteVersionCode(int remoteVersionCode) {
        this.remoteVersionCode = remoteVersionCode;
    }

    public File getApkFile() {
        return apkFile;
    }

    public void setApkFile(File apkFile) {
        this.apkFile = apkFile;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUpdateinformation() {
        return updateinformation;
    }

    public void setUpdateinformation(String updateinformation) {
        this.updateinformation = updateinformation;
    }

    public void deleteFile() {
        if (context != null && downloadId != -1 && apkFile != null) {
            //mark
//			VersionReceiver.delete(context, downloadId, apkFile);
            LogUtils.d("delete ignore version file !");
        }
    }

    /**
     * 升级方式：2为普通提示升级 3静默升级
     */
    public int getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(int updateMode) {
        this.updateMode = updateMode;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "downloadId=" + downloadId +
                ", curVersionName='" + curVersionName + '\'' +
                ", curVersionCode=" + curVersionCode +
                ", remoteVersionName='" + remoteVersionName + '\'' +
                ", remoteVersionCode=" + remoteVersionCode +
                ", apkFile=" + apkFile +
                ", information='" + information + '\'' +
                ", updateinformation='" + updateinformation + '\'' +
                ", context=" + context +
                ", md5='" + md5 + '\'' +
                ", packageName='" + packageName + '\'' +
                ", updateMode=" + updateMode +
                '}';
    }
}
