package com.eebbk.bfc.sdk.version.download;

/**
 * @author hesn
 * @function
 * @date 17-3-29
 * @company 步步高教育电子有限公司
 */

public class DownloadConstants {

    /**
     * 总是全量升级
     */
    public static final String TASK_TAG_ALWAYS_FULL_UPGRADE = "alwaysFullUpgrade";
    /**
     * 应用包名
     */
    public static final String TASK_TAG_PACKAGE = "packageName";
    /**
     * apk文件名
     */
    public static final String TASK_TAG_APK_FILE_NAME = "apkFileName";
    /**
     * 差分包文件名
     */
    public static final String TASK_TAG_PATCH_FILE_NAME = "patchFileName";
    /**
     * 当前版本Apk文件名字
     * <br> 上报APP升级信息需要用到
     */
    public static final String TASK_TAG_CURRENT_VERSION_CODE = "currentVersionCode";
    /**
     * 当前版本Apk文件路径
     * <br> 上报APP升级信息需要用到
     */
    public static final String TASK_TAG_CURRENT_VERSION_NAME = "currentVersionName";

    /**
     * 当前版本Apk文件名字
     * <br> 差分包合并需要,如果没有则全量升级
     */
    public static final String TASK_TAG_CURRENT_APK_FILE_NAME = "currentApkFileName";
    /**
     * 当前版本Apk文件路径
     * <br> 差分包合并需要,如果没有则全量升级
     */
    public static final String TASK_TAG_CURRENT_APK_FILE_PATH = "currentApkFilePath";

    /**
     * 差分包合并算法类型
     */
    public static final String TASK_TAG_PATCH_MERGE_TYPE = "patchMergeType";

    /**
     * 升级方法
     */
    public static final String TASK_TAG_UPDATE_MODE = "updateMode";

    public static final String TASK_TAG_FUNCTION = "functionName";
    /**
     * 应用名
     */
    public static final String TASK_TAG_MODULE = "moduleName";
    /**
     * 新版本号
     */
    public static final String TASK_TAG_NEW_VERSION_CODE = "newVersionCode";
    /**
     * 新版本名
     */
    public static final String TASK_TAG_NEW_VERSION_NAME = "newVersionName";
    /**
     * 服务器文件资源id，上报需要回传
     */
    public static final String TASK_TAG_SERVER_FILE_ID = "serverFileId";
    /**
     * 升级描述信息
     */
    public static final String TASK_TAG_UPDATE_INFORMATION = "updateInformation";
    /**
     * 是否有增量包
     */
    public static final String TASK_TAG_PATCH_AVAILABLE = "patchAvailable";
    /**
     * 增量包md5
     */
    public static final String TASK_TAG_PATCH_MD5 = "patchMd5";
    /**
     * 全量包下载地址
     */
    public static final String TASK_TAG_APK_DOWNLOAD_URL = "apkUrl";
    /**
     * 全量包md5
     */
    public static final String TASK_TAG_APK_MD5 = "apkMd5";
    /**
     * 文件大小
     */
    public static final String TASK_TAG_FILE_SIZE = "fileSize";
    /**
     * 差分包文件大小
     */
    public static final String TASK_TAG_PATCH_SIZE = "patchSize";
    /**
     * Apk/patch 描述信息
     */
    public static final String TASK_TAG_APK_INTRODUCE = "apkIntroduce";
    /**
     * 版本提示信息
     */
    public static final String TASK_TAG_TIP = "tip";
}
