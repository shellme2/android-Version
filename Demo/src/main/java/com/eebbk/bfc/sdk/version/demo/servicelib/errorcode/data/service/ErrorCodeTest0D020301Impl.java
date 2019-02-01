package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service;

import android.content.Context;

import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;
import com.eebbk.bfc.sdk.version.entity.CheckParams;
import com.eebbk.bfc.sdk.version.error.ErrorCode;

import java.util.ArrayList;

/**
 * @author hesn
 * @function
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeTest0D020301Impl extends AServiceErrorCodeTest {

    @Override
    public String getErrorCode() {
        return ErrorCode.SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL;
    }

    @Override
    public void run(Context context, OnCheckErrorCodeListener l) {
        if (!initDefault(context, l)) {
            return;
        }
        bfcVersion.checkVersion(new ArrayList<CheckParams>());
    }
}
