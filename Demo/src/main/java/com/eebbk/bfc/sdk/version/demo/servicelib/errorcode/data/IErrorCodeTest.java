package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data;

import android.content.Context;

import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.OnCheckErrorCodeListener;

import javax.security.auth.Destroyable;

/**
 * @author hesn
 * @function 错误码覆盖测试接口
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public interface IErrorCodeTest extends Destroyable{

    /**
     * 错误码
     *
     * @return
     */
    String getErrorCode();

    /**
     * 错误信息
     *
     * @return
     */
    String getErrorMsg();

    /**
     * 执行测试代码
     *
     * @param context
     * @param l
     */
    void run(Context context, OnCheckErrorCodeListener l);
}
