package com.eebbk.bfc.sdk.version.strategy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.entity.response.DataInfoEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.Settings;
import com.eebbk.bfc.sdk.version.download.DownLoadController;
import com.eebbk.bfc.sdk.version.download.DownloadExtraAgent;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.error.ErrorCode;
import com.eebbk.bfc.sdk.version.listener.OnCheckStrategyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function 版本更新检查策略-本地已有下载的版本优先
 * <br> 如果有检查过,并有下载任务或者已经下载好的apk未安装,则即时远端有更新的版本,也不会网络请求获取.直接返回已有的任务.(省流量)
 * @date 17-2-15
 * @company 步步高教育电子有限公司
 */

public class LocalCheckStrategyImpl extends ACheckStrategy {

    private static final String TAG = "LocalCheckStrategyImpl";

    public LocalCheckStrategyImpl(@NonNull Context context, @NonNull BfcVersion bfcVersion
            , @NonNull BfcCoreVersion bfcCoreVersion, @NonNull Settings settings
            , @NonNull DownLoadController downLoadController, @NonNull OnCheckStrategyListener l) {
        super(context.getApplicationContext(), bfcVersion, bfcCoreVersion, settings, downLoadController, l);
    }

    @Override
    public synchronized void check(@NonNull List<CheckParams> entities) {
        LogUtils.d(TAG, "on version local strategy check run...");
        if (ListUtils.isEmpty(entities)) {
            LogUtils.d(TAG, "bfc version no new task");
            onVersionCheckException(ErrorCode.SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL, null);
            checkOver();
            return;
        }

        // Step1:先向数据库查询,看是否有任务
        ArrayList<ITask> taskList = mDownLoadController.getTaskList();
        if (taskList != null) {
            // Step2:过滤过期的任务
            ArrayList<ITask> unUseList = filterUnUseTask(taskList, entities);
            if (unUseList.size() > 0) {
                taskList.removeAll(unUseList);
            }

            // Step3:过滤已经在下载的新任务
            filterLoadingTask(entities, taskList);

            // Step4:有效的旧任务处理
            oldTaskCallBack(taskList);
            hasDownloadTask(taskList);
        }

        if (ListUtils.isEmpty(entities)) {
            LogUtils.d(TAG, "bfc version no new task");
            if(ListUtils.isEmpty(taskList)){
                // 没有新任务,也没有已经在下载的任务,则无更新信息
                onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
            }
            checkOver();
            return;
        }

        // Step5:网络检查新任务
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

        // 需要添加下载的新任务
        if (!ListUtils.isEmpty(data)) {
            //需要添加新的下载任务
            dealRemoteVersion(data, entities);
        } else {
            //无更新信息
            onVersionCheckException(ErrorCode.SERVICE_CHECK_RESPONSE_NULL, "");
        }
    }

    /**
     * 过滤已经在下载的新任务
     *
     * @param entities
     * @param taskList
     */
    private static void filterLoadingTask(List<CheckParams> entities, List<ITask> taskList) {
        if (ListUtils.isEmpty(entities) || ListUtils.isEmpty(taskList)) {
            return;
        }

        List<ITask> unCheckList = new ArrayList<ITask>();

        for (ITask task : taskList) {
            if (task == null) {
                continue;
            }

            String packageName = DownloadExtraAgent.getPackageName(task);
            LogUtils.d(TAG, "id :" + task.getId() + " packageName : " + packageName);
            CheckParams downingVersion = getVersionParamsByPackageName(entities, packageName);
            if (downingVersion != null) {
                LogUtils.d(TAG, "已有 id :" + task.getId() + " packageName : " + packageName + " 任务,无需重复下载.");
                entities.remove(downingVersion);
            } else {
                unCheckList.add(task);
            }
        }

        taskList.removeAll(unCheckList);
    }
}
