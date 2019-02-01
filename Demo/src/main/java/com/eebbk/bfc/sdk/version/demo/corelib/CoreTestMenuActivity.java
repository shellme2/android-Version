package com.eebbk.bfc.sdk.version.demo.corelib;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ABaseActivity;
import com.eebbk.bfc.sdk.version.demo.corelib.function.CoreParameterActivity;

/**
 * 核心库菜单
 */
public class CoreTestMenuActivity extends ABaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_second_menu;
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

    public void onFunction(View v) {
        startActivity(new Intent(this, CoreParameterActivity.class));
    }

    public void onPerformance(View v) {
        Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
    }
}
