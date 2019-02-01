package com.eebbk.bfc.sdk.version.demo.servicelib.function.test;

import android.content.Context;

import com.eebbk.bfc.common.app.AppUtils;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.util.Md5Utils;
import com.eebbk.bfc.sdk.version.util.ParamUtils;

import java.io.File;

/**
 * @author hesn
 * @function
 * @date 16-12-29
 * @company 步步高教育电子有限公司
 */

public abstract class AAutoGetTestParameter implements ITestParameter {

    private final Context mContext;
    private static final String APK_SAVE_PATH = VersionConstants.PATH_SD + VersionConstants.DOWNLOAD_APK_FOLDER + File.separator;

    AAutoGetTestParameter(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public String getVersionName() {
        return AppUtils.getVersionName(mContext, getPackageName());
    }

    @Override
    public String getApkMd5() {
        return ParamUtils.getApkMd5(mContext, getPackageName());
    }

    @Override
    public String getMachineId() {
        return ParamUtils.getMachineId(mContext);
    }

    @Override
    public String getDeviceOSVersion() {
        return ParamUtils.getDeviceOSVersion();
    }

    @Override
    public String getDeviceModel() {
        return ParamUtils.getDeviceModel();
    }

    @Override
    public int getVersionCode() {
        return AppUtils.getVersionCode(mContext, getPackageName());
    }

    @Override
    public String getCurrentApkFilePath() {
        return APK_SAVE_PATH;
    }

    @Override
    public String getMd5() {
        return Md5Utils.getMd5ByFile(new File(getCurrentApkFilePath() + getCurrentApkFileName()));
    }
}
