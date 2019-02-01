package com.eebbk.bfc.sdk.version.listener;

import com.eebbk.bfc.sdk.downloadmanager.ITask;
import com.eebbk.bfc.sdk.version.entity.Version;

import java.util.List;

/**
 * @author hesn
 * @function 连接接口层和检查策略层(内部调用接口)
 * @date 17-2-15
 * @company 步步高教育电子有限公司
 */

public interface OnCheckStrategyListener {

    void checkOverStrategy();

    void onVersionCheckStrategyException(String errorCode, String detail);

    void onNewVersionCheckedStrategy(List<Version> versions);

    void dealOldTaskStrategy(int status, boolean needCheckFile, ITask task);
}
