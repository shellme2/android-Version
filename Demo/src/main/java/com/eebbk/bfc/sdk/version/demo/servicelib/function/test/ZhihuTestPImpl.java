package com.eebbk.bfc.sdk.version.demo.servicelib.function.test;

import android.content.Context;

/**
 * @author hesn
 * @function 知乎测试数据
 * @date 16-12-29
 * @company 步步高教育电子有限公司
 */

public class ZhihuTestPImpl extends AAutoGetTestParameter {

    public ZhihuTestPImpl(Context context) {
        super(context);
    }

    @Override
    public String getPackageName() {
        return "com.zhihu.android";
    }

    /**
     * 文件名
     * <br> 测试时候需要将同名apk拷贝到 getCurrentApkFilePath() 路径下,用于差分包测试
     *
     * @return
     */
    @Override
    public String getCurrentApkFileName() {
        return "zhihu_3.2.0_307.apk";
    }
}