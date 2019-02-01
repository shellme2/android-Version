package com.eebbk.bfc.sdk.version.demo.servicelib.config;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ABaseActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.common.ServiceConfigPresenter;

/**
 * 业务库菜单
 */
public class ServiceConfigActivity extends ABaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_service_config;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new ServiceConfigPresenter(this, null);
    }

    @Override
    protected boolean isShowLog() {
        return false;
    }
}
