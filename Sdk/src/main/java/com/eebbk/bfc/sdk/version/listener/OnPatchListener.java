package com.eebbk.bfc.sdk.version.listener;

import com.eebbk.bfc.sdk.version.patch.PatchParam;

/**
 * 差分合并回调
 * Created by lcg on 16-4-22.
 */
public interface OnPatchListener {

    /**
     * 差分合并成功之后回调
     * @param patchParam Patch所需参数
     */
    void patchSuccess(PatchParam patchParam);

    /**
     * 差分合并失败之后回调
     * @param patchParam Patch所需参数
     */
    void patchFailed(PatchParam patchParam, int result);
}
