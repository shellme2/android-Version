package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service;

import android.content.Context;

import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;
import com.eebbk.bfc.sdk.version.entity.VersionInfo;
import com.eebbk.bfc.sdk.version.error.ErrorCode;

import java.io.File;

/**
 * @author hesn
 * @function
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeTest0D020308Impl extends AServiceErrorCodeTest {

    @Override
    public String getErrorCode() {
        return ErrorCode.SERVICE_INSTALL_FAILED_NOT_EXISTS;
    }

    @Override
    public void run(Context context, OnCheckErrorCodeListener l) {
        if (!initDefault(context, l)) {
            return;
        }
        bfcVersion.installApk(context, new VersionInfo(context, 0, "remoteVersionName", 0,
                "information", new File(""), 0, "curVersionName",
                "md5", "packageName", 0));
    }
}
