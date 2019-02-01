package com.eebbk.bfc.sdk.version.demo.servicelib.common;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.servicelib.config.ServoceConfig;

/**
 * @author hesn
 * @function 基础配置管理
 * @date 16-12-6
 * @company 步步高教育电子有限公司
 */

public class ServiceConfigPresenter implements CompoundButton.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener {

    private CheckBox mAutoDownloadCb;
    private CheckBox mAlwayFullUpgradeCb;
    private CheckBox mAutoReloadDownloadFailedTaskCb;
    private CheckBox mAutoReloadCheckFailedTaskCb;
    private CheckBox mDebugModeCb;
    private RadioGroup mCheckStrategyRG;
    private final OnServiceConfigLintener l;

    public ServiceConfigPresenter(Activity activity, OnServiceConfigLintener l) {
        initView(activity);
        initData();
        this.l = l;
    }

    private void initData() {
        setAutoDownload(ServoceConfig.isAutoDownload());
        setAlwayFullUpgrade(ServoceConfig.isAlwayFullUpgrade());
        setAutoReloadDownloadFailedTask(ServoceConfig.isAutoReloadDownloadFailedTask());
        setAutoReloadCheckFailedTask(ServoceConfig.isAutoReloadCheckFailedTask());
        setDebugMode(ServoceConfig.isDebugMode());
        mCheckStrategyRG.check(ServoceConfig.getCheckStrategy() == VersionConstants.CHECK_STRATEGY_LOCAL_FIRST
                ? R.id.localRBtn : R.id.remoteRBtn);
    }

    /**
     * 检查有更新后自动下载
     *
     * @return
     */
    public boolean isAutoDownload() {
        return mAutoDownloadCb.isChecked();
    }

    /**
     * 检查有更新后自动下载
     *
     * @param enableAutoDownload
     */
    public void setAutoDownload(boolean enableAutoDownload) {
        mAutoDownloadCb.setChecked(enableAutoDownload);
    }

    /**
     * 是否总是全量升级
     *
     * @return
     */
    public boolean isAlwayFullUpgrade() {
        return mAlwayFullUpgradeCb.isChecked();
    }

    /**
     * 是否总是全量升级
     *
     * @param alwayFullUpgrade
     */
    public void setAlwayFullUpgrade(boolean alwayFullUpgrade) {
        mAlwayFullUpgradeCb.setChecked(alwayFullUpgrade);
    }

    /**
     * 自动重新下载 下载失败的任务
     *
     * @return
     */
    public boolean isAutoReloadDownloadFailedTask() {
        return mAutoReloadDownloadFailedTaskCb.isChecked();
    }

    /**
     * 自动重新下载 下载失败的任务
     *
     * @param autoReloadDownloadFailedTask
     */
    public void setAutoReloadDownloadFailedTask(boolean autoReloadDownloadFailedTask) {
        mAutoReloadDownloadFailedTaskCb.setChecked(autoReloadDownloadFailedTask);
    }

    /**
     * 自动重新下载 校验失败的任务
     *
     * @return
     */
    public boolean isAutoReloadCheckFailedTask() {
        return mAutoReloadCheckFailedTaskCb.isChecked();
    }

    /**
     * 自动重新下载 校验失败的任务
     *
     * @param autoReloadCheckFailedTask
     */
    public void setAutoReloadCheckFailedTask(boolean autoReloadCheckFailedTask) {
        mAutoReloadCheckFailedTaskCb.setChecked(autoReloadCheckFailedTask);
    }

    /**
     * 是否为调试模式
     *
     * @return
     */
    public boolean isDebugMode() {
        return mDebugModeCb.isChecked();
    }

    /**
     * 设置是否为调试模式
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {
        mDebugModeCb.setChecked(debugMode);
    }

    private void initView(Activity activity) {
        mAutoDownloadCb = (CheckBox) activity.findViewById(R.id.autoDownloadCb);
        mAlwayFullUpgradeCb = (CheckBox) activity.findViewById(R.id.alwayFullUpgradeCb);
        mAutoReloadDownloadFailedTaskCb = (CheckBox) activity.findViewById(R.id.autoReloadDownloadFailedTaskCb);
        mAutoReloadCheckFailedTaskCb = (CheckBox) activity.findViewById(R.id.autoReloadCheckFailedTaskCb);
        mDebugModeCb = (CheckBox) activity.findViewById(R.id.debugModeCb);
        mCheckStrategyRG = (RadioGroup) activity.findViewById(R.id.checkStrategyRG);

        mAutoDownloadCb.setOnCheckedChangeListener(this);
        mAlwayFullUpgradeCb.setOnCheckedChangeListener(this);
        mAutoReloadDownloadFailedTaskCb.setOnCheckedChangeListener(this);
        mAutoReloadCheckFailedTaskCb.setOnCheckedChangeListener(this);
        mDebugModeCb.setOnCheckedChangeListener(this);
        mCheckStrategyRG.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        switch(buttonView.getId()){
            case R.id.autoDownloadCb:{
                ServoceConfig.setAutoDownload(isChecked);
                if(l != null){
                    l.onAutoDownloadChange(isChecked);
                }
            }
            break;
            case R.id.alwayFullUpgradeCb:{
                ServoceConfig.setAlwayFullUpgrade(isChecked);
                if(l != null){
                    l.onAlwayFullUpgradeChange(isChecked);
                }
            }
            break;
            case R.id.autoReloadDownloadFailedTaskCb:{
                ServoceConfig.setAutoReloadDownloadFailedTask(isChecked);
                if(l != null){
                    l.onAutoReloadDownloadFailedTaskChange(isChecked);
                }
            }
            break;
            case R.id.autoReloadCheckFailedTaskCb:{
                ServoceConfig.setAutoReloadCheckFailedTask(isChecked);
                if(l != null){
                    l.onAutoReloadCheckFailedTaskChange(isChecked);
                }
            }
            break;
            case R.id.debugModeCb:{
                ServoceConfig.setDebugMode(isChecked);
                if(l != null){
                    l.onDebugModeCbChange(isChecked);
                }
            }
            break;
            default:{

            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.localRBtn:
                ServoceConfig.setCheckStrategy(VersionConstants.CHECK_STRATEGY_LOCAL_FIRST);
                break;
            case R.id.remoteRBtn:
                ServoceConfig.setCheckStrategy(VersionConstants.CHECK_STRATEGY_REMOTE_FIRST);
                break;
            default:
                break;
        }
    }
}
