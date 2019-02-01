package com.eebbk.bfc.sdk.version.listener;

import com.eebbk.bfc.sdk.downloadmanager.ITask;

/**
 * @author hesn
 * @function 版本更新下载监听
 * @date 16-12-1
 * @company 步步高教育电子有限公司
 */

public interface OnVersionDownloadListener {

    /**
     * 等待中
     *
     * @param task 当前任务
     */
    void onDownloadWaiting(final ITask task);

    /**
     * 已开始执行下载任务
     *
     * @param task 当前任务
     */
    void onDownloadStarted(final ITask task);

    /**
     * 已连接下载服务器
     *
     * @param task  当前任务
     * @param resuming 是否恢复是否恢复
     * @param finishedSize 已下载文件大小，单位Bytes
     * @param totalSize 文件总大小，单位Bytes
     */
    void onDownloadConnected(final ITask task, final boolean resuming, final long finishedSize, final long totalSize);

    /**
     * 下载中
     *
     * @param task   当前任务
     * @param finishedSize 已下载文件大小，单位Bytes
     * @param totalSize 文件总大小，单位Bytes
     */
    void onDownloading(final ITask task, final long finishedSize, final long totalSize);

    /**
     * 已暂停
     *
     * @param task    当前任务
     * @param errorCode 错误码，标识当前暂停原因
     */
    void onDownloadPause(final ITask task, final String errorCode);

    /**
     * 正在重试
     *
     * @param task       当前任务
     * @param retries 重试次数
     * @param errorCode  错误码，标识错误代号
     * @param throwable 异常
     */
    void onDownloadRetry(final ITask task, final int retries, final String errorCode, final Throwable throwable);

    /**
     * 下载失败
     *
     * @param task         当前任务
     * @param errorCode 错误码，标识错误代号
     * @param throwable 异常
     */
    void onDownloadFailure(final ITask task, final  String errorCode, final Throwable throwable);

    /**
     * 下载成功
     *
     * @param task 当前任务
     */
    void onDownloadSuccess(final ITask task);
}
