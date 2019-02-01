package com.eebbk.bfc.sdk.version;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.sdk.version.download.DownloadInfo;
import com.eebbk.bfc.sdk.version.entity.Version;

/**
 * 版本管理器，向服务器查询版本信息，处理下载
 *
 * @deprecated 建议改用 BfcVersion bfcVersion = new BfcVersion.Builder().build(context);
 */
@Deprecated
public class VersionManager {

    private final BfcVersion bfcVersion;

    /**
     * 构造函数
     *
     * @param context 上下文
     *
     * @deprecated 建议改用 BfcVersion
     */
    @Deprecated
    public VersionManager(@NonNull Context context) {
        bfcVersion = new BfcVersion.Builder().build(context);
    }

    /**
     * 差分合并失败，重新下载apk，走全量升级流程
     *
     * @param info    下载信息
     * @param context 上下文
     *
     * @deprecated 建议改用 BfcVersion 的 downloadFullNewApk(DownloadInfo info, Context context);
     */
    @Deprecated
    public void downloadFullNewApk(DownloadInfo info, Context context) {
        bfcVersion.downloadFullNewApk(info, context);
    }

    /**
     * 开始下载，已废弃（拼写错误）,请使用beginDownload代替
     *
     * @param remoteVersion 服务器版本信息
     *
     * @deprecated 建议改用 BfcVersion 的 beginDownload(Version remoteVersion);
     */
    @Deprecated
    public void beginDonwload(Version remoteVersion) {
        beginDownload(remoteVersion);
    }

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param remoteVersion 新版本信息
     *
     * @deprecated 建议改用 BfcVersion 的 beginDownload(Version remoteVersion);
     */
    @Deprecated
    public void beginDownload(Version remoteVersion) {
        bfcVersion.beginDownload(remoteVersion);
    }

    /**
     * 获取当前应用版本信息
     *
     * @return 当前应用版本信息
     *
     * @deprecated 建议改用 BfcVersion 的 getLocalVersion();
     */
    @Deprecated
    public Version getLocalVersion() {
        return bfcVersion.getLocalVersion();
    }

    /**
     * 检查版本 开启异步任务
     * (方法名容易误解)
     *
     * @deprecated 建议改用 BfcVersion 的 checkVersion();
     */
    @Deprecated
    public void onVersionCheck() {
        checkVersion();
    }

    /**
     * 检查版本 开启异步任务
     * <br> 检查当前app
     *
     * @deprecated 建议改用 BfcVersion 的 checkVersion();
     */
    @Deprecated
    public void checkVersion() {
        bfcVersion.checkVersion();
    }
}
