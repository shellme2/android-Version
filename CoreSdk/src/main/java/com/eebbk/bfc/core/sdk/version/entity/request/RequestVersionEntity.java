package com.eebbk.bfc.core.sdk.version.entity.request;

/**
 * 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public class RequestVersionEntity extends RequestEntity{

    /**
     * 计算签名所得的 MD5值
     */
    private String md5;
    /**
     * 应用的 md5值
     */
    private String apkMd5;

    public RequestVersionEntity(){

    }

//    public RequestVersionEntity(Context context) {
//        super(context);
//        setMd5(ParamUtils.getLocalVersionApkMd5(context));
//    }

    /**
     * 当前版本APK md5值
     */
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    @Override
    public String toString() {
        return super.toString()
                + ",[md5 =" + md5 + "]"
                + ",[apkMd5 =" + apkMd5 + "]";
    }
}
