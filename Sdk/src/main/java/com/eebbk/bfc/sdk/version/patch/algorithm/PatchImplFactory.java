package com.eebbk.bfc.sdk.version.patch.algorithm;

import com.eebbk.bfc.sdk.version.VersionConstants;

/**
 * @author hesn
 * @function
 * @date 17-1-12
 * @company 步步高教育电子有限公司
 */

public class PatchImplFactory {

    public static IPatch create(int mergeType) {
        switch (mergeType) {
            case VersionConstants.PATCH_MERGE_TYPE_BAIDU:
                // 百度差分包算法
                return new BaiduPatchImpl();
            case VersionConstants.PATCH_MERGE_TYPE_BBK:
                // 步步高差分包算法
                return new BBKPatchImpl();
            default:
                return null;
        }
    }

    /**
     * 是否有此差分算法
     * <br> 目前只有百度的有
     *
     * @param mergeType
     * @return
     */
    public static boolean hasPatch(int mergeType) {
        return mergeType == VersionConstants.PATCH_MERGE_TYPE_BAIDU
                || mergeType == VersionConstants.PATCH_MERGE_TYPE_BBK;
    }
}
