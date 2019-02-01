package com.eebbk.bfc.sdk.version.demo.servicelib.config;

import com.eebbk.bfc.core.sdk.version.url.IUrl;
import com.eebbk.bfc.core.sdk.version.url.UrlTestImpl;
import com.eebbk.bfc.sdk.version.VersionConstants;

/**
 * @author hesn
 * @function
 * @date 16-12-6
 * @company 步步高教育电子有限公司
 */

public class ServoceConfig {

    /**
     * 检查有更新后自动下载
     */
    private static boolean autoDownload = true;

    /**
     * 是否总是全量升级
     */
    private static boolean alwayFullUpgrade = false;

    /**
     * 自动重新下载 下载失败的任务
     */
    private static boolean autoReloadDownloadFailedTask = true;

    /**
     * 版本更新检查策略
     */
    private static int mCheckStrategy = VersionConstants.CHECK_STRATEGY_LOCAL_FIRST;

    /**
     * 自动重新下载 校验失败的任务
     */
    private static boolean autoReloadCheckFailedTask = true;

    private static boolean isDebugMode = true;

    private static IUrl mUrl = new UrlTestImpl();

    public static boolean isAutoDownload() {
        return autoDownload;
    }

    public static void setAutoDownload(boolean enable) {
        autoDownload = enable;
    }

    public static boolean isAlwayFullUpgrade() {
        return alwayFullUpgrade;
    }

    public static void setAlwayFullUpgrade(boolean enable) {
        alwayFullUpgrade = enable;
    }

    public static boolean isAutoReloadDownloadFailedTask() {
        return autoReloadDownloadFailedTask;
    }

    public static void setAutoReloadDownloadFailedTask(boolean enable) {
        autoReloadDownloadFailedTask = enable;
    }

    public static boolean isAutoReloadCheckFailedTask() {
        return autoReloadCheckFailedTask;
    }

    public static void setAutoReloadCheckFailedTask(boolean enable) {
        autoReloadCheckFailedTask = enable;
    }

    public static boolean isDebugMode() {
        return isDebugMode;
    }

    public static void setDebugMode(boolean enable) {
        isDebugMode = enable;
    }

    public static IUrl getUrl() {
        return mUrl;
    }

    public static void setUrl(IUrl url) {
        mUrl = url;
    }

    public static int getCheckStrategy() {
        return mCheckStrategy;
    }

    public static void setCheckStrategy(int checkStrategy) {
        mCheckStrategy = checkStrategy;
    }
}
