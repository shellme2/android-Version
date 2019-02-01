package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
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

public class ErrorCodeTest0D010304Impl extends ACoreErrorCodeTest {

    @Override
    public String getErrorCode() {
        return CoreErrorCode.CORE_CHECK_URL_NULL;
    }

    @Override
    public void run(Context context, OnCheckErrorCodeListener l) {
        if (!initDefault(context, l)) {
            return;
        }
        List<RequestVersionEntity> list = new ArrayList<RequestVersionEntity>();
        list.add(new RequestVersionEntity());
        bfcVersion.checkVersion(list, null, onCoreVerCheckListener);
    }
}
