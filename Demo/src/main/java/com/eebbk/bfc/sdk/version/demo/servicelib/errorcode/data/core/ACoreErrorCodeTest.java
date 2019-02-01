package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.OnCoreVerCheckListener;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.AErrorCodeTest;

/**
 * @author hesn
 * @function 核心库错误码测试基类
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

abstract class ACoreErrorCodeTest extends AErrorCodeTest {

    protected BfcCoreVersion bfcVersion;

    @Override
    public void destroy() {
        super.destroy();
        if(bfcVersion != null){
            bfcVersion.cancelAll();
        }
        bfcVersion = null;
    }

    /**
     * 默认简单初始化
     *
     * @param context
     */
    @Override
    protected boolean initDefault(Context context, OnCheckErrorCodeListener l) {
        if (context == null || l == null) {
            return false;
        }
        if (bfcVersion != null) {
            return true;
        }
        bfcVersion = new BfcCoreVersion.Builder().build(context);
        setOnCheckErrorCodeListener(l);
        return true;
    }

    protected OnCoreVerCheckListener onCoreVerCheckListener = new OnCoreVerCheckListener() {
        @Override
        public void onResponse(String respSrc, ResponseEntity respEntity) {

        }

        @Override
        public void onError(BfcHttpError error) {
            if (l != null && error != null) {
                l.checkResult(getErrorCode(), error.getMsg(), check(error.getErrorCode()));
            }
        }
    };

}
