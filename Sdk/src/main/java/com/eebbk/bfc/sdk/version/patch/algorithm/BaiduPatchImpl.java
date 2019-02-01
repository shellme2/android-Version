package com.eebbk.bfc.sdk.version.patch.algorithm;

import android.text.TextUtils;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.patch.algorithm.baidu.GDiffPatcher;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author hesn
 * @function 百度差分包合并
 * @date 17-1-12
 * @company 步步高教育电子有限公司
 */

public class BaiduPatchImpl implements IPatch {

    private static final String TAG = "BaiduPatchImpl";

    @Override
    public int patch(String source, String patch, String output) {
        LogUtils.d(TAG, "Baidu patch run...");
        String unGzipPatch = unGzip(patch);
        int patchResult = 0;
        try {
            FileUtils.createFileByDeleteOld(output);
            LogUtils.d(TAG, "unGzipPatch:" + FileUtils.isFileExists(unGzipPatch));
            GDiffPatcher gDiffPatcher = new GDiffPatcher();
            gDiffPatcher.patch(new File(source), new File(unGzipPatch), new File(output));
        } catch (Exception e) {
            e.printStackTrace();
            patchResult = -1;
        }
        return patchResult;
    }

    private String unGzip(String patch) {
        if (TextUtils.isEmpty(patch)) {
            LogUtils.d(TAG, "patch == null");
            return patch;
        }

        if (!isGzip(patch)) {
            //不是gzip无需解压
            LogUtils.d(TAG, "非gzip,无需解压．");
            return patch;
        }

        int lastPIndex = patch.lastIndexOf(".") - 1;
        String patchUnGzip = TextUtils.concat(
                patch.substring(0, lastPIndex),
                "_gzip_decompress",
                VersionConstants.PATCH_SUFFIX
        ).toString();
        InputStream patchIS = null;
        OutputStream patchOS = null;
        try {
            FileUtils.createFileOrExists(patchUnGzip);
            patchIS = new FileInputStream(patch);
            patchOS = new FileOutputStream(patchUnGzip);
            decompress(patchIS, patchOS);
        } catch (IOException e) {
            e.printStackTrace();
            patchUnGzip = null;
        } finally {
            FileUtils.closeIO(patchIS);
            FileUtils.closeIO(patchOS);
        }
        return patchUnGzip;
    }

    private boolean isGzip(String patchStr) {
        InputStream patch = null;
        DataInputStream patchIS = null;
        try {
            patch = new FileInputStream(patchStr);
            patchIS = new DataInputStream(new BufferedInputStream(patch, GDiffPatcher.QUARTER_MB));
            // gzip 固定开头 1f8b 0800 0000 0000
            if (patchIS.readUnsignedByte() == 0x1f && patchIS.readUnsignedByte() == 0x8b
                    && patchIS.readUnsignedByte() == 0x08 && patchIS.readUnsignedByte() == 0x00
                    && patchIS.readUnsignedByte() == 0x00 && patchIS.readUnsignedByte() == 0x00
                    && patchIS.readUnsignedByte() == 0x00 && patchIS.readUnsignedByte() == 0x00) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(patchIS);
            FileUtils.closeIO(patch);
        }
        return false;
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     */
    private void decompress(InputStream is, OutputStream os) {
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(is);
            int buffer = 1024;
            int count;
            byte data[] = new byte[buffer];
            while ((count = gis.read(data, 0, buffer)) != -1) {
                os.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtils.closeIO(gis);
        }
    }
}
