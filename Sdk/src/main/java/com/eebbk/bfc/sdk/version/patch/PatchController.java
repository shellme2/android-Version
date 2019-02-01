package com.eebbk.bfc.sdk.version.patch;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.DownloadController;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.download.DownloadInfo;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.error.ErrorCode;
import com.eebbk.bfc.sdk.version.listener.OnPatchListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Destroyable;

/**
 * @author hesn
 * @function 差分包合并管理类
 * @date 16-12-1
 * @company 步步高教育电子有限公司
 */

public class PatchController implements OnPatchListener, Destroyable {

    private Context mContext;
    private BfcVersion mBfcVersion;
    private OnVersionCheckListener mOnVersionCheckListener;
    private static final String TAG = "PatchController";
    private final Map<String, DownloadInfo> mMergingMap = new HashMap<String, DownloadInfo>();

    public PatchController(BfcVersion bfcVersion) {
        mBfcVersion = bfcVersion;
    }

    /**
     * 开始差分包合并
     *
     * @param context
     * @param info
     */
    public void startPatch(Context context, DownloadInfo info) {
        if (context == null) {
            throw new NullPointerException("BfcVersion PatchController context non null!");
        }
        if (info == null) {
            if (mOnVersionCheckListener != null) {
                mOnVersionCheckListener.onVersionCheckException(ErrorCode.SERVICE_CHECK_DOWNLOAD_INFO_NULL);
            }
            return;
        }
        putMergingMap(info);
        mContext = context.getApplicationContext();
        PatchParam param = new PatchParam(mContext, info);
        PatchApkTask patchApkTask = new PatchApkTask(mContext, param);
        patchApkTask.setOnPatchListener(this);
        patchApkTask.execute();
    }

    /**
     * 获取正在进行差分包合并的任务
     *
     * @return
     */
    public Map<String, DownloadInfo> getMergingTask() {
        return new HashMap<String, DownloadInfo>(mMergingMap);
    }

    @Override

    public void patchFailed(PatchParam patchParam, int result) {
        removeMergingMap(patchParam.getDownloadInfo());
        if (mBfcVersion == null) {
            LogUtils.w(TAG, "差分包合并失败,PatchController已注销,无法下载全量包.");
            return;
        }

        DownloadInfo info = patchParam.getDownloadInfo();
        ITask task = DownloadController.getInstance().getTaskById(info.getDownloadId());
        if (task == null) {
            //原任务已经被用户删除,则无需要再继续下载全量包
            LogUtils.w(TAG, "原任务已经被删除,无需要再继续下载全量包.");
            return;
        }

        //提示上层差分包合并失败,尝试下载全量
        if (mOnVersionCheckListener != null) {
            mOnVersionCheckListener.onVersionCheckException(ErrorCode.SERVICE_PATCH_FAILED);
        }

        LogUtils.w(TAG, "差分包合并失败,尝试下载全量包.");
        //原任务还在,则需要删除原文件,从新下载全量包
        DownloadController.getInstance().deleteTaskAndAllFile(task);
        try {
            info.setPatchAvailable(VersionConstants.PATCH_UNAVAILABLE);
            mBfcVersion.downloadFullNewApk(info, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportUpdate(info, Constants.REPORT_UPDATE_FAILED, "差分包下载成功，但合并失败！ Patch Result Code：" + result);
    }

    @Override
    public void patchSuccess(PatchParam patchParam) {
        removeMergingMap(patchParam.getDownloadInfo());
        DownloadInfo info = patchParam.getDownloadInfo();
        ITask task = DownloadController.getInstance().getTaskById(info.getDownloadId());
        if (task == null) {
            //原任务已经被用户删除,无需要提示用户安装
            LogUtils.w(TAG, "原任务已经被删除,无需要提示用户安装.");
            return;
        }

        // 差分包合并的md5和全量直接下的md5不一样
        VersionInfo versionInfo = new VersionInfo(
                patchParam.getContext(), info.getDownloadId(),
                info.getVersionName(), info.getVersionCode(),
                info.getApkIntroduce(), new File(patchParam.getNewApkPath()),
                info.getCurrentVersionCode(), info.getCurrentVersionName(),
                patchParam.getDownloadInfo().getPatchMd5(), info.getPackageName(),
                info.getUpdateMode(), info.getUpdateinformation()
        );

        if (mOnVersionCheckListener != null) {
            mOnVersionCheckListener.onUpdateReady(patchParam.getContext(), versionInfo);
        }

//        DownloadController.getInstance().deleteTaskAndAllFile(info.getDownloadId());
        reportUpdate(info, Constants.REPORT_UPDATE_SUCCESS, "差分包下载成功，且合并成功！");
    }

    public void setOnVersionCheckListener(OnVersionCheckListener l) {
        mOnVersionCheckListener = l;
    }

    @Override
    public void destroy() {
        mOnVersionCheckListener = null;
        mBfcVersion = null;
        mContext = null;
    }

    @Override
    public boolean isDestroyed() {
        return mContext == null;
    }

    private void reportUpdate(Version version, int state, String info) {
        if (mBfcVersion == null) {
            LogUtils.w(TAG, "PatchController已注销,无法上报更新状态.");
            return;
        }

        LogUtils.d(VersionConstants.TAG, "state =" + state + "info =" + info);
        mBfcVersion.reportUpdate(version, state, info);
    }

    /**
     * 添加到统计正在差分包合并的map中(正在差分包合并)
     *
     * @param info
     */
    private void putMergingMap(DownloadInfo info) {
        if (info == null) {
            return;
        }
        if (!mMergingMap.containsKey(info.getPackageName())) {
            mMergingMap.put(info.getPackageName(), info);
        }
    }

    /**
     * 移除统计正在差分包合并的map中(差分包已经合并完)
     *
     * @param info
     */
    private void removeMergingMap(DownloadInfo info) {
        if (info == null) {
            return;
        }
        mMergingMap.remove(info.getPackageName());
    }
}
