package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service;

import android.content.Context;

import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.AErrorCodeTest;
import com.eebbk.bfc.sdk.version.entity.Version;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.listener.OnVersionCheckListener;

import java.util.List;

/**
 * @author hesn
 * @function 业务库错误码测试基类
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

abstract class AServiceErrorCodeTest extends AErrorCodeTest {

    protected BfcVersion bfcVersion;

    @Override
    public void destroy() {
        super.destroy();
        if(bfcVersion != null){
            bfcVersion.destroy();
        }
        bfcVersion = null;
    }

    protected void setOnVersionCheckListener() {
        if (bfcVersion == null) {
            return;
        }
        bfcVersion.setOnVersionCheckListener(onVersionCheckListener);
    }

    /**
     * 默认简单初始化
     *
     * @param context
     */
    @Override
    protected boolean initDefault(Context context, OnCheckErrorCodeListener l) {
        isCheckOver = false;
        if (context == null || l == null) {
            return false;
        }
        if (bfcVersion != null) {
            return true;
        }
        bfcVersion = new BfcVersion.Builder()
                .setDebugMode(true)
                .setCacheLog(true)
                .build(context);
        setOnCheckErrorCodeListener(l);
        setOnVersionCheckListener();
        return true;
    }

    private OnVersionCheckListener onVersionCheckListener = new OnVersionCheckListener() {
        @Override
        public void onUpdateReady(Context context, VersionInfo info) {

        }

        @Override
        public void onNewVersionChecked(List<Version> newVersions) {

        }

        @Override
        public void onVersionCheckException(String errorCode) {
            if (l != null) {
                isCheckOver = true;
                l.checkResult(getErrorCode(), getErrorMsg(), check(errorCode));
            }
        }

        @Override
        public void onCheckOver() {
            if (!isCheckOver && l != null) {
                l.checkResult(getErrorCode(), getErrorMsg(), false);
            }
        }
    };

}
