package com.eebbk.bfc.sdk.version.demo.servicelib.common;

/**
 * @author hesn
 * @function 配置修改监听
 * @date 16-12-6
 * @company 步步高教育电子有限公司
 */

public interface OnServiceConfigLintener {

    void onAutoDownloadChange(boolean enable);

    void onAlwayFullUpgradeChange(boolean enable);

    void onMultipleRequestsChange(boolean enable);

    void onAutoReloadDownloadFailedTaskChange(boolean enable);

    void onAutoReloadCheckFailedTaskChange(boolean enable);

    void onDebugModeCbChange(boolean enable);
}
