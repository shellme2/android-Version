package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode;

/**
 * @author hesn
 * @function
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public interface OnCheckErrorCodeListener {

    /**
     * 错误码检查结果监听
     *
     * @param errorCode 错误码
     * @param errorMsg
     * @param isOK 是否和预期一样
     *
     */
    void checkResult(String errorCode, String errorMsg, boolean isOK);
}
