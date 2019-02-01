package com.eebbk.bfc.sdk.version.patch.algorithm;

/**
 * @author hesn
 * @function 差分包合并接口
 * @date 17-1-12
 * @company 步步高教育电子有限公司
 */

public interface IPatch {

    /**
     * 差分包合并
     *
     * @param source    原.apk文件路径
     * @param patch     差分包文件.patch路径
     * @param output    输出.apk文件路径
     * @return
     */
    int patch(String source, String patch, String output);

}
