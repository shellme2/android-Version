package com.eebbk.bfc.sdk.version.listener;

import android.content.Context;

import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;

import java.util.List;

/**
 * 版本检测 监听
 */
public interface OnVersionCheckListener {

    /**
     * 可以升级
     *
     * @param context
     * @param info      安装apk版本信息
     */
    void onUpdateReady(Context context, VersionInfo info);

    /**
     * 有新版本被检测到时的回调
     *
     * @param newVersions 新版本信息
     */
    void onNewVersionChecked(List<Version> newVersions);

    /**
     * 版本检测发生异常的回调
     *
     * @param errorCode 错误码
     */
    void onVersionCheckException(String errorCode);

    /**
     * 检查结束
     */
    void onCheckOver();
}
