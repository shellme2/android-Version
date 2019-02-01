package com.eebbk.bfc.core.sdk.version.error;

import android.support.annotation.NonNull;

import com.eebbk.bfc.http.error.BfcHttpError;

/**
 * @author hesn
 * @function
 * @date 17-3-3
 * @company 步步高教育电子有限公司
 */

public class BfcVersionHttpError extends BfcHttpError{

    private String errorCode;
    private String msg;

    public BfcVersionHttpError(@NonNull String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.msg = ErrorCodeAgent.getErrorMsg(errorCode);
    }

    public String getMsg() {
        return msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "BfcVersionHttpError{" +
                "errorCode='" + errorCode + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
