package com.eebbk.bfc.sdk.version.download;

import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.VersionConstants;

/**
 * @author hesn
 * @function 下载扩展字段保存信息
 * @date 17-3-13
 * @company 步步高教育电子有限公司
 */

public class DownloadExtraAgent {

    /**
     * 获取应用包名
     * <br> 下载扩展字段保存的信息
     *
     * @param task
     * @return
     */
    public static String getPackageName(ITask task) {
        return getStringExtra(task, DownloadConstants.TASK_TAG_PACKAGE);
    }

    public static String getModuleName(ITask task) {
        return getStringExtra(task, DownloadConstants.TASK_TAG_MODULE);
    }

    public static String getApkIntroduce(ITask task) {
        return getStringExtra(task, DownloadConstants.TASK_TAG_APK_INTRODUCE);
    }

    public static String getTip(ITask task) {
        return getStringExtra(task, DownloadConstants.TASK_TAG_TIP);
    }

    public static long getFileSize(ITask task) {
        return Long.valueOf(getStringExtra(task, DownloadConstants.TASK_TAG_FILE_SIZE)).longValue();
    }

    public static long getPatchSize(ITask task) {
        return Long.valueOf(getStringExtra(task, DownloadConstants.TASK_TAG_PATCH_SIZE)).longValue();
    }

    /**
     * 升级模式
     * <br> 下载扩展字段保存的信息
     *
     * @param task
     * @return
     */
    public static int getUpdateMode(ITask task) {
        return getIntExtra(task, DownloadConstants.TASK_TAG_UPDATE_MODE, VersionConstants.UPDATE_MODE_DEFAULT);
    }

    /**
     * 下载扩展字段保存的信息
     *
     * @param task
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntExtra(ITask task, String key, int defaultValue) {
        if (task == null) {
            return defaultValue;
        }
        return task.getIntExtra(key, defaultValue);
    }

    /**
     * 下载扩展字段保存的信息
     *
     * @param task
     * @param key
     * @return
     */
    public static String getStringExtra(ITask task, String key) {
        if (task == null) {
            return null;
        }
        return task.getStringExtra(key);
    }
}
