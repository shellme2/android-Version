package com.eebbk.bfc.core.sdk.version.error;

import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hesn
 * @function 代理管理错误码信息
 * <br> 因为有核心库和业务库两个库不同的错误码,这个类是为了统一他们一起管理
 * @date 17-1-13
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeAgent {

    /**
     * 合并业务库和核心库错误码
     */
    private static final Map<String, String> mErrCodes = new HashMap<String, String>();

    public static void addErrCodes(Map<String, String> errCodes){
        if(errCodes == null || errCodes.isEmpty()){
            return;
        }

        //无需重复添加
        if(mErrCodes.size() >= CoreErrorCode.size() + errCodes.size()){
            return;
        }

        mErrCodes.putAll(errCodes);
    }

    public static String getErrorMsg(String errorCode) {
        if(TextUtils.isEmpty(errorCode)){
            return Constants.ERROR_MSG_UNKNOWN;
        }

        String msg = getErrCodes().get(errorCode);
        if(TextUtils.isEmpty(msg)){
            return Constants.ERROR_MSG_UNKNOWN;
        }
        return msg;
    }

    private static Map<String, String> getErrCodes(){
        if(mErrCodes.isEmpty()){
            mErrCodes.putAll(CoreErrorCode.getErrorCodes());
        }

        return mErrCodes;
    }
}
