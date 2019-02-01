package com.eebbk.bfc.sdk.version.strategy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.entity.response.DataInfoEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.Settings;
import com.eebbk.bfc.sdk.version.download.DownLoadController;
import com.eebbk.bfc.sdk.version.download.DownloadConstants;
import com.eebbk.bfc.sdk.version.download.DownloadExtraAgent;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.error.ErrorCode;
import com.eebbk.bfc.sdk.version.listener.OnCheckStrategyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function 版本更新检查策略-服务器远端版本为准
 * <br> 每次请求都会去服务器端获取最新版本,即使本地已有任务,只要服务器有更新的,就抛弃本地已有的,下载更新的版本.
 * @date 17-2-15
 * @company 步步高教育电子有限公司
 */

public class RemoteCheckStrategyImpl extends ACheckStrategy {

    private static final String TAG = "RemoteCheckStrategyImpl";

    public RemoteCheckStrategyImpl(@NonNull Context context, @NonNull BfcVersion bfcVersion
            , @NonNull BfcCoreVersion bfcCoreVersion, @NonNull Settings settings
            , @NonNull DownLoadController downLoadController, @NonNull OnCheckStrategyListener l) {
        super(context.getApplicationContext(), bfcVersion, bfcCoreVersion, settings, downLoadController, l);
    }

    @Override
    public synchronized void check(@NonNull List<CheckParams> entities) {
        LogUtils.d(TAG, "on version remote strategy check run...");
        if (ListUtils.isEmpty(entities)) {
            LogUtils.d(TAG, "bfc version no new task");
            onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL, null);
            checkOver();
            return;
        }

        // 网络检查新任务
        startCheckVersion(entities);
    }

    /**
     * 检查远程访问返回版本
     *
     * @param respEntity
     */
    @Override
    public void remoteRespVerCheck(ResponseEntity respEntity, List<CheckParams> entities) {
        if (respEntity == null) {
            onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
            return;
        }

        List<DataInfoEntity> data = respEntity.getData();
        if (ListUtils.isEmpty(data)) {
            onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
            return;
        }

        //过滤低版本数据
        List<DataInfoEntity> lowVersionData = filterLowVersion(data);
        if (!ListUtils.isEmpty(lowVersionData)) {
            data.removeAll(lowVersionData);
        }

        // 先向数据库查询,看是否有任务
        ArrayList<ITask> taskList = mDownLoadController.getTaskList();
        if (taskList != null) {
            // 过滤过期的任务
            ArrayList<ITask> unUseList = filterUnUseTask(taskList, entities);
            if (unUseList.size() > 0) {
                taskList.removeAll(unUseList);
            }

            //网络请求版本和本地已有下载任务对比,输出 需要开启的旧任务 和 需要添加下载的新任务
            List<ITask> needDeleteList = filterTaskFromNet(data, taskList);
            if (!ListUtils.isEmpty(needDeleteList)) {
                // 删除比远端版本低的任务
                taskList.removeAll(needDeleteList);
                mDownLoadController.deleteTaskAndAllFile(needDeleteList.toArray(new ITask[needDeleteList.size()]));
            }

            // 需要重新开启的旧任务
            if (!ListUtils.isEmpty(taskList)) {
                // 已经有下载任务,并且是最新的
                // 有效的旧任务处理
                oldTaskCallBack(taskList);
                hasDownloadTask(taskList);
            }
        }

        // 需要添加下载的新任务
        if (!ListUtils.isEmpty(data)) {
            //需要添加新的下载任务
            dealRemoteVersion(data, entities);
        } else if (ListUtils.isEmpty(data) && ListUtils.isEmpty(taskList)) {
            // 没有新任务,也没有已经在下载的任务,则无更新信息
            onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
        }
    }

    /**
     * 根据网络返回的版本信息过滤旧的下载任务
     *
     * @param data
     * @param taskList
     * @return 返回需要删除的任务
     */
    private static List<ITask> filterTaskFromNet(List<DataInfoEntity> data, List<ITask> taskList) {
        //本次检查远程有比旧任务更新的,需要删除的任务列表
        List<ITask> needDeleteList = new ArrayList<ITask>();
        if (ListUtils.isEmpty(data) || ListUtils.isEmpty(taskList)) {
            return needDeleteList;
        }

        //本次没有检查到的旧任务,但是不需要删除的
        List<ITask> unCheckList = new ArrayList<ITask>();

        for (ITask task : taskList) {
            if (task == null) {
                continue;
            }

            String packageName = DownloadExtraAgent.getPackageName(task);
            int versionCode = task.getIntExtra(DownloadConstants.TASK_TAG_NEW_VERSION_CODE, 0);
            LogUtils.d(TAG, "id :" + task.getId() + " packageName : " + packageName);
            DataInfoEntity downingVersion = getDataInfoFromOldTask(data, packageName);

            if (downingVersion == null) {
                // 已有的旧任务,但是本次不需要开启或者检查
                unCheckList.add(task);
                continue;
            }

            // 有包名相匹配的下载旧任务
            if (versionCode >= downingVersion.getApkVersionCode()) {
                // 旧任务已经是最新版本
                LogUtils.d(TAG, "已有 id :" + task.getId() + " packageName : " + packageName + " 任务,无需重复下载.");
                data.remove(downingVersion);
            } else {
                // 远端有更新的版本
                LogUtils.d(TAG, "已有 id :" + task.getId() + " packageName : " + packageName + " 任务,远端有更新的版本,删除旧任务下载最新版本.");
                needDeleteList.add(task);
            }
        }

        taskList.removeAll(unCheckList);
        return needDeleteList;
    }

    /**
     * 检查旧任务中是否有包名相同的版本
     *
     * @param entities
     * @param packageName
     * @return 有则返回对应的网络信息类, 方便上传从list中删除
     */
    private static DataInfoEntity getDataInfoFromOldTask(List<DataInfoEntity> entities
            , String packageName) {
        if (ListUtils.isEmpty(entities)) {
            return null;
        }

        for (DataInfoEntity version : entities) {
            if (version == null) {
                continue;
            }
            //包名相同,并且已有下载任务版本大于等于远程版本
            if (TextUtils.equals(packageName, version.getPackageName())) {
                return version;
            }
        }
        return null;
    }
}
