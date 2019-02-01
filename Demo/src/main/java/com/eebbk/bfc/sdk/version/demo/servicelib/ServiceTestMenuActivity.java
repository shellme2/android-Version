package com.eebbk.bfc.sdk.version.demo.servicelib;

import android.content.Intent;
import android.view.View;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ABaseActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.config.ServiceConfigActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.ErrorCodeActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.ServiceParameterActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.performance.loop.ServiceLoopActivity;

/**
 * 业务库菜单
 */
public class ServiceTestMenuActivity extends ABaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_service_second_menu;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected boolean isShowLog() {
        return false;
    }

    /**
     * 功能测试
     *
     * @param v
     */
    public void onFunction(View v) {
        startActivity(new Intent(this, ServiceParameterActivity.class));
    }

    /**
     * 性能测试
     *
     * @param v
     */
    public void onPerformance(View v) {
        startActivity(new Intent(this, ServiceLoopActivity.class));
    }

    /**
     * 错误码测试
     *
     * @param v
     */
    public void onErrorCode(View v) {
        startActivity(new Intent(this, ErrorCodeActivity.class));
    }

    /**
     * 配置
     *
     * @param v
     */
    public void onConfig(View v) {
        startActivity(new Intent(this, ServiceConfigActivity.class));
    }
}
