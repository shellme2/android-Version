package com.eebbk.bfc.sdk.version.demo.servicelib.function.test;

import android.content.Context;

/**
 * @author hesn
 * @function
 * @date 16-12-29
 * @company 步步高教育电子有限公司
 */

public class BfcVersionDemoTestPImpl extends AAutoGetTestParameter{

    public BfcVersionDemoTestPImpl(Context context){
        super(context);
    }

    @Override
    public String getPackageName() {
        return "com.eebbk.bfc.sdk.version.demo";
    }

    /**
     * 文件名
     * <br> 测试时候需要将同名apk拷贝到 getCurrentApkFilePath() 路径下,用于差分包测试
     *
     * @return
     */
    @Override
    public String getCurrentApkFileName() {
        return "BfcVersionDemo_9.apk";
    }

    @Override
    public String getMd5() {
        return "D2E7EF5F040C65C4AC703F2189927E81";
    }

    @Override
    public int getVersionCode() {
        return 9;
    }

    @Override
    public String getVersionName() {
        return "2.1.2-beta";
    }
}
