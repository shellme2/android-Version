package com.eebbk.bfc.core.sdk.version;

import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;

/**
 * 版本检测 监听
 */
public interface OnCoreVerCheckListener {
    /**
     * 返回服务器结果
     *
     * @param respSrc       返回信息源
     * @param respEntity    返回信息
     */
    void onResponse(String respSrc, ResponseEntity respEntity);

    /**
     * 访问异常回调
     *
     * @param error
     */
    void onError(BfcHttpError error);
}
