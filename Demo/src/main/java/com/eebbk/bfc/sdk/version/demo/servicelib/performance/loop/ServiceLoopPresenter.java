package com.eebbk.bfc.sdk.version.demo.servicelib.performance.loop;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.demo.servicelib.config.ServoceConfig;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.BfcVersionDemoTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.demo.utils.TestUtils;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionDownloadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public class ServiceLoopPresenter implements OnVersionCheckListener, OnVersionDownloadListener {
    private static final String TAG = ServiceLoopPresenter.class.getName();
    private BfcVersion bfcVersion = null;
    private boolean isLoop = false;
    private final OnProgressBarListener mProgressListener;
    private boolean isMultipleRequests;
    private ITestParameter iTestParameter;

    public ServiceLoopPresenter(Context context, OnProgressBarListener l, ITestParameter iTestParameter) {
        initBfcVersion(context, false);
        mProgressListener = l;
        this.iTestParameter = iTestParameter;
    }

    /**
     * 检查指定app
     */
    public void appCheck(Context context) {
        if (isLoop) {
            return;
        }
        check(context);
    }

    private void check(Context context) {
        isLoop = true;
        if(this.isMultipleRequests){
            List<CheckParams> requests = new ArrayList<CheckParams>();
            CheckParams entity = TestUtils.getVersionEntity(iTestParameter);
            requests.add(entity);
            bfcVersion.checkVersion(requests);
        }else{
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
    }

    public void stop() {
        isLoop = false;
        deleteAll();
    }

    public void deleteAll() {
        bfcVersion.deleteAllDownloadTask();
    }

    public void destroy() {
        stop();
        bfcVersion.destroy();
    }

    public void change(Context context, boolean isMultipleRequests) {
        if(this.isMultipleRequests == isMultipleRequests){
            return;
        }
        destroy();
        initBfcVersion(context, isMultipleRequests);
    }

    public void setTestData(ITestParameter iTestParameter){
        this.iTestParameter = iTestParameter;
    }

    @Override
    public void onUpdateReady(Context context, VersionInfo info) {
        LogUtils.i(TAG, "安装信息:" + info.toString());
        if (!isLoop) {
            return;
        }
        deleteAll();
        check(context);
    }

    @Override
    public void onNewVersionChecked(List<Version> newVersions) {

    }

    @Override
    public void onVersionCheckException(String errorCode) {
        stop();
    }

    @Override
    public void onCheckOver() {

    }

    @Override
    public void onDownloadWaiting(ITask task) {
    }

    @Override
    public void onDownloadStarted(ITask task) {

    }

    @Override
    public void onDownloadConnected(ITask task, boolean resuming, long finishedSize, long totalSize) {

    }

    @Override
    public void onDownloading(ITask task, long finishedSize, long totalSize) {
        double per = finishedSize * 100 / (double) totalSize;
        mProgressListener.setProgress((int) per);
    }

    @Override
    public void onDownloadPause(ITask task, String errorCode) {

    }

    @Override
    public void onDownloadRetry(ITask task, int retries, String errorCode, Throwable throwable) {

    }

    @Override
    public void onDownloadFailure(ITask task, String errorCode, Throwable throwable) {

    }

    @Override
    public void onDownloadSuccess(ITask task) {

    }

    private void initBfcVersion(Context context, boolean isMultipleRequests) {
        this.isMultipleRequests = isMultipleRequests;
        bfcVersion = new BfcVersion.Builder()
                .setDebugMode(true)
                .setMultipleRequests(isMultipleRequests)
                .setCacheLog(true)
                .setUrl(ServoceConfig.getUrl())
                .build(context);

        bfcVersion.setOnVersionCheckListener(this);
        bfcVersion.setOnVersionDownloadListener(this);
    }

    interface OnProgressBarListener {
        void setProgress(int progress);
    }

}
