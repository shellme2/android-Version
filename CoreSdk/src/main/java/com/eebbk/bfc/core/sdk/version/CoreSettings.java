package com.eebbk.bfc.core.sdk.version;

import com.eebbk.bfc.core.sdk.version.url.IUrl;
import com.eebbk.bfc.core.sdk.version.url.UrlReleaseImpl;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public class CoreSettings {

    private boolean isDebugMode = false;
    private boolean isCacheLog = false;
    private boolean isMultipleRequests = false;
    private int connectionOut = 10000;
    private IUrl mUrl = new UrlReleaseImpl();
    private int requestNetwork = Constants.NETWORK_DEFAULT;

    /**
     * 是否为调试模式
     *
     * @return
     */
    public boolean isDebugMode() {
        return isDebugMode;
    }

    /**
     * 设置是否为调试模式
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {
        isDebugMode = debugMode;
    }

    /**
     * 多请求
     *
     * @return
     */
    public boolean isMultipleRequests() {
        return isMultipleRequests;
    }

    /**
     * 设置是否为多请求
     *
     * @param multipleRequests
     */
    public void setMultipleRequests(boolean multipleRequests) {
        isMultipleRequests = multipleRequests;
    }

    /**
     * 连接超时
     *
     * @return
     */
    public int getConnectionOut() {
        return connectionOut;
    }

    /**
     * 连接超时
     *
     * @param connectionOut
     */
    public void setConnectionOut(int connectionOut) {
        this.connectionOut = connectionOut;
    }

    public boolean isCacheLog() {
        return isCacheLog;
    }

    public void setCacheLog(boolean cacheLog) {
        isCacheLog = cacheLog;
    }

    public IUrl getUrl() {
        return mUrl;
    }

    public void setUrl(IUrl url) {
        this.mUrl = url;
    }

    /**
     * 允许请求的网络类型
     *
     * @return
     */
    public int getRequestNetwork() {
        return requestNetwork;
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
     * @param network 网络类型
     * <br> 默认wifi和移动网络(不包括2G)都可以访问
     */
    public void setRequestNetwork(int network) {
        this.requestNetwork = network;
    }

}
