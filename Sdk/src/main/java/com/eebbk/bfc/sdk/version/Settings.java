package com.eebbk.bfc.sdk.version;

import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.CoreSettings;
import com.eebbk.bfc.sdk.download.net.NetworkType;
import com.eebbk.bfc.sdk.version.util.Utils;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public class Settings extends CoreSettings {

    /**
     * 检查有更新后自动下载
     */
    private boolean autoDownload = true;

    /**
     * 是否总是全量升级
     */
    private boolean alwaysFullUpgrade = false;

    /**
     * 自动重新下载 下载失败的任务
     */
    private boolean autoReloadDownloadFailedTask = true;

    /**
     * 自动重新下载 校验失败的任务
     */
    private boolean autoReloadCheckFailedTask = true;

    /**
     * 版本更新检查策略
     */
    private int checkStrategy = VersionConstants.CHECK_STRATEGY_LOCAL_FIRST;

    /**
     * 下载网络类型
     */
    private int downloadNetwork = NetworkType.NETWORK_WIFI | NetworkType.NETWORK_MOBILE;

    /**
     * 检查有更新后自动下载
     *
     * @return
     */
    public boolean isAutoDownload() {
        return autoDownload;
    }

    /**
     * 检查有更新后自动下载
     *
     * @param enableAutoDownload
     */
    public void setAutoDownload(boolean enableAutoDownload) {
        this.autoDownload = enableAutoDownload;
    }

    /**
     * 是否总是全量升级
     *
     * @return
     */
    public boolean isAlwaysFullUpgrade() {
        return alwaysFullUpgrade;
    }

    /**
     * 是否总是全量升级
     *
     * @param alwaysFullUpgrade
     */
    public void setAlwaysFullUpgrade(boolean alwaysFullUpgrade) {
        this.alwaysFullUpgrade = alwaysFullUpgrade;
    }

    /**
     * 自动重新下载 下载失败的任务
     *
     * @return
     */
    public boolean isAutoReloadDownloadFailedTask() {
        return autoReloadDownloadFailedTask;
    }

    /**
     * 自动重新下载 下载失败的任务
     *
     * @param autoReloadDownloadFailedTask
     */
    public void setAutoReloadDownloadFailedTask(boolean autoReloadDownloadFailedTask) {
        this.autoReloadDownloadFailedTask = autoReloadDownloadFailedTask;
    }

    /**
     * 自动重新下载 校验失败的任务
     *
     * @return
     */
    public boolean isAutoReloadCheckFailedTask() {
        return autoReloadCheckFailedTask;
    }

    /**
     * 自动重新下载 校验失败的任务
     *
     * @param autoReloadCheckFailedTask
     */
    public void setAutoReloadCheckFailedTask(boolean autoReloadCheckFailedTask) {
        this.autoReloadCheckFailedTask = autoReloadCheckFailedTask;
    }

    /**
     * 版本更新检查策略
     *
     * @return
     */
    public int getCheckStrategy() {
        return checkStrategy;
    }

    /**
     * 版本更新检查策略
     *
     * @param checkStrategy
     */
    public void setCheckStrategy(int checkStrategy) {
        this.checkStrategy = checkStrategy;
    }

    /**
     * 允许下载的网络类型
     *
     * @return
     */
    public int getDownloadNetwork() {
        return downloadNetwork;
    }

    /**
     * 允许下载的网络类型
     *
     * <pre>
     * {@link Constants#NETWORK_WIFI }
     * {@link Constants#NETWORK_MOBILE }
     * {@link Constants#NETWORK_MOBILE_2G }
     * </pre>
     * 可以叠加使用，比如: wifi+mobile的值 = {@link Constants#NETWORK_WIFI } | {@link Constants#NETWORK_MOBILE }
     *
     * @param network 网络类型
     * <br> 默认wifi和移动网络(包括2G)都可以访问
     */
    public void setDownloadNetwork(int network) {
        this.downloadNetwork = Utils.versionNetType2DownloadNetType(network);
    }
}
