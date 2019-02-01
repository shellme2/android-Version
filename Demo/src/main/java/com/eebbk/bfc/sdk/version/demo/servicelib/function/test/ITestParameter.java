package com.eebbk.bfc.sdk.version.demo.servicelib.function.test;

/**
 * @author hesn
 * @function 测试数据接口
 * @date 16-12-29
 * @company 步步高教育电子有限公司
 */

public interface ITestParameter {

    String getPackageName();

    String getMd5();

    String getApkMd5();

    String getMachineId();

    String getDeviceOSVersion();

    String getDeviceModel();

    int getVersionCode();

    String getVersionName();

    String getCurrentApkFileName();

    String getCurrentApkFilePath();
}
