package com.eebbk.bfc.sdk.version;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.url.IUrl;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.download.DownloadInfo;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionDownloadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public interface BfcVersion {

    /**
     * 检查版本 开启异步任务
     * <br> 检查当前app
     */
    void checkVersion();

    /**
     * 检查版本 开启异步任务
     *
     * @param entities
     */
    void checkVersion(@NonNull final List<CheckParams> entities);

    /**
     * 上报升级信息
     *
     * @param version apk版本信息
     * @param state   升级状态
     * @param info    升级信息
     */
    void reportUpdate(Version version, int state, String info);

    /**
     * 上报升级信息
     *
     * @param requests 上报信息封装entitys
     */
    void reportUpdate(@NonNull List<RequestReportEntity> requests);

    /**
     * 销毁
     */
    void destroy();

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
    void setRequestHead(@NonNull String machineId, @NonNull String accountId, @NonNull String apkPackageName
            , @NonNull String apkVersionCode, @NonNull String deviceModel, @NonNull String deviceOSVersion);

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param remoteVersion 新版本信息
     */
    void beginDownload(@NonNull Version remoteVersion);

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param remoteVersions 新版本信息
     */
    void beginDownload(@NonNull List<Version> remoteVersions);

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param iTask 下载任务信息
     */
    void beginDownload(@NonNull ITask... iTask);

    /**
     * 获取当前应用版本信息
     *
     * @return 当前应用版本信息
     */
    Version getLocalVersion();

    /**
     * 差分合并失败，重新下载apk，走全量升级流程
     *
     * @param info    下载信息
     * @param context 上下文
     */
    void downloadFullNewApk(@NonNull DownloadInfo info, @NonNull Context context);

    /**
     * 差分包合并
     *
     * @param context
     * @param info
     */
    void patchApk(@NonNull Context context, @NonNull DownloadInfo info);

    /**
     * 下载监听
     *
     * @param l
     */
    void setOnVersionDownloadListener(OnVersionDownloadListener l);

    /**
     * 下载监听
     */
    OnVersionDownloadListener getOnVersionDownloadListener();

    /**
     * 设置版本检查回调 如果有需要即时了解新版本检查的需求 需要设置该监听器
     *
     * @param ovcl 版本检测回调listener
     */
    void setOnVersionCheckListener(OnVersionCheckListener ovcl);

    /**
     * 版本检查回调 如果有需要即时了解新版本检查的需求 需要设置该监听器
     */
    OnVersionCheckListener getOnVersionCheckListener();

    /**
     * 获取配置信息
     *
     * @return
     */
    Settings getSettings();

    /**
     * 删除所有下载任务
     */
    void deleteAllDownloadTask();

    /**
     * 根据包名删除下载任务
     *
     * @param packageNames 包名
     */
    void deleteDownloadTask(String... packageNames);

    /**
     * 安装pak
     *
     * @param context
     * @param file
     */
    void installApk(@NonNull Context context, @NonNull File file);

    /**
     * 安装pak
     *
     * @param context
     * @param info
     */
    void installApk(@NonNull Context context, @NonNull VersionInfo info);

    /**
     * 获取库的版本信息
     *
     * @return
     */
    String getLibVersion();

    /**
     * 获取所有下载任务
     *
     * @return
     */
    ArrayList<ITask> getAllDownloadTask();

    /**
     * 获取版本更新的下载任务
     * <br> 根据检查更新返回的Version获取下载任务的Builder,可以根据此Builder添加下载相关设置
     * <br> 只有在该下载前添加的设置才能生效
     *
     * @param version
     * @return
     */
    ITask.Builder getDownloadTaskBuilder(@NonNull Version version);

    /**
     * 根据错误码获取错误信息
     *
     * @param errorCode
     * @return
     */
    String getErrorMsg(@NonNull String errorCode);

    /**
     * 忽略版本
     * <br> 忽略比这版本(包括此版本)低的所有版本
     *
     * @param info OnVersionCheckListener接口中onUpdateReady()函数返回的可安装信息
     */
    void ignoreVersion(@NonNull VersionInfo info);

    /**
     * 忽略版本
     * <br> 忽略比这版本(包括此版本)低的所有版本
     *
     * @param packageName 忽略版本的应用包名
     * @param versionCode 忽略版本的应用版本号
     */
    void ignoreVersion(@NonNull String packageName, int versionCode);

    /**
     * 获取已设置的忽略版本号
     *
     * @param packageName 忽略版本的应用包名
     * @return 返回无视的版本号;返回-1则无设置
     */
    int getIgnoreVersion(@NonNull String packageName);

    /**
     * 获取允许请求更新信息的网络类型
     *
     * @return
     */
    int getRequestNetworkType();

    /**
     * 获取允许下载的网络类型
     *
     * @return
     */
    int getDownloadNetworkType();

    /**
     * 是否需要静默安装
     *
     * @param info
     * @return
     */
    boolean isSilentInstall(@NonNull VersionInfo info);

    /**
     * 是否需要静默安装
     *
     * @param version
     * @return
     */
    boolean isSilentInstall(@NonNull Version version);

    /**
     * 是否需要静默安装
     *
     * @param updateMode 升级方式类型
     * @return
     */
    boolean isSilentInstall(int updateMode);

    class Builder {
        final Settings settings;

        public Builder() {
            settings = new Settings();
        }

        /**
         * 检查如果更新 自动下载
         *
         * @param enable
         * @return
         */
        public Builder autoDownload(boolean enable) {
            settings.setAutoDownload(enable);
            return this;
        }

        /**
         * 优先全量升级
         *
         * @param enable
         * @return
         */
        public Builder alwaysFullUpgrade(boolean enable) {
            settings.setAlwaysFullUpgrade(enable);
            return this;
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
            settings.setUrl(url);
            return this;
        }

        /**
         * 自动从新下载 下载失败的任务
         *
         * @param enable
         * @return
         */
        public Builder autoReloadDownloadFailedTask(boolean enable) {
            settings.setAutoReloadDownloadFailedTask(enable);
            return this;
        }

        /**
         * 自动从新下载 文件校验失败的任务
         *
         * @param enable
         * @return
         */
        public Builder autoReloadCheckFailedTask(boolean enable) {
            settings.setAutoReloadCheckFailedTask(enable);
            return this;
        }

        /**
         * 允许请求更新信息的网络类型
         * <p>
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
        public Builder setRequestNetwork(int network) {
            settings.setRequestNetwork(network);
            return this;
        }

        /**
         * 允许下载的网络类型
         * <p>
         * <pre>
         * {@link Constants#NETWORK_WIFI }
         * {@link Constants#NETWORK_MOBILE }
         * </pre>
         * 可以叠加使用，比如: wifi+mobile的值 = {@link Constants#NETWORK_WIFI } | {@link Constants#NETWORK_MOBILE }
         *
         * @param network 网络类型
         */
        public Builder setDownloadNetwork(int network) {
            settings.setDownloadNetwork(network);
            return this;
        }

        /**
         * 版本更新检查策略
         * <p>
         * <pre>
         * {@link VersionConstants#CHECK_STRATEGY_LOCAL_FIRST }
         * {@link VersionConstants#CHECK_STRATEGY_REMOTE_FIRST }
         * </pre>
         *
         * @param checkStrategy 检查策略类型
         */
        public Builder setCheckStrategy(int checkStrategy) {
            settings.setCheckStrategy(checkStrategy);
            return this;
        }

        public BfcVersion build(@NonNull Context context) {
            return new BfcVersionImpl(context, settings);
        }
    }

}
