package com.eebbk.bfc.sdk.version.demo.servicelib.function;

import android.content.Context;
import android.text.TextUtils;

import com.eebbk.bfc.common.app.SharedPreferenceUtils;
import com.eebbk.bfc.common.app.ToastUtils;
import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.demo.servicelib.config.ServoceConfig;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.BfcVersionDemoTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.demo.utils.TestUtils;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public class ServiceParameterPresenter implements OnVersionCheckListener {
    private static final String TAG = ServiceParameterPresenter.class.getName();
    private BfcVersion bfcVersion = null;
    private boolean isMultipleRequests;
    private boolean isGetNewVersion4Ignore = true;
    private OnParameterListener l;
    private static final String SEPARATOR = "\r\n";
    private static final String SP_NAME = "commondata";

    public ServiceParameterPresenter(Context context, OnParameterListener onParameterListener) {
        l = onParameterListener;
        initBfcVersion(context, isMultipleRequests);
    }

    public void change(Context context, boolean isMultipleRequests) {
        if (this.isMultipleRequests == isMultipleRequests) {
            return;
        }
        destroy();
        initBfcVersion(context, isMultipleRequests);
    }

    private void initBfcVersion(Context context, boolean isMultipleRequests) {
        this.isMultipleRequests = isMultipleRequests;
        bfcVersion = new BfcVersion.Builder()
                .autoDownload(ServoceConfig.isAutoDownload())
                .alwaysFullUpgrade(ServoceConfig.isAlwayFullUpgrade())
                .setDebugMode(ServoceConfig.isDebugMode())
                .setCheckStrategy(ServoceConfig.getCheckStrategy())
                .setMultipleRequests(this.isMultipleRequests)
                .autoReloadDownloadFailedTask(ServoceConfig.isAutoReloadDownloadFailedTask())
                .autoReloadCheckFailedTask(ServoceConfig.isAutoReloadCheckFailedTask())
                .setCacheLog(true)
                .setUrl(ServoceConfig.getUrl())
                .build(context);

        bfcVersion.setOnVersionCheckListener(this);
    }

    public void setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion) {
        bfcVersion.setRequestHead(machineId, accountId, apkPackageName
                , apkVersionCode, deviceModel, deviceOSVersion);
    }

    /**
     * 检查当前app
     */
    public void appCheck(Context context) {
//        bfcVersion.checkVersion();
        List<CheckParams> localVersionList = new ArrayList<CheckParams>();
        CheckParams checkParams = new CheckParams(bfcVersion.getLocalVersion());
        ITestParameter iTestParameter = new BfcVersionDemoTestPImpl(context);
        checkParams.setMd5(iTestParameter.getMd5());
        checkParams.setVersionName(iTestParameter.getVersionName());
        checkParams.setVersionCode(iTestParameter.getVersionCode());
        checkParams.setCurrentApkFileName(iTestParameter.getCurrentApkFileName());
        checkParams.setCurrentApkFilePath(iTestParameter.getCurrentApkFilePath());
        localVersionList.add(checkParams);
        bfcVersion.checkVersion(localVersionList);
    }

//    /**
//     * 检查指定app
//     *
//     * @param params
//     */
//    public void appCheck(String... params) {
//        List<CheckParams> requests = new ArrayList<CheckParams>();
//        CheckParams entity = getVersionEntity(params);
//        requests.add(entity);
//        bfcVersion.checkVersion(requests);
//    }

    public void appChcek(List<ITestParameter> mITestParameters) {
        if (ListUtils.isEmpty(mITestParameters)) {
            return;
        }
        bfcVersion.checkVersion(getVersionEntity(mITestParameters));
    }

    public void localReport() {
        bfcVersion.reportUpdate(bfcVersion.getLocalVersion(), Constants.REPORT_UPDATE_SUCCESS, "上报成功");
    }

    public void appReport() {
        localReport();
    }

    public void deleteAll() {
        bfcVersion.deleteAllDownloadTask();
    }

    public void destroy() {
        bfcVersion.destroy();
    }

    public List<CheckParams> getVersionEntity(List<ITestParameter> mITestParameters) {
        List<CheckParams> list = new ArrayList<CheckParams>();
        for (ITestParameter params : mITestParameters) {
            if (params == null) {
                continue;
            }
            list.add(TestUtils.getVersionEntity(params));
        }
        return list;
    }

    private CheckParams getVersionEntity(String... params) {
        CheckParams entity = new CheckParams();
        entity.setVersionName(params[0]);
        int versionCode = -1;
        try {
            versionCode = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        entity.setVersionCode(versionCode);
        entity.setPackageName(params[2]);
        entity.setMd5(params[3]);
        entity.setApkMd5(params[4]);
        entity.setMachineId(params[5]);
        entity.setDeviceModel(params[6]);
        entity.setDeviceOSVersion(params[7]);
        entity.setCurrentApkFileName(params[8]);
        entity.setCurrentApkFilePath(params[9]);

        return entity;
    }

    public boolean isGetNewVersion4Ignore() {
        return isGetNewVersion4Ignore;
    }

    public void setGetNewVersion4Ignore(boolean getNewVersion4Ignore) {
        isGetNewVersion4Ignore = getNewVersion4Ignore;
    }

    public void ignoreVersion(Context context, String packageName, int versionCode) {
        if (TextUtils.isEmpty(packageName)) {
            ToastUtils.getInstance(context).l("包名不能为空").show();
            return;
        }
        bfcVersion.deleteDownloadTask(packageName);
        bfcVersion.ignoreVersion(packageName, versionCode);
    }

    public void logIgnore(Context context) {
        Map<String, ?> map = SharedPreferenceUtils.getInstance(context, SP_NAME).getSharedPreference().getAll();
        if (map == null || map.isEmpty()) {
            LogUtils.i(TAG, "暂无忽略版本信息");
            return;
        }
        StringBuilder sb = new StringBuilder(200);
        sb.append("*** 忽略版本 start ***");
        sb.append(SEPARATOR);
        sb.append(SEPARATOR);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            sb.append("包名:");
            sb.append(entry.getKey());
            sb.append(SEPARATOR);
            sb.append("版本:");
            sb.append(entry.getValue());
            sb.append(SEPARATOR);
            sb.append(SEPARATOR);
        }
        sb.append("*** 忽略版本 end ***");
        LogUtils.i(TAG, sb.toString());
    }

    public void clearIgnore(Context context) {
        SharedPreferenceUtils.getInstance(context, SP_NAME).clear();
    }

    @Override
    public void onUpdateReady(Context context, VersionInfo info) {
        LogUtils.i(TAG, "安装信息:" + info.toString());
        ignoreCallback(info.getPackageName(), info.getRemoteVersionCode());
        if(!TestUtils.isMonkeyTest){
            bfcVersion.installApk(context, info);
        }
    }

    @Override
    public void onNewVersionChecked(List<Version> newVersions) {
        LogUtils.i(TAG, "有更新:" + newVersions);
        if (!ListUtils.isEmpty(newVersions)) {
            Version version = newVersions.get(0);
            ignoreCallback(version.getPackageName(), version.getVersionCode());
        }
    }

    @Override
    public void onVersionCheckException(String errorCode) {

    }

    @Override
    public void onCheckOver() {

    }

    private void ignoreCallback(String packageName, int version) {
        if (l == null) {
            return;
        }
        if (!isGetNewVersion4Ignore) {
            return;
        }
        l.setIgnoreInfo(packageName, version);
    }

    interface OnParameterListener {
        void setIgnoreInfo(String packageName, int version);
    }
}
