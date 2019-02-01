package com.eebbk.bfc.sdk.version.patch.algorithm;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.versionmanager.util.PatchUtils;

/**
 * @author hesn
 * @function 步步高自家增量升级算法(bsdiff)
 * @date 17-1-12
 * @company 步步高教育电子有限公司
 */

public class BBKPatchImpl implements IPatch{

    private static final String TAG = "BBKPatchImpl";

    @Override
    public int patch(String source, String patch, String output) {
        LogUtils.d(TAG, "BBK patch run...");
        int patchResult;
        FileUtils.createFileByDeleteOld(output);
        try {
            patchResult = PatchUtils.patch(source, output, patch);
        } catch (Exception e) {
            e.printStackTrace();
            patchResult = -1;
        }
        return patchResult;
    }
}
