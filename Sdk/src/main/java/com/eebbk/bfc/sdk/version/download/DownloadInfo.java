package com.eebbk.bfc.sdk.version.download;

import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.VersionConstants;

/**
 * 下载信息
 * Created by lcg on 16-4-14.
 */
public class DownloadInfo extends Version {
    /**
     * 下载id，记录在BFCDownload
     */
    private int downloadId;

    public DownloadInfo(ITask task){
        super(task);
        this.setDownloadId(task.getId());
    }

    public boolean checkSetIntent(ITask task) {
        if (task == null
                || DownloadExtraAgent.getModuleName(task) == null
                || !TextUtils.equals(task.getStringExtra(DownloadConstants.TASK_TAG_FUNCTION), VersionConstants.TAG)) {
            // 不是同一种类型的任务!!!
            LogUtils.w("It's not VersionManager Task!");
            return false;
        }
        return true;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    @Override
    public String toString() {
        return super.toString() + "downloadId =" + downloadId;
    }
}
