/*
 * 文 件 名:  PatchApkTask.java
 * 版    权:  广东小天才科技有限公司
 * 描    述:  <文件主要内容描述>
 * 作    者:  lcg
 * 创建时间:  2016-3-3 上午8:37:28
 */
package com.eebbk.bfc.sdk.version.patch;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.listener.OnPatchListener;
import com.eebbk.bfc.sdk.version.patch.algorithm.IPatch;
import com.eebbk.bfc.sdk.version.patch.algorithm.PatchImplFactory;
import com.eebbk.bfc.sdk.version.util.ApkUtils;
import com.eebbk.bfc.sdk.version.util.Md5Utils;
import com.eebbk.bfc.sdk.version.util.Utils;

import java.io.File;

/**
 * 源APK与差分包合成新APK Task
 *
 * @author lcg
 * @version [1.0.0.0, 2016-3-3]
 */
class PatchApkTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "PatchApkTask";

    // 成功
    private static final int WHAT_SUCCESS = 1;

    // 本地安装的应用MD5不正确
    //这部分校验不需要了，把本地apk MD5传给服务器，让服务器去比对。避免下载了不能使用
    @Deprecated
    private static final int WHAT_FAIL_OLD_MD5 = -1;

    // 新生成的应用MD5不正确
    private static final int WHAT_FAIL_GEN_MD5 = -2;

    // 合成失败
    private static final int WHAT_FAIL_PATCH = -3;

    // 获取源文件失败
    private static final int WHAT_FAIL_GET_SOURCE = -4;

    // 参数错误
    private static final int WHAT_FAIL_PARAMS = -6;

    // 无此合并算法
    private static final int WHAT_FAIL_PATCH_TYPE = -7;

    private Context mContext;
    private PatchParam patchParam;
    private OnPatchListener onPatchListener;

    private PatchApkTask() {

    }

    public PatchApkTask(Context mContext, PatchParam patchParam) {
        this.mContext = mContext;
        this.patchParam = patchParam;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (patchParam != null && patchParam.checkParamsAvailable()) {
            LogUtils.d("patchParam checked available");

            String oldApkPath;
            String currentApkFilePath = patchParam.getDownloadInfo().getCurrentApkFilePath();
            String currentApkFileName = patchParam.getDownloadInfo().getCurrentApkFileName();

            LogUtils.d(TAG, "currentApkFilePath：" + currentApkFilePath + " currentApkFileName: " + currentApkFileName);
            LogUtils.d(TAG, "mergeType：" + patchParam.getDownloadInfo().getMergeType());
            if (!TextUtils.isEmpty(currentApkFilePath) && !TextUtils.isEmpty(currentApkFileName)) {
                oldApkPath = currentApkFilePath + currentApkFileName;
            } else {
                oldApkPath = ApkUtils.getSourceApkPath(mContext, patchParam.getPkg());
            }

            String newApkPath = patchParam.getNewApkPath();
            String patchPath = patchParam.getPatchPath();

            LogUtils.d(TAG, "oldApkPath：" + oldApkPath + " newApkPath: " + newApkPath + " patchPath=" + patchPath);

            if (TextUtils.isEmpty(oldApkPath)) {
                return WHAT_FAIL_GET_SOURCE;
            }

            IPatch mPatch = PatchImplFactory.create(patchParam.getDownloadInfo().getMergeType());

            if (mPatch == null) {
                return WHAT_FAIL_PATCH_TYPE;
            }

            int patchResult = mPatch.patch(oldApkPath, patchPath, newApkPath);
            LogUtils.d(TAG, "patchResult:" + patchResult);

            if (patchResult != 0) {
                return WHAT_FAIL_PATCH;
            }
            if (Md5Utils.checkMd5(patchParam.getNewApkPath(), patchParam.getRemoteVersionMD5())) {
                //合并成功的文件拷贝到.SmartUpdate/apk/文件夹下
                String move2Path = VersionConstants.PATH_B + VersionConstants.DOWNLOAD_APK_FOLDER
                        + File.separator + patchParam.getDownloadInfo().getApkFileName();
                Utils.copyOrMoveFile(new File(patchParam.getNewApkPath()), new File(move2Path), true);
                patchParam.setNewApkPath(move2Path);
                return WHAT_SUCCESS;
            } else {
                return WHAT_FAIL_GEN_MD5;
            }
        } else {
            return WHAT_FAIL_PARAMS;
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //需不需要显示patch进度？
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        String toastText = null;

        LogUtils.d(TAG, "result:" + result);

        switch (result) {
            case WHAT_SUCCESS: {
                toastText = "新apk已合成成功：" + patchParam.getNewApkPath();
            }
            break;
            case WHAT_FAIL_OLD_MD5: {
                toastText = "现在安装的的MD5与远程服务器不对！";
            }
            break;
            case WHAT_FAIL_GEN_MD5: {
                toastText = "合成完毕，但是合成得到的apk MD5不对！";
            }
            break;
            case WHAT_FAIL_PATCH: {
                toastText = "新apk已合成失败！";
            }
            break;
            case WHAT_FAIL_GET_SOURCE: {
                toastText = "无法获取源apk文件，只能整包更新了！";
            }
            break;
            default: {

            }
            break;
        }

        LogUtils.d(toastText);

        if (result == WHAT_SUCCESS) {
            notifyPatchSuccess();
        } else {
            notifyPatchFailed(result);
        }

    }

    public void setOnPatchListener(OnPatchListener pl) {
        this.onPatchListener = pl;
    }

    /**
     * 通知合并成功
     */
    private void notifyPatchSuccess() {

        if (onPatchListener != null) {
            onPatchListener.patchSuccess(patchParam);
        } else {
            LogUtils.w("onPatchListener is Null!");
        }
    }

    /**
     * 通知合并失败
     */
    private void notifyPatchFailed(int result) {

        if (onPatchListener != null) {
            onPatchListener.patchFailed(patchParam, result);
        } else {
            LogUtils.w("onPatchListener is Null!");
        }
    }

}


