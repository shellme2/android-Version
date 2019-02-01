package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.error.ErrorCodeAgent;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;

import java.io.File;

/**
 * @author hesn
 * @function 错误码测试基类
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public abstract class AErrorCodeTest implements IErrorCodeTest {

    protected OnCheckErrorCodeListener l;
    protected boolean isCheckOver = false;

    /**
     * 默认简单初始化
     *
     * @param context
     */
    protected abstract boolean initDefault(Context context, OnCheckErrorCodeListener l);

    @Override
    public String getErrorMsg() {
        return ErrorCodeAgent.getErrorMsg(getErrorCode());
    }

    @Override
    public void destroy() {
        l = null;
    }

    @Override
    public boolean isDestroyed() {
        return l == null;
    }

    /**
     * 错误码测试文件保存路径
     *
     * @return
     */
    protected String getTestPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "bfcversion_error_code_test" + File.separator;
    }

    protected void setOnCheckErrorCodeListener(OnCheckErrorCodeListener onCheckErrorCodeListener) {
        l = onCheckErrorCodeListener;
    }

    /**
     * 检查错误码是否和预期一致
     *
     * @param errorCode
     * @return
     */
    protected boolean check(String errorCode) {
        return TextUtils.equals(errorCode, getErrorCode());
    }

}
