package com.eebbk.bfc.sdk.version.strategy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.OnCoreVerCheckListener;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.DataInfoEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.sdk.download.Status;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.Settings;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.download.DownLoadController;
import com.eebbk.bfc.sdk.version.download.DownloadConstants;
import com.eebbk.bfc.sdk.version.download.DownloadExtraAgent;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.error.ErrorCode;
import com.eebbk.bfc.sdk.version.listener.OnCheckStrategyListener;
import com.eebbk.bfc.sdk.version.util.ApkUtils;
import com.eebbk.bfc.sdk.version.util.ExecutorsUtils;
import com.eebbk.bfc.sdk.version.util.ParamUtils;
import com.eebbk.bfc.sdk.version.util.VersionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 17-2-21
 * @company 步步高教育电子有限公司
 */

public abstract class ACheckStrategy implements ICheckStrategy {

    private static final String TAG = "ACheckStrategy";
    private BfcCoreVersion bfcCoreVersion;
    private final Context mContext;
    private final Settings mSettings;
    final DownLoadController mDownLoadController;
    private BfcVersion bfcVersion;
    private OnCheckStrategyListener mOnCheckStrategyListener;

    protected abstract void remoteRespVerCheck(ResponseEntity respEntity, List<CheckParams> entities);

    ACheckStrategy(@NonNull Context context, @NonNull BfcVersion bfcVersion
            , @NonNull BfcCoreVersion bfcCoreVersion, @NonNull Settings settings
            , @NonNull DownLoadController downLoadController, @NonNull OnCheckStrategyListener l) {
        this.mContext = context.getApplicationContext();
        this.bfcVersion = bfcVersion;
        this.mSettings = settings;
        this.mDownLoadController = downLoadController;
        this.mOnCheckStrategyListener = l;
        this.bfcCoreVersion = bfcCoreVersion;
    }

    @Override
    public void setRequestHead(@NonNull String machineId, @NonNull String accountId, @NonNull String apkPackageName, @NonNull String apkVersionCode, @NonNull String deviceModel, @NonNull String deviceOSVersion) {
        if (bfcCoreVersion == null) {
            return;
        }
        bfcCoreVersion.setRequestHead(machineId, accountId, apkPackageName
                , apkVersionCode, deviceModel, deviceOSVersion);
    }

    @Override
    public void destroy() {
        synchronized (TAG){
            bfcVersion = null;
            bfcCoreVersion = null;
            mOnCheckStrategyListener = null;
        }
    }

    @Override
    public boolean isDestroyed() {
        return bfcVersion == null;
    }

    /**
     * 获取服务器版本信息
     *
     * @return 远程服务器版本信息
     */
    void startCheckVersion(final List<CheckParams> entities) {
        synchronized (TAG) {
            if (bfcCoreVersion == null) {
                LogUtils.w(TAG, "version check error, bfcCoreVersion == null");
                return;
            }

            if (bfcCoreVersion.isRequestHeadEmpty()) {
                initRequestHead();
            }

            bfcCoreVersion.checkVersion(getRequestVersionList(entities), new OnCoreVerCheckListener() {

                @Override
                public void onResponse(String respSrc, final ResponseEntity respEntity) {
                    ExecutorsUtils.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.v(TAG, "bfc version check remote version");
                            remoteRespVerCheck(respEntity, entities);
                            checkOver();
                        }
                    });
                }

                @Override
                public void onError(BfcHttpError error) {
                    onVersionCheckException(ErrorCode.SERVICE_NET_WORK_EXCEPTION,
                            error == null ? "" : TextUtils.concat(
                                    " BfcHttpError errorcode:", error.getErrorCode(),
                                    ", errorMsg:", error.getMsg()
                            ).toString());
                    checkOver();
                }
            });
        }
    }

    private void initRequestHead() {
        bfcCoreVersion.setRequestHead(
                ParamUtils.getMachineId(mContext),
                "",
                ParamUtils.getPackageName(mContext),
                String.valueOf(ParamUtils.getVersionCode(mContext)),
                ParamUtils.getDeviceModel(),
                ParamUtils.getDeviceOSVersion()
        );
    }

    /**
     * 旧任务中已有此更新任务,通知上层
     *
     * @param tasks
     */
    void oldTaskCallBack(List<ITask> tasks) {
        if (ListUtils.isEmpty(tasks)) {
            return;
        }
        List<Version> oldTaskCallBackList = new ArrayList<Version>();
        for (ITask task : tasks) {
            if (task == null) {
                continue;
            }
            if (isTaskDownloadSuccess(task)) {
                //下载好了的任务旧不需要再回调 onNewVersionChecked(),直接回调onUpdateReady()
                continue;
            }
            oldTaskCallBackList.add(new Version(task));
        }
        if (!ListUtils.isEmpty(oldTaskCallBackList)) {
            onNewVersionChecked(oldTaskCallBackList);
        }
    }

    private boolean isTaskDownloadSuccess(ITask task) {
        boolean needCheckFile = task.needCheckFile();
        int state = task.getState();
        return state == Status.DOWNLOAD_SUCCESS && !needCheckFile || state == Status.CHECK_SUCCESS && needCheckFile;

    }

    void hasDownloadTask(List<ITask> tasks) {
        if (ListUtils.isEmpty(tasks)) {
            return;
        }
        for (ITask task : tasks) {
            if (task == null) {
                continue;
            }
            dealOldTask(task.getState(), task.needCheckFile(), task);
        }
    }

    /**
     * List<Version> 转 List<RequestVersionEntity>
     *
     * @param entities
     * @return
     */
    private List<RequestVersionEntity> getRequestVersionList(List<CheckParams> entities) {
        List<RequestVersionEntity> qvList = new ArrayList<RequestVersionEntity>();
        for (CheckParams version : entities) {
            if (version == null) {
                continue;
            }
            RequestVersionEntity entity = getRequestVersion(version);
            qvList.add(entity);
        }
        return qvList;
    }

    private RequestVersionEntity getRequestVersion(CheckParams version) {
        RequestVersionEntity entity = new RequestVersionEntity();
        entity.setPackageName(version.getPackageName());
        entity.setVersionCode(version.getVersionCode());
        entity.setVersionName(version.getVersionName());
        entity.setMd5(version.getMd5());
        entity.setApkMd5(version.getApkMd5());
        entity.setDeviceModel(getString(version.getDeviceModel(), ParamUtils.getDeviceModel()));
        entity.setDeviceOSVersion(getString(version.getDeviceOSVersion(), ParamUtils.getDeviceOSVersion()));
        entity.setMachineId(getString(version.getMachineId(), ParamUtils.getMachineId(mContext)));
        return entity;
    }

    private String getString(String str, String def) {
        return TextUtils.isEmpty(str) ? def : str;
    }

    ArrayList<ITask> filterUnUseTask(ArrayList<ITask> taskList, List<CheckParams> entities) {
        ArrayList<ITask> unUseList = new ArrayList<ITask>();
        // 过滤一次
        for (ITask task : taskList) {
            if (task == null) {
                continue;
            }

            String packageName = DownloadExtraAgent.getPackageName(task);
            LogUtils.d(TAG, "id :" + task.getId() + " packageName : " + packageName);
            CheckParams localVersion = getVersionParamsByPackageName(entities, packageName);
            if (localVersion == null) {
                continue;
            }

            int taskVersionCode = task.getIntExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, 0);
            LogUtils.d(VersionConstants.TAG, "id :" + task.getId() + " taskVersionCode : " + taskVersionCode);

            if (taskVersionCode <= localVersion.getVersionCode()) {
                // 数据库里面记录的版本小于等于本地版本,就要删掉记录,和文件
                LogUtils.d(TAG, "删除过期版本下载文件和数据库记录ID:" + task.getId());
                unUseList.add(task);
                mDownLoadController.deleteTaskAndAllFile(task);
            }

            if (isIgnoreVersion(packageName, taskVersionCode)) {
                // 是否为忽视版本
                LogUtils.d(TAG, "删除无视版本和数据库记录ID:" + task.getId());
                unUseList.add(task);
                mDownLoadController.deleteTaskAndAllFile(task);
            }
        }

        return unUseList;
    }

    static CheckParams getVersionParamsByPackageName(List<CheckParams> entities, String packageName) {
        if (ListUtils.isEmpty(entities)) {
            return null;
        }

        for (CheckParams version : entities) {
            if (version == null) {
                continue;
            }
            if (TextUtils.equals(packageName, version.getPackageName())) {
                return version;
            }
        }
        return null;
    }

    void dealRemoteVersion(List<DataInfoEntity> data, List<CheckParams> entities) {
        List<Version> remoteVersions = new ArrayList<Version>();
        for (DataInfoEntity dataInfo : data) {
            if (dataInfo == null) {
                continue;
            }
            Version remoteVersion = new Version(dataInfo);
            CheckParams currentVersion = getVersionParamsByPackageName(entities, remoteVersion.getPackageName());
            LogUtils.d(TAG, "Check remote response version, package name:" + remoteVersion.getPackageName());

            if (currentVersion == null) {
                LogUtils.w(TAG, "currentVersion == null");
                continue;
            }

            int compare = remoteVersion.compareTo(new Version(currentVersion));
            if (compare == 0) {
                LogUtils.w(TAG, "服务器版本与本地版本相等, 不更新!!");
                continue;
            }
            if (compare != 1) {
                LogUtils.w(TAG, "服务器版本低于本地版本, 不更新!!");
                continue;
            }

            LogUtils.i(TAG, "服务器版本比本地版本高，可进行升级");
            if (!isIgnoreVersion(remoteVersion.getPackageName(), remoteVersion.getVersionCode())) {
                //需要下载的更新版本
                currentVersion = findApkPath(currentVersion);
                remoteVersion.setCurrentApkFileName(currentVersion.getCurrentApkFileName());
                remoteVersion.setCurrentApkFilePath(currentVersion.getCurrentApkFilePath());
                remoteVersion.setCurrentVersionCode(currentVersion.getVersionCode());
                remoteVersion.setCurrentVersionName(currentVersion.getVersionName());
                remoteVersion.setAlwaysFullUpgrade(mSettings.isAlwaysFullUpgrade());
                remoteVersions.add(remoteVersion);
            } else {
                LogUtils.w(TAG, "远程版本号＜＝忽略版本号，不更新！！");
            }
        }

        if (ListUtils.isEmpty(remoteVersions)) {
            LogUtils.w(TAG, "No remote response version need to download.");
            //无更新信息
            onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
            return;
        }
        downloadNewTask(remoteVersions);
    }

    /**
     * 如果同一个apk返回多个版本,只保留最高版本
     *
     * @param data
     * @return 过滤后需要删除的低版本更新信息列表
     */
    List<DataInfoEntity> filterLowVersion(List<DataInfoEntity> data) {
        List<DataInfoEntity> lowVersionData = new ArrayList<DataInfoEntity>();
        if (ListUtils.isEmpty(data)) {
            return lowVersionData;
        }
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                break;
            }
            DataInfoEntity curInfo = data.get(i);
            for (int j = i + 1; j < size; j++) {
                DataInfoEntity info = data.get(j);
                if (!TextUtils.equals(curInfo.getPackageName(), info.getPackageName())) {
                    continue;
                }
                //删除版本号低的更新数据
                lowVersionData.add(curInfo.getApkVersionCode() >= info.getApkVersionCode() ? info : curInfo);
            }
        }
        return lowVersionData;
    }

    private void downloadNewTask(List<Version> remoteVersions) {
        onNewVersionChecked(remoteVersions);

        if (bfcVersion != null && mSettings.isAutoDownload()) {
            bfcVersion.beginDownload(remoteVersions);
        } else {
            LogUtils.d(TAG, "不允许自动下载，应用手动控制添加下载任务!");
        }
    }

    /**
     * 查找本地是否有旧apk
     *
     * @param checkParams
     * @return
     */
    private CheckParams findApkPath(CheckParams checkParams) {
        if (checkParams == null) {
            return null;
        }

        if (!TextUtils.isEmpty(checkParams.getCurrentApkFilePath())
                && !TextUtils.isEmpty(checkParams.getCurrentApkFileName())) {
            //app有指定的apk路径,就不找了,源用app指定的
            return checkParams;
        }

        String name = checkParams.getPackageName() + VersionConstants.APK_SUFFIX;
        //查找全量下载保存路径下有没有
        String path = VersionConstants.PATH_B + VersionConstants.DOWNLOAD_APK_FOLDER + File.separator;
        if (checkApkFileExists(checkParams, path + name)) {
            checkParams.setCurrentApkFilePath(path);
            checkParams.setCurrentApkFileName(name);
            return checkParams;
        }

        //查找增量升级合并后apk保存路径下有没有
        path = VersionConstants.PATH_B + VersionConstants.NEW_APK_FOLDER + File.separator;
        if (checkApkFileExists(checkParams, path + name)) {
            checkParams.setCurrentApkFilePath(path);
            checkParams.setCurrentApkFileName(name);
            return checkParams;
        }

        return checkParams;
    }

    /**
     * 查看路径下对应版本的apk是否存在
     *
     * @param checkParams
     * @param path
     */
    private boolean checkApkFileExists(CheckParams checkParams, String path) {
        //默认文件名为包名.apk,必须包名,版本名,版本号都对应上
        return FileUtils.isFileExists(path)
                && TextUtils.equals(checkParams.getPackageName(), ApkUtils.getApkFilePackageName(mContext, path))
                && TextUtils.equals(checkParams.getVersionName(), ApkUtils.getApkFileVersionName(mContext, path))
                && checkParams.getVersionCode() == ApkUtils.getApkFileVersionCode(mContext, path);
    }

    /**
     * 回调上层-检查结束
     */
    void checkOver() {
        if (mOnCheckStrategyListener != null) {
            mOnCheckStrategyListener.checkOverStrategy();
        }
    }

    /**
     * 回调上层-检查异常
     *
     * @param errorCode
     * @param detail
     */
    void onVersionCheckException(String errorCode, String detail) {
        if (mOnCheckStrategyListener != null) {
            mOnCheckStrategyListener.onVersionCheckStrategyException(errorCode, detail);
        }
    }

    /**
     * 是否为忽视版本
     *
     * @param packageName
     * @param versionCode
     * @return
     */
    boolean isIgnoreVersion(String packageName, int versionCode) {
        return VersionUtil.getIgnoreVersionCode(mContext, packageName) >= versionCode;
    }

    /**
     * 回调上传-上报更新版本
     *
     * @param versions
     */
    private void onNewVersionChecked(List<Version> versions) {
        if (mOnCheckStrategyListener != null) {
            mOnCheckStrategyListener.onNewVersionCheckedStrategy(versions);
        }
    }

    /**
     * 回调上传-处理旧任务
     *
     * @param status
     * @param needCheckFile
     * @param task
     */
    private void dealOldTask(int status, boolean needCheckFile, ITask task) {
        if (mOnCheckStrategyListener != null) {
            mOnCheckStrategyListener.dealOldTaskStrategy(status, needCheckFile, task);
        }
    }
}
