package com.eebbk.bfc.core.sdk.version;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.core.sdk.version.url.IUrl;

import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public interface BfcCoreVersion {

    /**
     * 向服务器获取版本信息
     *
     * @param requests 获取版本信息参数封装entitys
     * @param l        回调接口
     */
    void checkVersion(@NonNull List<RequestVersionEntity> requests
            , @NonNull OnCoreVerCheckListener l);

    /**
     * 向服务器获取版本信息
     *
     * @param requests 获取版本信息参数封装entitys
     * @param url      服务器接口地址
     * @param l        回调接口
     */
    void checkVersion(@NonNull List<RequestVersionEntity> requests
            , @NonNull String url, @NonNull OnCoreVerCheckListener l);

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     */
    void reportUpdate(@NonNull List<RequestReportEntity> requests);

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     * @param l        回调接口
     */
    void reportUpdate(@NonNull List<RequestReportEntity> requests
            , @Nullable OnCoreVerCheckListener l);

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     * @param url      上报地址
     * @param l        回调接口
     */
    void reportUpdate(@NonNull List<RequestReportEntity> requests
            , @NonNull String url, @Nullable OnCoreVerCheckListener l);

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
    void setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion);

    /**
     * 请求头信息是否已经设置
     *
     * @return
     */
    boolean isRequestHeadEmpty();

    /**
     * 获取版本更新核心库版本号
     *
     * @return
     */
    String getLibVersion();

    /**
     * 取消所有请求
     */
    void cancelAll();

    class Builder {
        CoreSettings settings;

        public Builder() {
            settings = new CoreSettings();
        }

        /**
         * 调试模式
         *
         * @param debugMode
         * @return
         */
        public Builder setDebugMode(boolean debugMode) {
            settings.setDebugMode(debugMode);
            return this;
        }

        /**
         * 设置是否为多请求
         *
         * @param multipleRequests
         */
        public Builder setMultipleRequests(boolean multipleRequests) {
            settings.setMultipleRequests(multipleRequests);
            return this;
        }

        /**
         * 允许请求的网络类型
         *
         * <pre>
         * {@link Constants#NETWORK_WIFI }
         * {@link Constants#NETWORK_MOBILE }
         * {@link Constants#NETWORK_MOBILE_2G }
         * </pre>
         * 可以叠加使用，比如: wifi+mobile的值 = {@link Constants#NETWORK_WIFI } | {@link Constants#NETWORK_MOBILE }
         *
         * @param requestNetwork 网络类型
         * <br> 默认wifi和移动网络(不包括2G)都可以访问
         */
        public Builder setRequestNetwork(int requestNetwork) {
            settings.setRequestNetwork(requestNetwork);
            return this;
        }

        /**
         * 连接超时
         *
         * @param connectionOut
         */
        public Builder setConnectionOut(int connectionOut) {
            settings.setConnectionOut(connectionOut);
            return this;
        }

        /**
         * 缓存日志
         *
         * @param cacheLog
         * @return
         */
        public Builder setCacheLog(boolean cacheLog) {
            settings.setCacheLog(cacheLog);
            return this;
        }

        /**
         * 设置访问服务器地址
         *
         * @param url
         * @return
         */
        public Builder setUrl(@NonNull IUrl url) {
            if (url == null) {
                return this;
            }
            settings.setUrl(url);
            return this;
        }

        public Builder setSettings(@NonNull CoreSettings settings) {
            if (settings == null) {
                return this;
            }
            this.settings = settings;
            return this;
        }

        public BfcCoreVersion build(Context context) {
            return new BfcCoreVersionImpl(context, settings);
        }
    }
}
