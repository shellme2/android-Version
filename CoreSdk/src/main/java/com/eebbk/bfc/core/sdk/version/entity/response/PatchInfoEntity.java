package com.eebbk.bfc.core.sdk.version.entity.response;

/**
 * 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public class PatchInfoEntity {

    // 增量包下载地址
    private String patchUrl;
    // 合并后校验的Md5值
    private String patchMd5;

    // 增量包大小
    private long patchSize;
    /**
     * 获取增量包下载地址
     *
     * @return String：增量包下载地址
     */
    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    /**
     * 获取增量包Md5值
     *
     * @return String：增量包Md5值
     */
    public String getPatchMd5() {
        return patchMd5;
    }

    public void setPatchMd5(String patchMd5) {
        this.patchMd5 = patchMd5;
    }

    public long getPatchSize() {
        return patchSize;
    }

    public void setPatchSize(long patchSize) {
        this.patchSize = patchSize;
    }

    @Override
    public String toString() {
        return "[patchUrl =" + patchUrl + "]" + "],[patchMd5 =" + patchMd5 + "],[patchSize =" + patchSize+ "]";
    }
}
