package com.eebbk.bfc.sdk.version.entity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.eebbk.bfc.common.app.AppUtils;
import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.entity.response.DataInfoEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.PatchInfoEntity;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.download.DownloadConstants;
import com.eebbk.bfc.sdk.version.download.DownloadExtraAgent;
import com.eebbk.bfc.sdk.version.patch.algorithm.PatchImplFactory;
import com.eebbk.bfc.sdk.version.util.ParamUtils;

import java.io.File;
import java.io.Serializable;

/**
 * 版本信息集合类，为了兼容，有些旧的属性要保留
 * <br> 服务器返回的版本更新信息
 */
public class Version implements Comparable<Version>, Serializable {
    private static final long serialVersionUID = 3761769793888969771L;
    /**
     * 版本号
     */
    private int versionCode;
    /**
     * 版本名
     */
    private String versionName;
    /**
     * 全量包下载地址
     */
    private String apkUrl;
    /**
     * 全量包md5
     */
    private String apkMd5;
    /**
     * Apk文件名字
     */
    private String apkFileName;
    /**
     * Apk文件路径
     */
    private String apkFilePath;

    /**
     * 模块名，主要为了下载确认
     */
    private String moduleName;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 更新信息
     */
    private String updateinformation;
    /**
     * Apk/patch 描述信息
     */
    private String apkIntroduce;
    /**
     * 版本提示信息
     */
    private String tip;

    /**
     * 服务器资源id(需要回传)
     */
    private int serverFileId;
    /**
     * 包的大小（apk/patch）
     */
    private long apkSize;

    /**
     * 是否有增量包 0标识无 1标识有
     */
    private int patchAvailable;
    /**
     * 升级方式：2为普通提示升级 3静默升级
     */
    private int updateMode;
    /**
     * 增量包下载地址
     */
    private String patchUrl;
    /**
     * 增量包Md5值
     */
    private String patchMd5;
    /**
     * 增量包文件名
     */
    private String patchFileName;
    /**
     * 增量包路径
     */
    private String patchFilePath;
    /**
     * 增量包大小
     */
    private long patchSize;
    /**
     * 当前版本md5
     */
    private String md5;
    /**
     * 机型名
     */
    private String deviceModel;
    /**
     * 系统版本名
     */
    private String deviceOSVersion;
    /**
     * 机器码
     */
    private String machineId;
    /**
     * 是否总是全量升级
     */
    private boolean alwaysFullUpgrade = false;
    /**
     * 当前版本Apk版本名
     * <br> 上报APP升级信息需要
     */
    private String currentVersionName;
    /**
     * 当前版本Apk版本名
     * <br> 上报APP升级信息需要
     */
    private int currentVersionCode;
    /**
     * 当前版本Apk文件名字
     * <br> 差分包合并需要,如果没有则全量升级
     */
    private String currentApkFileName;
    /**
     * 当前版本Apk文件路径
     * <br> 差分包合并需要,如果没有则全量升级
     */
    private String currentApkFilePath;
    /**
     * 差分包合并算法类型
     * <br> 1:百度类型 2:豌豆荚类型
     */
    private int mergeType;

    public Version() {

    }

    /**
     * 获取本地版本信息
     *
     * @param context 上下文
     */
    public Version(Context context) {
        this.versionCode = ParamUtils.getVersionCode(context);
        this.versionName = ParamUtils.getVersionName(context);
        this.moduleName = ParamUtils.getModuleName(context);
        this.packageName = ParamUtils.getPackageName(context);
        this.apkFileName = ParamUtils.getApkFileName(context);
        this.patchFileName = ParamUtils.getPatchFileName(context);
        this.md5 = AppUtils.getSignatureMd5(context);
    }

    /**
     * 将服务器返回信息，转化为提供给应用的版本信息
     *
     * @param entity 服务器返回实体信息
     */
    public Version(DataInfoEntity entity) {
//        VersionInfoEntity apkInfo = entity.getApkInfo();
        PatchInfoEntity patch = entity.getPatch();
        //Http获取的时候已经检查过了，不可能为空
        this.packageName = entity.getPackageName();
        this.versionCode = entity.getApkVersionCode();
        this.versionName = entity.getApkVersion();
        this.apkUrl = entity.getUrl();
        this.apkMd5 = entity.getMd5();
        this.apkIntroduce = entity.getApkIntroduce();
        this.updateinformation = entity.getUpdateInformation();
        this.tip = entity.getTip();
        this.updateMode = entity.getUpdateMode();
        this.apkSize = entity.getApkSize();
        this.serverFileId = entity.getId();
        this.patchAvailable = entity.getPatchAvailable();
        this.moduleName = entity.getPackageName();
        this.apkFileName = ParamUtils.getApkFileName(moduleName);
        this.patchFileName = ParamUtils.getPatchFileName(moduleName);
        this.mergeType = entity.getMergeType();

        //patch是有可能为空的
        if (patch != null) {
            this.patchUrl = patch.getPatchUrl();
            this.patchMd5 = patch.getPatchMd5();
            this.patchSize = patch.getPatchSize();
        }
    }

    public Version(CheckParams checkParams) {
        this.versionCode = checkParams.getVersionCode();
        this.versionName = checkParams.getVersionName();
        this.packageName = checkParams.getPackageName();
        this.md5 = checkParams.getMd5();
        this.currentApkFileName = checkParams.getCurrentApkFileName();
        this.currentApkFilePath = checkParams.getCurrentApkFilePath();
    }

    public Version(ITask task) {
        this.setModuleName(DownloadExtraAgent.getModuleName(task));
        this.setPackageName(DownloadExtraAgent.getPackageName(task));
        this.setApkFileName(task.getStringExtra(DownloadConstants.TASK_TAG_APK_FILE_NAME));
        this.setPatchFileName(task.getStringExtra(DownloadConstants.TASK_TAG_PATCH_FILE_NAME));
        this.setVersionCode(task.getIntExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, 0));
        this.setVersionName(task.getStringExtra(DownloadConstants.TASK_TAG_NEW_VERSION_NAME));
        this.setUpdateinformation(task.getStringExtra(DownloadConstants.TASK_TAG_UPDATE_INFORMATION));
        this.setServerFileId(task.getIntExtra(DownloadConstants.TASK_TAG_SERVER_FILE_ID, -1));
        this.setPatchAvailable(task.getIntExtra(DownloadConstants.TASK_TAG_PATCH_AVAILABLE, VersionConstants.PATCH_UNAVAILABLE));
        this.setPatchMd5(task.getStringExtra(DownloadConstants.TASK_TAG_PATCH_MD5));
        this.setApkUrl(task.getStringExtra(DownloadConstants.TASK_TAG_APK_DOWNLOAD_URL));
        this.setApkMd5(task.getStringExtra(DownloadConstants.TASK_TAG_APK_MD5));
        this.setCurrentApkFileName(task.getStringExtra(DownloadConstants.TASK_TAG_CURRENT_APK_FILE_NAME));
        this.setCurrentApkFilePath(task.getStringExtra(DownloadConstants.TASK_TAG_CURRENT_APK_FILE_PATH));
        this.setMergeType(task.getIntExtra(DownloadConstants.TASK_TAG_PATCH_MERGE_TYPE, 1));
        this.setCurrentVersionCode(task.getIntExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_CODE, 0));
        this.setCurrentVersionName(task.getStringExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_NAME));
        this.setUpdateMode(DownloadExtraAgent.getUpdateMode(task));
        this.setApkSize(DownloadExtraAgent.getFileSize(task));
        this.setApkIntroduce(DownloadExtraAgent.getApkIntroduce(task));
        this.setTip(DownloadExtraAgent.getTip(task));
        this.setPatchSize(DownloadExtraAgent.getPatchSize(task));
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionCode=" + versionCode +
                "\n\n versionName='" + versionName + '\'' +
                "\n\n apkUrl='" + apkUrl + '\'' +
                "\n\n apkMd5='" + apkMd5 + '\'' +
                "\n\n apkFileName='" + apkFileName + '\'' +
                "\n\n apkFilePath='" + apkFilePath + '\'' +
                "\n\n moduleName='" + moduleName + '\'' +
                "\n\n packageName='" + packageName + '\'' +
                "\n\n updateinformation='" + updateinformation + '\'' +
                "\n\n apkIntroduce='" + apkIntroduce + '\'' +
                "\n\n tip='" + tip + '\'' +
                "\n\n serverFileId=" + serverFileId +
                "\n\n apkSize=" + apkSize +
                "\n\n patchAvailable=" + patchAvailable +
                "\n\n updateMode=" + updateMode +
                "\n\n patchUrl='" + patchUrl + '\'' +
                "\n\n patchMd5='" + patchMd5 + '\'' +
                "\n\n patchFileName='" + patchFileName + '\'' +
                "\n\n patchFilePath='" + patchFilePath + '\'' +
                "\n\n patchSize=" + patchSize +
                "\n\n md5='" + md5 + '\'' +
                "\n\n deviceModel='" + deviceModel + '\'' +
                "\n\n deviceOSVersion='" + deviceOSVersion + '\'' +
                "\n\n machineId='" + machineId + '\'' +
                "\n\n alwaysFullUpgrade=" + alwaysFullUpgrade +
                "\n\n currentVersionName='" + currentVersionName + '\'' +
                "\n\n currentVersionCode=" + currentVersionCode +
                "\n\n currentApkFileName='" + currentApkFileName + '\'' +
                "\n\n currentApkFilePath='" + currentApkFilePath + '\'' +
                "\n\n mergeType=" + mergeType +
                '}';
    }

    @Override
    public int compareTo(@NonNull Version another) {
        if (this.getVersionCode() > another.getVersionCode()) {
            return 1;
        } else if (this.getVersionCode() < another.getVersionCode()) {
            return -1;
        }

        return 0;
    }

    private long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    public String getApkIntroduce() {
        return apkIntroduce;
    }

    public void setApkIntroduce(String apkIntroduce) {
        this.apkIntroduce = apkIntroduce;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }


    public int getPatchAvailable() {
        return patchAvailable;
    }

    public String getPatchMd5() {
        return patchMd5;
    }

    public void setPatchMd5(String patchMd5) {
        this.patchMd5 = patchMd5;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public int getServerFileId() {
        return serverFileId;
    }

    public void setServerFileId(int serverFileId) {
        this.serverFileId = serverFileId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(int updateMode) {
        this.updateMode = updateMode;
    }

    /**
     * 获取增量包文件名
     */
    public String getPatchFileName() {
        return patchFileName;
    }

    public void setPatchFileName(String patchFileName) {
        this.patchFileName = patchFileName;
    }

    public long getPatchSize() {
        return patchSize;
    }

    public void setPatchSize(long patchSize) {
        this.patchSize = patchSize;
    }

    public String getPatchFilePath() {
        return patchFilePath;
    }

    public void setPatchFilePath(String patchFilePath) {
        this.patchFilePath = patchFilePath;
    }

    public String getApkFilePath() {
        return apkFilePath;
    }

    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public boolean isPatchAvailable() {
        if (!(!isAlwaysFullUpgrade() && patchAvailable == VersionConstants.PATCH_AVAILABLE)) {
            return false;
        }

        if (!PatchImplFactory.hasPatch(mergeType)) {
            LogUtils.d("", "Nonsupport mergeType == " + mergeType + " patch!");
            return false;
        }

        // 检查apk文件是否存在,不存在则无法增量升级
        if (TextUtils.isEmpty(currentApkFileName) || TextUtils.isEmpty(currentApkFilePath)) {
            boolean isFileExists = FileUtils.isFileExists(VersionConstants.PATH_SD
                    + VersionConstants.DOWNLOAD_APK_FOLDER + File.separator + apkFileName);
            LogUtils.d("", "isFileExists:" + isFileExists);
            if (isFileExists) {
                currentApkFileName = apkFileName;
                currentApkFilePath = VersionConstants.PATH_SD
                        + VersionConstants.DOWNLOAD_APK_FOLDER + File.separator;
                return true;
            }
        } else {
            return FileUtils.isFileExists(currentApkFilePath + currentApkFileName);
        }

        return false;
    }

    public void setPatchAvailable(int patchAvailable) {
        this.patchAvailable = patchAvailable;
    }

    public long getFileSize() {
        return isPatchAvailable() ? getPatchSize() : getApkSize();
    }

    public String getSavePatchFolderName() {
        return isPatchAvailable() ? VersionConstants.DOWNLOAD_PATCH_FOLDER : VersionConstants.DOWNLOAD_APK_FOLDER;
    }

    public String getDownloadUrl() {
        return isPatchAvailable() ? getPatchUrl() : getApkUrl();
    }

    public String getFileName() {
        return isPatchAvailable() ? getPatchFileName() : getApkFileName();
    }

    public String getFileMd5() {
        return isPatchAvailable() ? getPatchMd5() : getApkMd5();
    }

    public void setFilePath(String path) {
        if (isPatchAvailable()) {
            setPatchFilePath(path);
        } else {
            setApkFilePath(path);
        }
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public boolean isAlwaysFullUpgrade() {
        return alwaysFullUpgrade;
    }

    public void setAlwaysFullUpgrade(boolean alwaysFullUpgrade) {
        this.alwaysFullUpgrade = alwaysFullUpgrade;
    }

    public String getCurrentVersionName() {
        return currentVersionName;
    }

    public void setCurrentVersionName(String currentVersionName) {
        this.currentVersionName = currentVersionName;
    }

    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public String getCurrentApkFileName() {
        return currentApkFileName;
    }

    public void setCurrentApkFileName(String currentApkFileName) {
        this.currentApkFileName = currentApkFileName;
    }

    public String getCurrentApkFilePath() {
        return currentApkFilePath;
    }

    public void setCurrentApkFilePath(String currentApkFilePath) {
        this.currentApkFilePath = currentApkFilePath;
    }

    public int getMergeType() {
        return mergeType;
    }

    public void setMergeType(int mergeType) {
        this.mergeType = mergeType;
    }
}
