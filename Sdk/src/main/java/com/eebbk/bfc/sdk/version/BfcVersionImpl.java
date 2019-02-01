package com.eebbk.bfc.sdk.version;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.OnCoreVerCheckListener;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.error.ErrorCodeAgent;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.sdk.download.Status;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.download.DownLoadController;
import com.eebbk.bfc.sdk.version.download.DownloadConstants;
import com.eebbk.bfc.sdk.version.download.DownloadExtraAgent;
import com.eebbk.bfc.sdk.version.download.DownloadInfo;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.error.ErrorCode;
import com.eebbk.bfc.sdk.version.listener.OnCheckStrategyListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;
import com.eebbk.bfc.sdk.version.listener.OnVersionDownloadListener;
import com.eebbk.bfc.sdk.version.patch.PatchController;
import com.eebbk.bfc.sdk.version.strategy.CheckStrategyFactory;
import com.eebbk.bfc.sdk.version.strategy.ICheckStrategy;
import com.eebbk.bfc.sdk.version.util.ApkUtils;
import com.eebbk.bfc.sdk.version.util.ExecutorsUtils;
import com.eebbk.bfc.sdk.version.util.ParamUtils;
import com.eebbk.bfc.sdk.version.util.Utils;
import com.eebbk.bfc.sdk.version.util.VersionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hesn
 * @function
 * @date 16-11-29
 * @company 步步高教育电子有限公司
 */

public class BfcVersionImpl implements BfcVersion {

    /**
     * 版本检测回调，给应用处理逻辑
     */
    private OnVersionCheckListener mOnVersionCheckListener;
    private OnVersionDownloadListener mOnVersionDownloadListener;
    private static final String TAG = "BfcVersionImpl";
    private BfcCoreVersion bfcCoreVersion;
    private Context mContext = null;
    private DownLoadController mDownLoadController;
    private PatchController mPatchController;
    private Settings mSettings;
    private ICheckStrategy iCheckStrategy;

    public BfcVersionImpl(@NonNull Context context, @Nullable Settings settings) {
        if (context == null) {
            throw new NullPointerException("BfcVersion context non null !");
        }
        if (settings == null) {
            settings = new Settings();
        }
        LogUtils.i(TAG, " init " + getLibVersion());
        mContext = context.getApplicationContext();
        init(settings);
    }

    /**
     * 检查版本
     * <br> 异步
     * <br> 检查当前app
     */
    @Override
    public void checkVersion() {
        LogUtils.v(TAG, "checkVersion");
        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                List<CheckParams> localVersionList = new ArrayList<CheckParams>();
                localVersionList.add(new CheckParams(getLocalVersion()));
                if (iCheckStrategy == null) {
                    onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_STRATEGY_NULL);
                    return;
                }
                iCheckStrategy.check(localVersionList);
            }
        });
    }

    /**
     * 检查版本
     * <br> 异步
     *
     * @param entities 需要检查的app信息
     */
    @Override
    public void checkVersion(@NonNull final List<CheckParams> entities) {
        LogUtils.v(TAG, "checkVersion list");
        if (ListUtils.isEmpty(entities)) {
            onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL);
            return;
        }

        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                if (iCheckStrategy == null) {
                    onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_STRATEGY_NULL);
                    return;
                }
                iCheckStrategy.check(entities);
            }
        });
    }

    /**
     * 上报app升级信息
     *
     * @param version apk版本信息
     * @param state   升级状态
     * @param info    升级信息
     */
    @Override
    public void reportUpdate(@NonNull Version version, int state, String info) {
        reportUpdate(getReportParamsFromVersion(version, state, info));
    }

    /**
     * 上报app升级信息
     *
     * @param requests 上报信息封装entitys
     */
    @Override
    public void reportUpdate(@NonNull List<RequestReportEntity> requests) {
        if (ListUtils.isEmpty(requests)) {
            onVersionCheckException(ErrorCode.SERVICE_REPORT_ENTITY_LIST_NULL);
            return;
        }
        bfcCoreVersion.reportUpdate(requests, new OnCoreVerCheckListener() {
            @Override
            public void onResponse(String respSrc, ResponseEntity respEntity) {
                LogUtils.d(TAG, "report respSrc:" + respSrc);
            }

            @Override
            public void onError(BfcHttpError error) {
                if (error == null) {
                    LogUtils.d(TAG, "BfcHttpError == null");
                    return;
                }
                LogUtils.d(TAG, "report error:" + error.toString());
            }
        });
    }

    /**
     * 注销
     * <br> 1.暂停所有任务
     * <br> 2.注销所有监听
     */
    @Override
    public void destroy() {
        if (bfcCoreVersion != null) {
            bfcCoreVersion.cancelAll();
        }
        Utils.destroy(mDownLoadController);
        Utils.destroy(mPatchController);
        Utils.destroy(iCheckStrategy);
        iCheckStrategy = null;
    }

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
    @Override
    public void setRequestHead(@NonNull String machineId, @NonNull String accountId, @NonNull String apkPackageName
            , @NonNull String apkVersionCode, @NonNull String deviceModel, @NonNull String deviceOSVersion) {
        bfcCoreVersion.setRequestHead(machineId, accountId, apkPackageName
                , apkVersionCode, deviceModel, deviceOSVersion);

        if (iCheckStrategy == null) {
            onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_STRATEGY_NULL);
        } else {
            iCheckStrategy.setRequestHead(machineId, accountId, apkPackageName
                    , apkVersionCode, deviceModel, deviceOSVersion);
        }
    }

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param remoteVersion 新版本信息
     */
    @Override
    public void beginDownload(@NonNull final Version remoteVersion) {
        if (remoteVersion == null) {
            onVersionCheckException(ErrorCode.SERVICE_BEGIN_DOWNLOAD_PARAMS_NULL);
            return;
        }
        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                doDownload(remoteVersion);
            }
        });
    }

    /**
     * 供外部手动添加新版本apk下载使用
     *
     * @param remoteVersions 新版本信息
     */
    @Override
    public void beginDownload(@NonNull List<Version> remoteVersions) {
        if (ListUtils.isEmpty(remoteVersions)) {
            onVersionCheckException(ErrorCode.SERVICE_BEGIN_DOWNLOAD_PARAMS_NULL);
            return;
        }

        for (Version version : remoteVersions) {
            beginDownload(version);
        }
    }

    @Override
    public void beginDownload(@NonNull ITask... iTask) {
        if (iTask == null || iTask.length == 0) {
            onVersionCheckException(ErrorCode.SERVICE_BEGIN_DOWNLOAD_PARAMS_NULL);
            return;
        }

        mDownLoadController.addTask(iTask);
    }

    /**
     * 获取当前应用版本信息
     *
     * @return 当前应用版本信息
     */
    @Override
    public Version getLocalVersion() {
        return new Version(mContext);
    }

    /**
     * 差分合并失败，重新下载apk，走全量升级流程
     *
     * @param info    下载信息
     * @param context 上下文
     */
    @Override
    public void downloadFullNewApk(@NonNull DownloadInfo info, @NonNull Context context) {
        if (Utils.calculateSpace(VersionConstants.PATH_B) < info.getFileSize()
                && Utils.calculateSpace(VersionConstants.PATH_SD) < info.getFileSize()) {
            LogUtils.w(TAG, "SDCARD空间不足!!");
            return;
        }

        if (info.getPatchAvailable() == VersionConstants.PATCH_AVAILABLE) {
            LogUtils.w(TAG, "非全量升级!");
            return;
        }
        ITask task = mDownLoadController.getDownloadTask(info);
        if (task == null) {
            LogUtils.w("The downloadTask can't be null");
            return;
        }
        mDownLoadController.addTask(task);
    }

    /**
     * 差分包合并
     *
     * @param context
     * @param info    下载后差分包信息
     */
    @Override
    public void patchApk(@NonNull Context context, @NonNull DownloadInfo info) {
        mPatchController.startPatch(context, info);
    }

    /**
     * 设置版本检查回调 如果有需要即时了解新版本检查的需求 需要设置该监听器
     *
     * @param ovcl 版本检测回调listener
     */
    @Override
    public void setOnVersionCheckListener(OnVersionCheckListener ovcl) {
        mOnVersionCheckListener = ovcl;
        mDownLoadController.setOnVersionCheckListener(mOnVersionCheckListener);
        mPatchController.setOnVersionCheckListener(mOnVersionCheckListener);
    }

    @Override
    public OnVersionCheckListener getOnVersionCheckListener() {
        return mOnVersionCheckListener;
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    @Override
    public synchronized void deleteAllDownloadTask() {
        List<ITask> list = mDownLoadController.getTaskList();
        if (ListUtils.isEmpty(list)) {
            return;
        }
        deleteDownloadTask(list);
    }

    @Override
    public void deleteDownloadTask(@NonNull String... packageNames) {
        if (packageNames == null || packageNames.length == 0) {
            return;
        }

        List<ITask> list = mDownLoadController.getTaskList();
        if (ListUtils.isEmpty(list)) {
            return;
        }

        List<ITask> delList = new ArrayList<ITask>();
        Arrays.sort(packageNames);
        for (ITask task : list) {
            if (Arrays.binarySearch(packageNames, DownloadExtraAgent.getPackageName(task)) >= 0) {
                delList.add(task);
            }
        }

        deleteDownloadTask(delList);
    }

    /**
     * 分类删除任务
     * <br> 正在差分包合并的任务只删除任务,不删除文件(合并过程中jni层异常无法捕获)
     * <br> 没有进行差分包合并的任务删除任务和文件
     *
     * @param list
     */
    private void deleteDownloadTask(List<ITask> list) {
        Map<String, DownloadInfo> mergingTask = mPatchController.getMergingTask();
        List<ITask> delWithFileList = new ArrayList<ITask>();
        List<ITask> delWithoutFileList = new ArrayList<ITask>();
        for (ITask task : list) {
            if (task == null) {
                continue;
            }
            if (isMergingTask(task, mergingTask)) {
                delWithoutFileList.add(task);
            } else {
                delWithFileList.add(task);
            }
        }

        if (!ListUtils.isEmpty(delWithFileList)) {
            //删除下载任务和下载文件
            int len = delWithFileList.size();
            LogUtils.d(TAG, "delete download task and file, size:" + len);
            mDownLoadController.deleteTaskAndAllFile(delWithFileList.toArray(new ITask[len]));
        }

        if (!ListUtils.isEmpty(delWithoutFileList)) {
            //删除下载任务,不删下载文件
            int len = delWithoutFileList.size();
            LogUtils.d(TAG, "delete download task without file, size:" + len);
            mDownLoadController.deleteTask(delWithoutFileList.toArray(new ITask[len]));
        }
    }

    /**
     * 安装pak
     *
     * @param context
     * @param file
     * @Deprecated 此接口不能校验apk信息, 不安全.建议使用 installApk(Context context, VersionInfo info)
     */
    @Deprecated
    @Override
    public void installApk(@NonNull Context context, @NonNull File file) {
        ApkUtils.installApk(context, file);
    }

    /**
     * 安装pak
     * <br> 会检验检验apk信息,防止安装文件被篡改
     *
     * @param context
     * @param info
     */
    @Override
    public void installApk(@NonNull Context context, @NonNull VersionInfo info) {
        if (!checkInstallApkParam(context, info)) {
            return;
        }
        LogUtils.d(TAG, "Apk's path:" + info.getApkFile().getAbsolutePath() + "\n\n info:" + info);

        ApkUtils.installApk(context, info.getApkFile());
    }

    /**
     * 获取库的版本信息
     *
     * @return
     */
    @Override
    public String getLibVersion() {
        return TextUtils.concat(
                SDKVersion.getLibraryName(), ", version: ", SDKVersion.getVersionName(),
                " code: ", String.valueOf(SDKVersion.getSDKInt()),
                " build: ", SDKVersion.getBuildName()
        ).toString();
    }

    /**
     * 获取所有版本更新下载任务
     *
     * @return
     */
    @Override
    public ArrayList<ITask> getAllDownloadTask() {
        return mDownLoadController.getTaskList();
    }

    /**
     * 获取版本更新的下载任务
     * <br> 根据检查更新返回的Version获取下载任务的Builder,可以根据此Builder添加下载相关设置
     * <br> 只有在该下载前添加的设置才能生效
     *
     * @param version
     * @return
     */
    @Override
    public ITask.Builder getDownloadTaskBuilder(@NonNull Version version) {
        return mDownLoadController.getDownloadTaskBuilder(version);
    }

    /**
     * 根据错误码获取错误信息
     *
     * @param errorCode
     * @return
     */
    @Override
    public String getErrorMsg(@NonNull String errorCode) {
        return ErrorCodeAgent.getErrorMsg(errorCode);
    }

    /**
     * 忽略版本
     * <br> 忽略比这版本(包括此版本)低的所有版本,并删除下载任务和对应下载文件
     *
     * @param info OnVersionCheckListener接口中onUpdateReady()函数返回的可安装信息
     */
    @Override
    public void ignoreVersion(@NonNull VersionInfo info) {
        if (info == null) {
            return;
        }
        FileUtils.deleteFile(info.getApkFile());
        mDownLoadController.deleteTaskAndAllFile(info.getDownloadId());
        ignoreVersion(info.getPackageName(), info.getRemoteVersionCode());
    }

    /**
     * 忽略版本
     * <br> 忽略比这版本(包括此版本)低的所有版本
     *
     * @param packageName 忽略版本的应用包名
     * @param versionCode 忽略版本的应用版本号
     */
    @Override
    public void ignoreVersion(@NonNull String packageName, int versionCode) {
        VersionUtil.setIgnoreVersionCode(mContext, packageName, versionCode);
    }

    /**
     * 获取已设置的忽略版本号
     *
     * @param packageName 忽略版本的应用包名
     * @return 返回无视的版本号;返回-1则无设置
     */
    @Override
    public int getIgnoreVersion(@NonNull String packageName) {
        return VersionUtil.getIgnoreVersionCode(mContext, packageName);
    }

    /**
     * 获取允许请求更新信息的网络类型
     *
     * @return
     */
    @Override
    public int getRequestNetworkType() {
        return mSettings.getRequestNetwork();
    }

    /**
     * 获取允许下载的网络类型
     *
     * @return
     */
    @Override
    public int getDownloadNetworkType() {
        //用户上层传的是版本更新库自定义的网络类新,这里返回的是已经转换成下载库识别的网络类新
        //不直接用下载库的网络类型原因:
        //1.核心库就需要这个网络类型,但是不想核心库就继承下载库.
        //2.两个库处理的网络类型是有差异的
        //3.避免以后下载库对网络类型扩展后,导致冲突
        return mSettings.getDownloadNetwork();
    }

    /**
     * 是否需要静默安装
     *
     * @param info
     * @return
     */
    @Override
    public boolean isSilentInstall(@NonNull VersionInfo info) {
        return info != null && isSilentInstall(info.getUpdateMode());
    }

    /**
     * 是否需要静默安装
     *
     * @param version
     * @return
     */
    @Override
    public boolean isSilentInstall(@NonNull Version version) {
        return version != null && isSilentInstall(version.getUpdateMode());
    }

    /**
     * 是否需要静默安装
     *
     * @param updateMode 升级方式类型
     * @return
     */
    @Override
    public boolean isSilentInstall(int updateMode) {
        return updateMode == VersionConstants.UPDATE_MODE_SILENT;
    }

    /**
     * 下载监听
     */
    @Override
    public void setOnVersionDownloadListener(OnVersionDownloadListener l) {
        mOnVersionDownloadListener = l;
        mDownLoadController.setOnVersionDownloadListener(mOnVersionDownloadListener);
    }

    @Override
    public OnVersionDownloadListener getOnVersionDownloadListener() {
        return mOnVersionDownloadListener;
    }

    private void init(Settings settings) {
        mSettings = settings;
        ErrorCodeAgent.addErrCodes(ErrorCode.getErrorCodes());

        mDownLoadController = new DownLoadController(mContext, this, mSettings);
        mPatchController = new PatchController(this);
        //初始化核心库
        bfcCoreVersion = new BfcCoreVersion.Builder()
                .setSettings(settings)
                .build(mContext);

        // 提前初始化请求头中需要的耗时参数
        initRequestHeadParam(mContext);

        //检查策略
        iCheckStrategy = CheckStrategyFactory.create(settings.getCheckStrategy(), mContext, this
                , bfcCoreVersion, settings, mDownLoadController, mOnCheckStrategyListener);
    }

    /**
     * 安装前校验
     *
     * @param info
     * @return
     */
    private boolean checkInstallApkParam(Context context, VersionInfo info) {
        if (context == null) {
            onVersionCheckException(ErrorCode.SERVICE_INSTALL_FAILED_CONTEXT_NULL);
            return false;
        }

        if (info == null) {
            onVersionCheckException(ErrorCode.SERVICE_INSTALL_VERSION_INFO_NULL);
            return false;
        }

        if (info.getApkFile() == null || TextUtils.isEmpty(info.getApkFile().getAbsolutePath())) {
            onVersionCheckException(ErrorCode.SERVICE_INSTALL_APK_FILE_NULL);
            return false;
        }

        String path = info.getApkFile().getAbsolutePath();
        if (path.endsWith(VersionConstants.PATCH_SUFFIX)) {
            onVersionCheckException(ErrorCode.SERVICE_CANNOT_INSTALL_PATCH_FILE);
            return false;
        }

        if (!FileUtils.isFileExists(info.getApkFile())) {
            onVersionCheckException(ErrorCode.SERVICE_INSTALL_FAILED_NOT_EXISTS);
            return false;
        }

        //服务器远程版本信息
        String remotePackageName = info.getPackageName();
        String remoteVersionName = info.getRemoteVersionName();
        int remoteVersionCode = info.getRemoteVersionCode();

        //安装apk文件信息
        String apkFilePackageName = ApkUtils.getApkFilePackageName(context, path);
        String apkFileVersionName = ApkUtils.getApkFileVersionName(context, path);
        int apkFileVersionCode = ApkUtils.getApkFileVersionCode(context, path);

        //安装前还是校验安装文件信息,避免安装前文件被替换了
        if (!TextUtils.equals(remotePackageName, apkFilePackageName)
                || !TextUtils.equals(remoteVersionName, apkFileVersionName)
                || remoteVersionCode != apkFileVersionCode) {
            String errorMsg = TextUtils.concat("\n",
                    "remotePackageName:", remotePackageName, "\n",
                    "remoteVersionName:", remoteVersionName, "\n",
                    "remoteVersionCode:", String.valueOf(remoteVersionCode), "\n",
                    "apkFilePackageName:", apkFilePackageName, "\n",
                    "apkFileVersionName:", apkFileVersionName, "\n",
                    "apkFileVersionCode:", String.valueOf(apkFileVersionCode)
            ).toString();
            onVersionCheckException(ErrorCode.SERVICE_INSTALL_FAILED_INFO, errorMsg);
            return false;
        }

        //安装前还是校验下md5,避免安装前文件被替换了,服务器有时候返回的md5为空
//        if (!Md5Utils.checkMd5(info.getApkFile(), info.getMd5())) {
//            onVersionCheckException(ErrorCode.SERVICE_INSTALL_FAILED_MD5);
//            return false;
//        }

        //寂寞安装检查
        if (isSilentInstall(info)) {
            onVersionCheckException(ErrorCode.SERVICE_CHECK_INSTALL_UPDATE_MODE
                    , " updateMode = " + info.getUpdateMode());
        }

        return true;
    }

    private void checkOver() {
        LogUtils.v(VersionConstants.TAG, "on version check over");
        if (mOnVersionCheckListener != null) {
            LogUtils.v(VersionConstants.TAG, "mOnVersionCheckListener.onCheckOver...");
            mOnVersionCheckListener.onCheckOver();
        } else {
            LogUtils.v(VersionConstants.TAG, "mOnVersionCheckListener is null.");
        }
    }

    private void dealOldTask(int status, boolean needCheckFile, ITask task) {
        switch (status) {
            case Status.DOWNLOAD_FAILURE:
                // 状态是失败的,可进行重试!!
                Log.v(TAG, "任务下载失败!!!");
                if (mOnVersionDownloadListener != null) {
                    mOnVersionDownloadListener.onDownloadFailure(task, "已有此任务,状态:下载失败", new Throwable("已有此任务,状态:下载失败"));
                }
                if (mSettings.isAutoDownload() && mSettings.isAutoReloadDownloadFailedTask()) {
                    // 进行重试
                    mDownLoadController.reloadTask(task);
                }
                break;
            case Status.CHECK_FAILURE:
                // 状态是失败的,可进行重试!!
                Log.v(TAG, "文件校验失败!!!");
                if (mOnVersionDownloadListener != null) {
                    mOnVersionDownloadListener.onDownloadFailure(task, "已有此任务,状态:文件校验失败", new Throwable("已有此任务,状态:文件校验失败"));
                }
                if (mSettings.isAutoDownload() && mSettings.isAutoReloadCheckFailedTask()) {
                    // 进行重试
                    mDownLoadController.reloadTask(task);
                }
                break;
            case Status.DOWNLOAD_SUCCESS:
                if (!needCheckFile) {
                    // 状态是成功的,要进行通知!!
                    updateReady(mContext, task);
                }
                break;
            case Status.CHECK_SUCCESS:
                if (needCheckFile) {
                    // 状态是成功的,要进行通知!!
                    updateReady(mContext, task);
                }
                break;
            case Status.DOWNLOAD_PAUSE:
                if (mOnVersionDownloadListener != null) {
                    mOnVersionDownloadListener.onDownloadPause(task, "已有此任务,状态:暂停");
                }
                if (mSettings.isAutoDownload()) {
                    // 状态是
                    Log.v(TAG, "任务已暂停,现在恢复了!!!");
                    // 恢复任务
                    mDownLoadController.resumeTask(task);
                }
                break;
            case Status.DOWNLOAD_WAITING:
            case Status.DOWNLOAD_PROGRESS:
                Log.v(TAG, "正常有更新,并且正在下载中,请等待!!");
                break;
            default:
                Log.e(TAG, "下载状态异常???  status:" + status + " fileName:" + task.getFileName());
                break;
        }
    }

    private void updateReady(Context context, ITask task) {
        LogUtils.v(TAG, "download success updateReady()");
        if (task == null) {
            LogUtils.w(TAG, "call updateReady() failed,task is null!");
            return;
        }

        String path = getUpdateReadyApkPath(task);
        LogUtils.d(TAG, "getUpdateReadyApkPath:" + path);
        if (TextUtils.isEmpty(path)) {
            // 文件不存在,删掉记录
            mDownLoadController.deleteTaskAndAllFile(task);
            onVersionCheckException(ErrorCode.TASK_DATA_EXCEPTION);
            return;
        }

        File file = new File(path);
        if (!checkUpdateReadyParam(task, file)) {
            return;
        }
        LogUtils.d(TAG, "path:" + path);

        if (mOnVersionCheckListener != null) {

            VersionInfo info = new VersionInfo(
                    context, task.getId(),
                    task.getStringExtra(DownloadConstants.TASK_TAG_NEW_VERSION_NAME),
                    task.getIntExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, 0),
                    task.getStringExtra(DownloadConstants.TASK_TAG_APK_INTRODUCE), file,
                    task.getIntExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_CODE, 0),
                    task.getStringExtra(DownloadConstants.TASK_TAG_CURRENT_VERSION_NAME),
                    task.getStringExtra(isDownloadPatchFile(task) ? DownloadConstants.TASK_TAG_PATCH_MD5 : DownloadConstants.TASK_TAG_APK_MD5),
                    DownloadExtraAgent.getPackageName(task),
                    task.getIntExtra(DownloadConstants.TASK_TAG_UPDATE_MODE, VersionConstants.UPDATE_MODE_DEFAULT),
                    task.getStringExtra(DownloadConstants.TASK_TAG_UPDATE_INFORMATION)
            );
            mOnVersionCheckListener.onUpdateReady(context, info);
        }
    }

    /**
     * 获取可安装的文件真实路径
     *
     * @param task
     * @return
     */
    private String getUpdateReadyApkPath(ITask task) {
        if (task == null) {
            return null;
        }
        String saveFileName = task.getFileName();
        String savePath = task.getSavePath();
        return isDownloadPatchFile(task) ? VersionConstants.PATH_SD + VersionConstants.DOWNLOAD_APK_FOLDER +
                File.separator + task.getStringExtra(DownloadConstants.TASK_TAG_APK_FILE_NAME)
                : savePath + File.separator + saveFileName;
    }

    /**
     * 判断下载好的文件是全量还是增量
     *
     * @param task
     * @return
     */
    private boolean isDownloadPatchFile(ITask task) {
        if (task == null) {
            return false;
        }
        String saveFileName = task.getFileName();
        String savePath = task.getSavePath();
        if (TextUtils.isEmpty(savePath) || TextUtils.isEmpty(saveFileName)) {
            return false;
        }
        // 差分包返回合并后的路径
        return saveFileName.endsWith(VersionConstants.PATCH_SUFFIX);
    }

    private boolean checkUpdateReadyParam(ITask task, File file) {
        if (!FileUtils.isFileExists(file)) {
            // 文件不存在,删掉记录
            mDownLoadController.deleteTaskAndAllFile(task);
            onVersionCheckException(ErrorCode.TASK_DATA_EXCEPTION);
            return false;
        }

        if (task.getId() == -1) {
            // ID异常,删掉记录,删文件
            mDownLoadController.deleteTaskAndAllFile(task);
            onVersionCheckException(ErrorCode.TASK_DATA_EXCEPTION);
            return false;
        }

        if (task.getIntExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, 0) < 0) {
            // 版本号小于0,删记录,删文件
            mDownLoadController.deleteTaskAndAllFile(task);
            onVersionCheckException(ErrorCode.TASK_DATA_EXCEPTION);
            return false;
        }

        return true;
    }

    /**
     * 检查服务器版本信息是否正确，保证最基础参数
     *
     * @param version 服务器版本信息
     * @return true:可用;false:不可用
     */
    private boolean checkVersionParamsAvailable(Version version) {
        //这里区分下载增量包还是patch包
        if (!mSettings.isAlwaysFullUpgrade() && version.getPatchAvailable() == VersionConstants.PATCH_AVAILABLE) {
            if (TextUtils.isEmpty(version.getPatchUrl()) && TextUtils.isEmpty(version.getPatchMd5())
                    && TextUtils.isEmpty(version.getApkMd5())) {
                return false;
            }
            LogUtils.d("Current update model is Patch ！");
        } else {
            if (TextUtils.isEmpty(version.getApkUrl())
                    && TextUtils.isEmpty(version.getApkMd5())) {
                return false;
            }
            LogUtils.d("Current update model is full apk ！");
        }
        return true;
    }

    private synchronized void doDownload(final Version remoteVersion) {
        long fileSize = remoteVersion.getFileSize();
        if (Utils.calculateSpace(VersionConstants.PATH_B) < fileSize
                && Utils.calculateSpace(VersionConstants.PATH_SD) < fileSize) {
            LogUtils.w(TAG, "SDCARD空间不足!!");
            return;
        }

        if (!checkVersionParamsAvailable(remoteVersion)) {
            LogUtils.w(TAG, "RemoteVersion params is Invalid!");
            return;
        }

        mDownLoadController.addTask(mDownLoadController.getDownloadTask(remoteVersion));
    }

    private void onVersionCheckException(String errorCode) {
        onVersionCheckException(errorCode, "");
    }

    private void onVersionCheckException(String errorCode, String detail) {
        LogUtils.bfcExceptionLog(TAG, errorCode, detail);
        if (mOnVersionCheckListener != null) {
            mOnVersionCheckListener.onVersionCheckException(errorCode);
        }
    }

    private void onNewVersionChecked(List<Version> versions) {
        if (mOnVersionCheckListener != null) {
            mOnVersionCheckListener.onNewVersionChecked(versions);
        }
    }

    private List<RequestReportEntity> getReportParamsFromVersion(Version version, int state, String info) {
        List<RequestReportEntity> list = new ArrayList<RequestReportEntity>();
        if (version == null) {
            return list;
        }

        RequestReportEntity request = new RequestReportEntity();
        request.setPackageName(version.getPackageName());
        request.setVersionName(version.getCurrentVersionName());
        request.setVersionCode(version.getCurrentVersionCode());
        request.setNewVersionName(version.getVersionName());
        request.setNewVersionCode(version.getVersionCode());
        request.setStateCode(state);
        request.setStateInfo(info);
        request.setId(version.getServerFileId());

        list.add(request);
        return list;
    }

    private void initRequestHeadParam(final Context context){
        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                ParamUtils.clean();
                ParamUtils.getMachineId(context);
                ParamUtils.getPackageName(context);
                ParamUtils.getVersionCode(context);
                ParamUtils.getDeviceModel();
                ParamUtils.getDeviceOSVersion();
            }
        });
    }

    /**
     * 是否为正在差分包合并的任务
     *
     * @param task
     * @param mergingTask
     * @return
     */
    private boolean isMergingTask(ITask task, Map<String, DownloadInfo> mergingTask) {
        if (task == null || mergingTask == null || mergingTask.isEmpty()) {
            return false;
        }
        return mergingTask.containsKey(DownloadExtraAgent.getPackageName(task));
    }

    private final OnCheckStrategyListener mOnCheckStrategyListener = new OnCheckStrategyListener() {
        @Override
        public void checkOverStrategy() {
            checkOver();
        }

        @Override
        public void onVersionCheckStrategyException(String errorCode, String detail) {
            onVersionCheckException(errorCode, detail);
        }

        @Override
        public void onNewVersionCheckedStrategy(List<Version> versions) {
            onNewVersionChecked(versions);
        }

        @Override
        public void dealOldTaskStrategy(int status, boolean needCheckFile, ITask task) {
            dealOldTask(status, needCheckFile, task);
        }
    };

}
