package com.eebbk.bfc.core.sdk.version.entity.response;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-8.
 */
public class DataInfoEntity implements Comparable<VersionInfoEntity>, Serializable {

    private static final long serialVersionUID = -8608946159372789742L;

    //应用包名
    private String packageName;

    //Apk描述信息
    private String apkIntroduce;

    //Apk大小
    private int apkSize;

    //升级目标版本的版本名
    private String apkVersion;

    //升级目标版本的版本号
    private int apkVersionCode;

    //服务器资源id(需要回传)
    private int id;

    //升级描述信息
    private String updateInformation;

    //版本提示信息
    private String tip;

    //全量包下载地址
    private String url;

    //全量包Md5值
    private String md5;

    //升级方式：2为普通提示升级 3静默升级
    private int updateMode;

    //是否有增量包 0标识无 1标识有
    private int patchAvailable;

    //patch包信息
    private PatchInfoEntity patch;

    //差分包合并算法类型 1:百度类型 2:豌豆荚类型 3:步步高类型
    private int mergeType;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 获取 Apk描述信息
     *
     * @return String：Apk描述信息
     */
    public String getApkIntroduce() {
        return apkIntroduce;
    }

    public void setApkIntroduce(String apkIntroduce) {
        this.apkIntroduce = apkIntroduce;
    }

    /**
     * 获取apk的大小
     *
     * @return String:Apk大小
     */
    public int getApkSize() {
        return apkSize;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }

    /**
     * 获取升级目标版本的版本名
     *
     * @return String:升级目标版本的版本名
     */
    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    /**
     * 获取APK的版本号
     *
     * @return int:APK的版本号
     */
    public int getApkVersionCode() {
        return apkVersionCode;
    }

    public void setApkVersionCode(int apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
    }

    /**
     * 获取服务器资源id(需要回传)
     *
     * @return int:服务器资源id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取升级描述信息
     *
     * @return String:升级描述信息
     */
    public String getUpdateInformation() {
        return updateInformation;
    }

    public void setUpdateInformation(String updateInformation) {
        this.updateInformation = updateInformation;
    }

    /**
     * 获取版本提示信息
     *
     * @return String:版本提示信息
     */
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     * 获取全量包下载地址
     *
     * @return String:全量包下载地址
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取全量包Md5值
     *
     * @return String:全量包Md5值
     */
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     * 获取升级方式
     *
     * @return int:升级方式：2为普通提示升级 3静默升级
     */
    public int getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(int updateMode) {
        this.updateMode = updateMode;
    }

    /**
     * 获取是否有增量包
     *
     * @return int:是否有增量包 0标识无 1标识有
     */
    public int getPatchAvailable() {
        return patchAvailable;
    }

    public void setPatchAvailable(int patchAvailable) {
        this.patchAvailable = patchAvailable;
    }

    public PatchInfoEntity getPatch() {
        return patch;
    }

    public void setPatch(PatchInfoEntity patch) {
        this.patch = patch;
    }

    public int getMergeType() {
        return mergeType;
    }

    public void setMergeType(int mergeType) {
        this.mergeType = mergeType;
    }

    @Override
    public int compareTo(@NonNull VersionInfoEntity another) {
        if (this.getApkVersionCode() > another.getApkVersionCode()) {
            return 1;
        } else if (this.getApkVersionCode() < another.getApkVersionCode()) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "[apkIntroduce =" + apkIntroduce + "], [apkSize =" + apkSize + "],[apkVersion =" + apkVersion
                + "],[apkVersionCode =" + apkVersionCode + "],[id =" + id + "],[updateInformation =" + updateInformation
                + "],[tip =" + tip + "],[url =" + url + "],[md5 =" + md5 + "],[updateMode =" + updateMode
                + "],[patchAvailable =" + patchAvailable + "]" + ",[patch =" + patch + "]";
    }
}
