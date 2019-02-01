package com.eebbk.bfc.sdk.version.error;

import com.eebbk.bfc.core.sdk.version.error.CoreErrorCode;

import java.util.Map;

/**
 * @author hesn
 * @function
 * @date 16-11-28
 * @company 步步高教育电子有限公司
 */

public class ErrorCode extends CoreErrorCode {

    /**
     * 升级方式与预期不一致
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：操作规范
     */
    public static final String SERVICE_CHECK_INSTALL_UPDATE_MODE = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + OPERATION_EXCEPTION + "01";

    /**
     * 检查版本更新失败,请求的数据 List<CheckParams> 不能为空
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "01";

    /**
     * 上报APP升级信息 List<RequestReportEntity> 不能为空
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_REPORT_ENTITY_LIST_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "02";

    /**
     * beginDownload()参数不能为空
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_BEGIN_DOWNLOAD_PARAMS_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "03";

    /**
     * 安装失败, 无法安装.patch文件.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_CANNOT_INSTALL_PATCH_FILE = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "04";

    /**
     * 安装失败, 安装文件MD5值不匹配
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_INSTALL_FAILED_MD5 = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "05";

    /**
     * 安装失败, 安装文件信息不匹配
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_INSTALL_FAILED_INFO = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "06";

    /**
     * 安装失败, context == null
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_INSTALL_FAILED_CONTEXT_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "07";

    /**
     * 安装失败, 安装文件不存在
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：参数异常
     */
    public static final String SERVICE_INSTALL_FAILED_NOT_EXISTS = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + ILLEGAL_ARGUMENT_EXCEPTION + "08";

    /**
     * 网络请求异常
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：网络异常
     */
    public static final String SERVICE_NET_WORK_EXCEPTION = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NET_EXCEPTION + "01";

    /**
     * 已下载数据已无效,请从新检查更新下载
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：数据处理异常
     */
    public static final String TASK_DATA_EXCEPTION = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + DATA_EXCEPTION + "01";

    /**
     * 无更新信息
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：空指针
     */
    public static final String SERVICE_CHECK_RESPONSE_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NULL_POINTER_EXCEPTION + "01";

    /**
     * 安装失败, VersionInfo 为空.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：空指针
     */
    public static final String SERVICE_INSTALL_VERSION_INFO_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NULL_POINTER_EXCEPTION + "02";

    /**
     * 安装失败, 找不到安装文件.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：空指针
     */
    public static final String SERVICE_INSTALL_APK_FILE_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NULL_POINTER_EXCEPTION + "03";

    /**
     * 检查策略为空,没有初始化或者已经调用了destroy()需要重新初始化.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：空指针
     */
    public static final String SERVICE_CHECK_CHECK_STRATEGY_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NULL_POINTER_EXCEPTION + "04";

    /**
     * 无法合并差分包,DownloadInfo为null.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：空指针
     */
    public static final String SERVICE_CHECK_DOWNLOAD_INFO_NULL = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + NULL_POINTER_EXCEPTION + "05";

    /**
     * 差分包合并失败,尝试下载全量包.
     * <br> 模块：业务库-检查版本模块
     * <br> 异常类型：其他
     */
    public static final String SERVICE_PATCH_FAILED = PROJECT_VERSION
            + MODULE_SERVICE_CHECK + OTHER_EXCEPTION + "01";

    /**
     * 打印所有错误码信息
     *
     * @return key:code  value:description
     */
    public static Map<String, String> getErrorCodes() {
        if (!isErrorMapEmpty()) {
            return mErrCodes;
        }

        CoreErrorCode.getErrorCodes();

        //业务库
        //检查版本模块
        //0D020201
        mErrCodes.put(SERVICE_CHECK_INSTALL_UPDATE_MODE, "升级方式与预期不一致.");
        //0D020301
        mErrCodes.put(SERVICE_CHECK_CHECK_VERSION_ENTITY_LIST_NULL, "检查版本更新失败,请求的数据 List<CheckParams> 不能为空");
        //0D020302
        mErrCodes.put(SERVICE_REPORT_ENTITY_LIST_NULL, "上报APP升级信息 List<RequestReportEntity> 不能为空");
        //0D020303
        mErrCodes.put(SERVICE_BEGIN_DOWNLOAD_PARAMS_NULL, "beginDownload()参数不能为空");
        //0D020304
        mErrCodes.put(SERVICE_CANNOT_INSTALL_PATCH_FILE, "安装失败, 无法安装.patch文件.");
        //0D020305
        mErrCodes.put(SERVICE_INSTALL_FAILED_MD5, "安装失败, 安装文件MD5值不匹配.");
        //0D020306
        mErrCodes.put(SERVICE_INSTALL_FAILED_INFO, "安装失败, 安装文件信息不匹配.");
        //0D020307
        mErrCodes.put(SERVICE_INSTALL_FAILED_CONTEXT_NULL, "安装失败, context == null.");
        //0D020308
        mErrCodes.put(SERVICE_INSTALL_FAILED_NOT_EXISTS, "安装失败, 安装文件不存在.");
        //0D020401
        mErrCodes.put(SERVICE_NET_WORK_EXCEPTION, "网络请求异常");
        //0D020501
        mErrCodes.put(TASK_DATA_EXCEPTION, "已下载数据已无效,请从新检查更新下载");
        //0D020601
        mErrCodes.put(SERVICE_CHECK_RESPONSE_NULL, "无更新信息");
        //0D020602
        mErrCodes.put(SERVICE_INSTALL_VERSION_INFO_NULL, "安装失败, VersionInfo 为空.");
        //0D020603
        mErrCodes.put(SERVICE_INSTALL_APK_FILE_NULL, "安装失败, 找不到安装文件.");
        //0D020604
        mErrCodes.put(SERVICE_CHECK_CHECK_STRATEGY_NULL, "检查策略为空,没有初始化或者已经调用了destroy()需要重新初始化.");
        //0D020605
        mErrCodes.put(SERVICE_CHECK_DOWNLOAD_INFO_NULL, "无法合并差分包,DownloadInfo为null.");
        //0D02ff01
        mErrCodes.put(SERVICE_PATCH_FAILED, "差分包合并失败,尝试下载全量包.");

        return mErrCodes;
    }

    private ErrorCode() {
    }
}
