package com.eebbk.bfc.core.sdk.version;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.eebbk.bfc.common.devices.NetUtils;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.core.sdk.version.error.BfcVersionHttpError;
import com.eebbk.bfc.core.sdk.version.error.CoreErrorCode;
import com.eebbk.bfc.core.sdk.version.url.IUrl;
import com.eebbk.bfc.core.sdk.version.url.UrlReleaseImpl;
import com.eebbk.bfc.core.sdk.version.util.HttpUtils;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.NetworkUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.http.config.BfcHttpConfigure;

import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public class BfcCoreVersionImpl implements BfcCoreVersion {

    private static final String TAG = "BfcCoreVersionImpl";
    private CoreSettings settings;
    private IUrl mUrl = null;
    private Context mContext;

    public BfcCoreVersionImpl(Context context, CoreSettings settings) {
        if (context == null) {
            throw new NullPointerException("BfcCoreVersion context cannot be null");
        }
        this.mContext = context.getApplicationContext();
        this.settings = settings;
        if (this.settings == null) {
            this.settings = new CoreSettings();
        }
        init();
    }

    /**
     * 向服务器获取版本信息
     *
     * @param requests 获取版本信息参数封装entitys
     * @param l        回调接口
     */
    @Override
    public void checkVersion(@NonNull List<RequestVersionEntity> requests
            , @NonNull OnCoreVerCheckListener l) {
        checkVersion(requests, getVersionRequestUrl(), l);
    }

    /**
     * 向服务器获取版本信息
     *
     * @param requests 获取版本信息参数封装entitys
     * @param url      服务器接口地址
     * @param l        回调接口
     */
    @Override
    public void checkVersion(@NonNull List<RequestVersionEntity> requests
            , @NonNull String url, @NonNull OnCoreVerCheckListener l) {
        if (!checkVersionParameter(mContext, requests, url, l)) {
            return;
        }

        if (mUrl.isBatchRequest()) {
            //可以批量处理
            HttpUtils.versionRequest(mContext.getApplicationContext(), requests, url, l);
        } else {
            //不可以批量处理
            for (RequestVersionEntity request : requests) {
                HttpUtils.versionRequest(mContext.getApplicationContext(), request, url, l);
            }
        }
    }

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     */
    @Override
    public void reportUpdate(@NonNull List<RequestReportEntity> requests) {
        reportUpdate(requests, null);
    }

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     * @param l        回调接口
     */
    @Override
    public void reportUpdate(@NonNull List<RequestReportEntity> requests
            , @Nullable OnCoreVerCheckListener l) {
        reportUpdate(requests, getReportUrl(), l);
    }

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     * @param url      上报地址
     * @param l        回调接口
     */
    @Override
    public void reportUpdate(@NonNull List<RequestReportEntity> requests
            , @NonNull String url, @Nullable OnCoreVerCheckListener l) {
        if (!checkReportParameter(mContext, requests, url, l)) {
            return;
        }

        if (mUrl.isBatchRequest()) {
            //可以批量处理
            HttpUtils.report(mContext.getApplicationContext(), requests, url, l);
        } else {
            //不可以批量处理
            for (RequestReportEntity request : requests) {
                HttpUtils.report(mContext.getApplicationContext(), request, url, l);
            }
        }
    }

    /**
     * 设置通用请求头信息
     *
     * @param machineId       机器码
     * @param accountId       账户id
     * @param apkPackageName  app包名
     * @param apkVersionCode  app版本号
     * @param deviceModel     机型名
     * @param deviceOSVersion 系统版本名
     */
    @Override
    public void setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion) {
        HttpUtils.setRequestHead(machineId, accountId, apkPackageName
                , apkVersionCode, deviceModel, deviceOSVersion);
    }

    /**
     * 请求头信息是否已经设置
     *
     * @return
     */
    @Override
    public boolean isRequestHeadEmpty() {
        return HttpUtils.isRequestHeadEmpty();
    }

    /**
     * 获取版本更新核心库版本号
     *
     * @return
     */
    @Override
    public String getLibVersion() {
        return TextUtils.concat(
                SDKVersion.getLibraryName(), ", version: ", SDKVersion.getVersionName(),
                " code: ", String.valueOf(SDKVersion.getSDKInt()),
                " build: ", SDKVersion.getBuildName()
        ).toString();
    }

    /**
     * 取消所有请求
     */
    @Override
    public void cancelAll() {
        HttpUtils.cancelAll();
    }

    private void init() {
        LogUtils.i(TAG, " init " + getLibVersion());
        initLog();
        initBfcHttp(mContext);
        mUrl = settings.getUrl();
        if (mUrl == null) {
            LogUtils.w(TAG, "Url is null, automatically sets to default url.");
            mUrl = new UrlReleaseImpl();
        }
    }

    private void initLog() {
        LogUtils.setCacheLog(settings.isCacheLog());
        LogUtils.setDebugMode(settings.isDebugMode());
    }

    private void initBfcHttp(Context context) {
        //初始化配置
        BfcHttpConfigure.init(context);
        //超时时长配置
        BfcHttpConfigure.setTimeout(settings.getConnectionOut());
        if (settings.isDebugMode()) {
            BfcHttpConfigure.openDebug();
        }
    }

    /**
     * 检查获取版本信息接口参数是否合法
     *
     * @param context  上下文
     * @param requests 获取版本信息参数封装entitys
     * @param url      服务器接口地址
     * @param l        回调接口
     * @return
     */
    private boolean checkVersionParameter(Context context, List<RequestVersionEntity> requests
            , String url, OnCoreVerCheckListener l) {
        if (l == null) {
            onError(l, CoreErrorCode.CORE_CHECK_LISTENER_NULL);
            return false;
        }

        if (context == null) {
            onError(l, CoreErrorCode.CORE_CHECK_CONTEXT_NULL);
            return false;
        }

        if (ListUtils.isEmpty(requests)) {
            onError(l, CoreErrorCode.CORE_CHECK_LIST_NULL);
            return false;
        }

        if (TextUtils.isEmpty(url)) {
            onError(l, CoreErrorCode.CORE_CHECK_URL_NULL);
            return false;
        }

        if (requests.size() > Constants.CHECK_VERSION_LIST_LIMIT) {
            throw new IllegalArgumentException(" List<RequestVersionEntity> requests size must be <= " + Constants.CHECK_VERSION_LIST_LIMIT);
        }

        if (!isNetConnected(context)) {
            onError(l, CoreErrorCode.CORE_CHECK_NET_EXCEPTION);
            return false;
        }

        return true;
    }

    /**
     * 检查上报升级信息接口参数是否合法
     *
     * @param context  上下文
     * @param requests 上报信息封装entitys
     * @param url      上报地址
     * @return
     */
    private boolean checkReportParameter(Context context, List<RequestReportEntity> requests
            , String url, OnCoreVerCheckListener l) {
        if (context == null) {
            onError(l, CoreErrorCode.CORE_REPORT_CONTEXT_NULL);
            return false;
        }

        if (ListUtils.isEmpty(requests)) {
            onError(l, CoreErrorCode.CORE_REPORT_LIST_NULL);
            return false;
        }

        if (TextUtils.isEmpty(url)) {
            onError(l, CoreErrorCode.CORE_REPORT_URL_NULL);
            return false;
        }

        if(!isNetConnected(context)){
            onError(l, CoreErrorCode.CORE_CHECK_NET_EXCEPTION);
            return false;
        }

        return true;
    }

    private boolean isNetConnected(Context context) {
        int type = NetUtils.getNetWorkType(context);

        if (type == NetUtils.NETWORK_NO) {
            LogUtils.d(TAG, "No network, don't check version.");
            return false;
        }

        if (type == NetUtils.NETWORK_UNKNOWN) {
            LogUtils.d(TAG, "network is unknown, don't check version.");
            return false;
        }

        if (!NetworkUtils.containsMobile2G(settings.getRequestNetwork()) && type == NetUtils.NETWORK_2G) {
            LogUtils.d(TAG, "network is 2G, don't check version.");
            return false;
        }

        if (!NetworkUtils.containsMobile(settings.getRequestNetwork()) && NetUtils.isMobileDataConnected(context)) {
            LogUtils.d(TAG, "network is mobile, don't check version.");
            return false;
        }

        if (!NetworkUtils.containsWifi(settings.getRequestNetwork()) && type == NetUtils.NETWORK_WIFI) {
            LogUtils.d(TAG, "network is wifi, don't check version.");
            return false;
        }

        return NetUtils.isConnected(context);
    }

    private String getVersionRequestUrl() {
        if (!mUrl.isBatchRequest()) {
            //没有批量请求的接口  (老接口没有商城请求接口)
            return mUrl.checkVersionAppUrl();
        }
        return settings.isMultipleRequests()
                ? mUrl.checkVersionMarketUrl() : mUrl.checkVersionAppUrl();
    }

    private String getReportUrl() {
        if (!mUrl.isBatchRequest()) {
            //没有批量请求的接口  (老接口没有商城请求接口)
            return mUrl.reportAppUrl();
        }
        return settings.isMultipleRequests()
                ? mUrl.reportMarketUrl() : mUrl.reportAppUrl();
    }

    private void onError(OnCoreVerCheckListener l, String errorCode) {
        onError(l, errorCode, "");
    }

    private void onError(OnCoreVerCheckListener l, String errorCode, String detail) {
        LogUtils.bfcExceptionLog(TAG, errorCode, detail);
        if (l != null) {
            l.onError(new BfcVersionHttpError(errorCode));
        }
    }
}
