package com.eebbk.bfc.core.sdk.version.entity.request;

//import com.eebbk.bfc.core.sdk.version.util.ParamUtils;

/**
 * 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public abstract class RequestEntity{
    /**
     * 当前应用版本名
     */
    private String versionName;
    /**
     * 当前应用版本号
     */
    private int versionCode;
    /**
     * 应用包名
     */
    private String packageName;

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

    RequestEntity(){

    }

//    RequestEntity(Context context) {
//        setDeviceModel(ParamUtils.getDeviceModel());
//        setDeviceOSVersion(ParamUtils.getDeviceOSVersion());
//        setMachineId(ParamUtils.getMachineId());
//        setVersionName(ParamUtils.getVersionName(context));
//        setVersionCode(ParamUtils.getVersionCode(context));
//        setPackageName(ParamUtils.getPackageName(context));
//    }

    /**
     * 当前应用版本名
     */
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 当前应用版本号
     */
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    @Override
    public String toString() {
        return ",[versionName =" + versionName + "],[versionCode =" + versionCode + "],[packageName =" + packageName + "]";
    }
}
