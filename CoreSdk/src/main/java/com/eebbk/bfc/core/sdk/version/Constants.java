package com.eebbk.bfc.core.sdk.version;

/**
 * 常量类
 */
public class Constants {

    /**
     * 获取增量升级应用数超过10,服务器只会返回10个升级应用信息
     */
    public static final int CHECK_VERSION_LIST_LIMIT = 10;
    /**
     * 上报升级成功：值不可更改，和服务器约定好的
     */
    public static final int REPORT_UPDATE_SUCCESS = 0;
    /**
     * 上报升级失败：值不可更改，和服务器约定好的
     */
    public static final int REPORT_UPDATE_FAILED = -1;
    /**
     * 允许请求的网络环境:wifi
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 允许请求的网络环境:移动网络
     */
    public static final int NETWORK_MOBILE = 1 << 1;
    /**
     * 允许请求的网络环境:2G已经网络
     * <br> M2000之前有2G网络下无法通话的情况,所以2G单独出来一个设置,有需要的自己设置,默认是2G可以访问
     */
    public static final int NETWORK_MOBILE_2G = 1 << 2;
    /**
     * 允许请求的网络环境:默认使用网络
     */
    public static final int NETWORK_DEFAULT = NETWORK_WIFI | NETWORK_MOBILE ;

    // http://jiajiaoji.eebbk.net/appUpdate/apkInfo/getNewApkInfo 参数,大小写跟新接口的不一致
    public static final String PARAMS_DEVICE_MODEL = "devicemodel";
    public static final String PARAMS_DEVICE_OS_VERSION = "deviceosversion";
    public static final String PARAMS_MACHINE_ID = "machineId";
    public static final String PARAMS_VERSION_NAME = "versionName";
    public static final String PARAMS_PACKAGE_NAME = "packageName";
    public static final String PARAMS_VERSION_CODE = "versionCode";

    public static final String ERROR_MSG_UNKNOWN = "未知错误";

    private Constants(){

    }
}