package com.eebbk.bfc.sdk.version.entity;

import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;

/**
 * @author hesn
 * @function 版本更新检查请求需要的参数
 * @date 16-12-7
 * @company 步步高教育电子有限公司
 */

public class CheckParams extends RequestVersionEntity {
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

    public CheckParams(){

    }

    public CheckParams(Version version){
        this.setMd5(version.getMd5());
        this.setVersionName(version.getVersionName());
        this.setVersionCode(version.getVersionCode());
        this.setPackageName(version.getPackageName());
        this.setCurrentApkFileName(version.getCurrentApkFileName());
        this.setCurrentApkFilePath(version.getCurrentApkFilePath());
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

    @Override
    public String toString() {
        return super.toString() + ",[" +
                "currentApkFileName='" + currentApkFileName + "]," +
                ", [currentApkFilePath='" + currentApkFilePath + "]";
    }
}
