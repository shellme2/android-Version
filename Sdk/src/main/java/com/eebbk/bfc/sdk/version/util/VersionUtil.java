/*
 * 文 件 名:  VersionUtil.java
 * 版    权:  广东小天才科技有限公司
 * 描    述:  <文件主要内容描述>
 * 作    者:  lcg
 * 创建时间:  2016-6-16 上午11:06:10
 */
package com.eebbk.bfc.sdk.version.util;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;

public class VersionUtil {

    public static void setIgnoreVersionCode(Context context, String packageName, int versionCode) {
        if (context == null) {
            LogUtils.w("Set ignore version error, context == null, packageName =" + packageName + " versionCode =" + versionCode);
            return;
        }
        LogUtils.d("ignore packageName =" + packageName + " versionCode =" + versionCode);
        XmlDBUtil.put(context, packageName, versionCode);
    }

    public static int getIgnoreVersionCode(Context context, String packageName) {
        if (context == null) {
            LogUtils.w("get ignore version error, context == null");
            return -1;
        }
        return XmlDBUtil.get(context, packageName, -1);
    }

    private VersionUtil() {

    }
}
