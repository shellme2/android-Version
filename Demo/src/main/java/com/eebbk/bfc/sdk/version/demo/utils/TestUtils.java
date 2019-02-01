package com.eebbk.bfc.sdk.version.demo.utils;

import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.entity.CheckParams;

/**
 * 测试数据
 * Created by lcg on 16-4-29.
 */
public class TestUtils {

    public static final boolean isMonkeyTest = true;

    public static CheckParams getVersionEntity(ITestParameter iTestParameter) {
        CheckParams entity = new CheckParams();
        if(iTestParameter == null){
            return entity;
        }
        entity.setVersionName(iTestParameter.getVersionName());
        entity.setVersionCode(iTestParameter.getVersionCode());
        entity.setPackageName(iTestParameter.getPackageName());
        entity.setMd5(iTestParameter.getMd5());
        entity.setApkMd5(iTestParameter.getApkMd5());
        entity.setMachineId(iTestParameter.getMachineId());
        entity.setDeviceModel(iTestParameter.getDeviceModel());
        entity.setDeviceOSVersion(iTestParameter.getDeviceOSVersion());
        entity.setCurrentApkFileName(iTestParameter.getCurrentApkFileName());
        entity.setCurrentApkFilePath(iTestParameter.getCurrentApkFilePath());
        return entity;
    }

    public static RequestVersionEntity getRequestVersionEntity(){
        RequestVersionEntity entity = new RequestVersionEntity();
        entity.setPackageName(getPackageName());
        entity.setMd5(getMd5());
        entity.setVersionCode(getVersionCode());
        entity.setVersionName(getVersionName());
        return entity;
    }

    public static RequestReportEntity getRequestReportEntity(){
        //mark
//        RequestVersionEntity entity = new RequestVersionEntity(context);
        RequestReportEntity entity = new RequestReportEntity();
        entity.setId(111);
        entity.setNewVersionCode(22);
        entity.setNewVersionName("2.0.0");
        entity.setStateCode(Constants.REPORT_UPDATE_SUCCESS);

        entity.setVersionName("1.0");
        entity.setVersionCode(1);
        entity.setPackageName("com.eebbk.bfc.app.versionmanagerdemo");
        return entity;
    }

    public static String getPackageName(){
        return "com.baidu.wenku";
    }

    public static String getMd5(){
        return "0f3c509eef614432e414ce9d37f00c80";
    }

    public static String getApkMd5(){
        return "58dac2df3815deefbb1d2cedc9ad160c";
    }

    public static String getMachineId(){
        return "M123456";
    }

    public static String getDeviceOSVersion(){
        return "V1.0";
    }

    public static String getDeviceModel(){
        return "Get";
    }

    public static int getVersionCode(){
        return 37;
    }

    public static String getVersionName(){
        return "3.0.2";
    }
}
