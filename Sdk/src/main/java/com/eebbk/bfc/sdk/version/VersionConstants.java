package com.eebbk.bfc.sdk.version;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 */
public class VersionConstants {

    public static final boolean DEBUG = true;

    public static final String TAG = "[VersionManager]";

    /**存储路径相关**/

    /**
     * patch包文件后缀
     */
    public static final String PATCH_SUFFIX = ".patch";
    /**
     * apk文件后缀
     */
    public static final String APK_SUFFIX = ".apk";

    private static final String SMARTUPDATE_PATH = ".SmartUpdate";

    public static final String DOWNLOAD_APK_FOLDER = "apk";
    /**
     * 下载patch包文件夹
     */
    public static final String DOWNLOAD_PATCH_FOLDER = "patch";
    /**
     * 新合成apk文件夹
     */
    public static final String NEW_APK_FOLDER = "newApk";

    public static final String PATH_B = Environment.getExternalStorageDirectory() + File.separator + SMARTUPDATE_PATH
            + File.separator;

    //mark
//    public final static String PATH_SD = Environment.getExternalFlashStorageDirectory() + File.separator + SMARTUPDATE_PATH
//            + File.separator;
        public final static String PATH_SD = PATH_B;


    /**存储路径相关**/

    /**
     * 有增量包
     */
    public static final int PATCH_AVAILABLE = 1;
    /**
     * 没有增量包
     */
    public static final int PATCH_UNAVAILABLE = 0;

    /**
     * 合并差分包算法-百度
     * <br> 接口定义,勿改
     */
    public static final int PATCH_MERGE_TYPE_BAIDU = 1;

    /**
     * 合并差分包算法-豌豆荚
     * <br> 接口定义,勿改
     */
    public static final int PATCH_MERGE_TYPE_WANDOUJIA = 2;

    /**
     * 合并差分包算法-步步高
     * <br> 接口定义,勿改
     */
    public static final int PATCH_MERGE_TYPE_BBK = 3;

    /**
     * 版本更新检查策略-本地已有下载的版本优先
     * <br> 只有检查过,并有下载任务或者已经下载好的apk未安装,则即时远端有更新的版本,也不会网络请求获取.直接返回已有的任务.(省流量)
     */
    public static final int CHECK_STRATEGY_LOCAL_FIRST = 0;

    /**
     * 版本更新检查策略-服务器远端版本为准
     * <br> 每次请求都会去服务器端获取最新版本,即使本地已有任务,只要服务器有更新的,就抛弃本地已有的,下载更新的版本.
     */
    public static final int CHECK_STRATEGY_REMOTE_FIRST = 1;

    /**
     * 升级方法:普通提示升级
     */
    public static final int UPDATE_MODE_TIP = 2;

    /**
     * 升级方法:静默升级
     */
    public static final int UPDATE_MODE_SILENT = 3;

    /**
     * 默认升级方法
     */
    public static final int UPDATE_MODE_DEFAULT = UPDATE_MODE_TIP;

    private VersionConstants(){

    }
}