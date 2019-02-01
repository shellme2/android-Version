package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.error.CoreErrorCode;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeTest0D010401Impl extends ACoreErrorCodeTest {

    @Override
    public String getErrorCode() {
        return CoreErrorCode.CORE_CHECK_NET_EXCEPTION;
    }

    @Override
    public void run(Context context, OnCheckErrorCodeListener l) {
        if (!initDefault(context, l)) {
            return;
        }
        List<RequestReportEntity> list = new ArrayList<RequestReportEntity>();
        list.add(new RequestReportEntity());
        bfcVersion.reportUpdate(list, onCoreVerCheckListener);
    }

    @Override
    protected boolean initDefault(Context context, OnCheckErrorCodeListener l) {
        if (context == null || l == null) {
            return false;
        }
        if (bfcVersion != null) {
            return true;
        }
        bfcVersion = new BfcCoreVersion.Builder()
                .setRequestNetwork(0)
                .build(context);
        setOnCheckErrorCodeListener(l);
        return true;
    }
}
