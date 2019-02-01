package com.eebbk.bfc.sdk.version.download;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.download.listener.OnDownloadListener;
import com.eebbk.bfc.sdk.downloadmanager.DownloadController;
import com.eebbk.bfc.sdk.downloadmanager.DownloadListener;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.Settings;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionDownloadListener;
import com.eebbk.bfc.sdk.version.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Destroyable;

/**
 * @author hesn
 * @function
 * @date 16-11-12
 * @company 步步高教育电子有限公司
 */

public class DownLoadController implements Destroyable {

    private static final String MODULE_NAME = VersionConstants.TAG;
    private final DownloadListener mListener = new DownloadListener();
    private static final String TAG = "DownLoadController";
    private Context mContext;
    private BfcVersion mBfcVersion;
    private Settings mSettings;
    private OnVersionDownloadListener mOnVersionDownloadListener;
    private OnVersionCheckListener mOnVersionCheckListener;
    private long lastRefreshTime = -1;

    public DownLoadController(Context context, BfcVersion bfcVersion, Settings settings) {
        if (context == null) {
            throw new NullPointerException("BfcVersion DownLoadController context non null!");
        }
        this.mBfcVersion = bfcVersion;
        this.mSettings = settings;
        mContext = context.getApplicationContext();
        //getGlobalConfig() 是为了判断当前下载控件有无初始化,如果报错,证明没有初始化
        //目的:避免初始化时候把上层app的下载控件初始化的设置冲掉
        try {
            DownloadController.getInstance().getGlobalConfig();
        } catch (Exception e) {
            e.printStackTrace();
            DownloadController.init(mContext);
        }
        registerListener();
    }

    public void setOnVersionDownloadListener(OnVersionDownloadListener l) {
        mOnVersionDownloadListener = l;
    }

    public void setOnVersionCheckListener(OnVersionCheckListener l) {
        mOnVersionCheckListener = l;
    }

    /**
     * 下载监听
     */
    private final OnDownloadListener mDownloadListener = new OnDownloadListener() {

        @Override
        public void onDownloadWaiting(ITask task) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadWaiting(task);
            }
        }

        @Override
        public void onDownloadStarted(ITask task) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadStarted(task);
            }
        }

        @Override
        public void onDownloadConnected(ITask task, boolean resuming, long finishedSize, long totalSize) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadConnected(task, resuming, finishedSize, totalSize);
            }
        }

        @Override
        public void onDownloading(ITask task, long finishedSize, long totalSize) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloading(task, finishedSize, totalSize);
            }

            if (VersionConstants.DEBUG) {
                long time = SystemClock.elapsedRealtime();
                if (time - lastRefreshTime < 50) {
                    return;
                }
                lastRefreshTime = time;
                LogUtils.v(TAG, task.getFileName() + " finishedSize:" + finishedSize + " totalSize:" + totalSize);
            }
        }

        @Override
        public void onDownloadPause(ITask task, String errorCode) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadPause(task, errorCode);
            }
        }

        @Override
        public void onDownloadRetry(ITask task, int retries, String errorCode, Throwable throwable) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadRetry(task, retries, errorCode, throwable);
            }
        }

        @Override
        public void onDownloadFailure(ITask task, String errorCode, Throwable throwable) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadFailure(task, errorCode, throwable);
            }
        }

        @Override
        public void onDownloadSuccess(ITask task) {
            if (mOnVersionDownloadListener != null) {
                mOnVersionDownloadListener.onDownloadSuccess(task);
            }
            downloadComplete(mContext, task);
        }

    };

    /**
     * 获取版本更新下载任务
     *
     * @return
     */
    public ArrayList<ITask> getTaskList() {
        return DownloadController.getInstance().getTask(MODULE_NAME);
    }

    /**
     * 添加任务
     *
     * @param pTasks
     */
    public void addTask(ITask... pTasks) {
        DownloadController.getInstance().addTask(pTasks);
    }

    /**
     * 删除任务同时删除所有文件
     *
     * @param pTasks
     */
    public void deleteTaskAndAllFile(ITask... pTasks) {
        DownloadController.getInstance().deleteTaskAndAllFile(pTasks);
    }

    /**
     * 删除任务同时删除所有文件
     *
     * @param ids
     */
    public void deleteTaskAndAllFile(int... ids) {
        DownloadController.getInstance().deleteTaskAndAllFile(ids);
    }

    /**
     * 删除任务(不删除所有文件)
     *
     * @param pTasks
     */
    public void deleteTask(ITask... pTasks) {
        DownloadController.getInstance().deleteTaskWithoutFile(pTasks);
    }

    /**
     * 删除任务(不删除所有文件)
     *
     * @param ids
     */
    public void deleteTask(int... ids) {
        DownloadController.getInstance().deleteTaskWithoutFile(ids);
    }

    /**
     * 重新下载 根据下载任务
     *
     * @param pTasks
     */
    public void reloadTask(ITask... pTasks) {
        DownloadController.getInstance().reloadTask(pTasks);
    }

    /**
     * 暂停任务
     *
     * @param pTasks
     */
    public void pauseTask(ITask... pTasks) {
        DownloadController.getInstance().pauseTask(pTasks);
    }

    /**
     * 恢复下载 根据下载任务
     *
     * @param pTasks
     */
    public void resumeTask(ITask... pTasks) {
        DownloadController.getInstance().resumeTask(pTasks);
    }

    public ITask getDownloadTask(Version version) {
        return getDownloadTaskBuilder(version).build();
    }

    public ITask.Builder getDownloadTaskBuilder(Version version) {
        if (version == null) {
            return null;
        }

        String fileDir = Utils.creatorDownloadDirectory(version);

        if (TextUtils.isEmpty(fileDir)) {
            LogUtils.w("fileDir is null !");
            return null;
        }

        version.setFilePath(fileDir);

        String downloadUrl = version.getDownloadUrl();
        String fileName = version.getFileName();

        String fileMd5 = version.getFileMd5();
        LogUtils.d(TAG, "beginDownload fileDir " + fileDir);
        LogUtils.d(TAG, "beginDownload downloadUrl " + downloadUrl);
        LogUtils.d(TAG, "beginDownload fileName " + fileName);

        // 创建任务并配置参数
        return DownloadController.buildTask(downloadUrl)
                .setFileName(fileName) // 设置文件名，可包含文件后缀
                .setSavePath(fileDir) //设置下载文件保存路径
                .setCheckType(ITask.CheckType.MD5)//设置校验类型
                .setCheckCode(fileMd5)//设置校验码
                .setCheckEnable(false)
                .setNetworkTypes(mSettings.getDownloadNetwork()) //设置下载可以使用的网络类型
                .setModuleName(MODULE_NAME) // 设置任务所属模块

                .putExtra("##auth##", "Gegeda_8_27")
                // 不直接将version转json保存是
                // 1.因为有些参数对应不上,比如versionCode是新版本的,传进来的是当前版本的
                // 2.有些地方单独调用会方便点
                .putExtra(DownloadConstants.TASK_TAG_MODULE, version.getModuleName())
                .putExtra(DownloadConstants.TASK_TAG_PACKAGE, version.getPackageName())
                .putExtra(DownloadConstants.TASK_TAG_APK_FILE_NAME, version.getApkFileName())
                .putExtra(DownloadConstants.TASK_TAG_PATCH_FILE_NAME, version.getPatchFileName())
                .putExtra(DownloadConstants.TASK_TAG_FUNCTION, VersionConstants.TAG)
                .putExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, version.getVersionCode())
                .putExtra(DownloadConstants.TASK_TAG_NEW_VERSION_NAME, version.getVersionName())
                .putExtra(DownloadConstants.TASK_TAG_UPDATE_INFORMATION, version.getUpdateinformation())
                .putExtra(DownloadConstants.TASK_TAG_SERVER_FILE_ID, version.getServerFileId())
                .putExtra(DownloadConstants.TASK_TAG_PATCH_AVAILABLE, version.getPatchAvailable())
                .putExtra(DownloadConstants.TASK_TAG_PATCH_MD5, version.getPatchMd5())
                .putExtra(DownloadConstants.TASK_TAG_APK_DOWNLOAD_URL, version.getApkUrl())
                .putExtra(DownloadConstants.TASK_TAG_APK_MD5, version.getApkMd5())
                .putExtra(DownloadConstants.TASK_TAG_ALWAYS_FULL_UPGRADE, version.isAlwaysFullUpgrade())
                .putExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_CODE, version.getCurrentVersionCode())
                .putExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_NAME, version.getCurrentVersionName())
                .putExtra(DownloadConstants.TASK_TAG_CURRENT_APK_FILE_NAME, version.getCurrentApkFileName())
                .putExtra(DownloadConstants.TASK_TAG_CURRENT_APK_FILE_PATH, version.getCurrentApkFilePath())
                .putExtra(DownloadConstants.TASK_TAG_PATCH_MERGE_TYPE, version.getMergeType())
                .putExtra(DownloadConstants.TASK_TAG_UPDATE_MODE, version.getUpdateMode())
                // 下载库 3.0.0 不支持long,以后有long型再改
                .putExtra(DownloadConstants.TASK_TAG_FILE_SIZE, String.valueOf(version.getFileSize()))
                .putExtra(DownloadConstants.TASK_TAG_PATCH_SIZE, String.valueOf(version.getPatchSize()))
                .putExtra(DownloadConstants.TASK_TAG_APK_INTRODUCE, version.getApkIntroduce())
                .putExtra(DownloadConstants.TASK_TAG_TIP, version.getTip());
    }

    private void registerListener() {
        mListener.setOnDownloadListener(mDownloadListener);
        DownloadController.getInstance().registerTaskListener(mListener, MODULE_NAME);
    }

    private void unregisterListener() {
        DownloadController.getInstance().unregisterTaskListener(mListener, MODULE_NAME);
    }

    @Override
    public void destroy() {
        List<ITask> tasks = getTaskList();
        if (!ListUtils.isEmpty(tasks)) {
            pauseTask(tasks.toArray(new ITask[tasks.size()]));
        }
        unregisterListener();
        mOnVersionCheckListener = null;
        mOnVersionDownloadListener = null;
        mBfcVersion = null;
    }

    @Override
    public boolean isDestroyed() {
        return mBfcVersion == null;
    }

    /**
     * 下载完成
     *
     * @param context 上下文
     * @return true:下载成功，false：下载失败
     */
    private void downloadComplete(Context context, ITask task) {
        LogUtils.v(TAG, "version download complete");
        DownloadInfo info = new DownloadInfo(task);
        boolean setIntentSuccess = info.checkSetIntent(task);

        if (!setIntentSuccess) {
            return;
        }

        String filePath = task.getSavePath() + File.separator + task.getFileName();
        LogUtils.d("downloadComplete filePath =" + filePath);

        if (TextUtils.isEmpty(filePath)) {
            // 文件不存在,删掉记录
            deleteTaskAndAllFile(task);
            LogUtils.w(TAG, "filePath is Null");
            reportUpdate(info, Constants.REPORT_UPDATE_FAILED, "下载失败！");
            return;
        }

        File file = new File(filePath);
        if (!FileUtils.isFileExists(file)) {
            // 文件不存在,删掉记录
            deleteTaskAndAllFile(task);
            LogUtils.w(TAG, "file is not exist");
            reportUpdate(info, Constants.REPORT_UPDATE_FAILED, "下载失败！");
            return;
        }

        if (!checkParamsAvailable(task.getId(), info.getVersionCode(), info.getVersionName())) {
            LogUtils.w(TAG, "check params available failed");
            deleteTaskAndAllFile(task);
            reportUpdate(info, Constants.REPORT_UPDATE_FAILED, "下载失败！");
            return;
        }

        if (!task.getBooleanExtra(DownloadConstants.TASK_TAG_ALWAYS_FULL_UPGRADE, false)
                && info.isPatchAvailable()) {
            LogUtils.v(TAG, "start patch apk");
            info.setPatchFilePath(filePath);
            if (mBfcVersion != null) {
                mBfcVersion.patchApk(context, info);
            }
        } else {
            LogUtils.v(TAG, "version update ready");
            reportUpdate(info, Constants.REPORT_UPDATE_SUCCESS, "全量包下载成功！");
            VersionInfo versionInfo = new VersionInfo(context, task.getId(),
                    info.getVersionName(), info.getVersionCode(),
                    info.getApkIntroduce(), file,
                    task.getIntExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_CODE, 0),
                    task.getStringExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_NAME),
                    task.getStringExtra(DownloadConstants.TASK_TAG_APK_MD5),
                    DownloadExtraAgent.getPackageName(task),
                    task.getIntExtra(DownloadConstants.TASK_TAG_UPDATE_MODE, VersionConstants.UPDATE_MODE_DEFAULT),
                    task.getStringExtra(DownloadConstants.TASK_TAG_UPDATE_INFORMATION)
            );
            onUpdateReady(context, versionInfo);
        }
    }

    /**
     * 检查返回下载参数是否正常
     *
     * @param id
     * @param newVersionCode
     * @param newVersionName
     * @return
     */
    private boolean checkParamsAvailable(long id, int newVersionCode, String newVersionName) {

        if (id == -1) {
            // ID异常,删掉记录,删文件
            LogUtils.w(TAG, "The id is Invalid ! [ id =" + id + "]");
            return false;
        }

        if (TextUtils.isEmpty(newVersionName)) {
            // 版本字段为空,删记录,删文件
            LogUtils.w(TAG, "The params is Empty ! [newVersionCode =" + newVersionCode + "],[newVersionName =" + "newVersionName]");
            return false;
        }

        if (newVersionCode < 0) {
            // 版本号小于0,删记录,删文件
            LogUtils.w(TAG, "The versionCode is Invalid ![ versionCode =" + newVersionCode + "]");
            return false;
        }

        return true;
    }

    /**
     * 上报app升级信息
     *
     * @param version
     * @param state
     * @param info
     */
    private void reportUpdate(Version version, int state, String info) {
        LogUtils.d(TAG, "state =" + state + "info =" + info);
        if (mBfcVersion != null) {
            mBfcVersion.reportUpdate(version, state, info);
        }
    }

    private void onUpdateReady(Context context, VersionInfo info) {
        if (mOnVersionCheckListener == null) {
            return;
        }
        mOnVersionCheckListener.onUpdateReady(context, info);
    }

}
