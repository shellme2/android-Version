package com.eebbk.bfc.core.sdk.version.error;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hesn
 * @function 大数据采集错误码
 * <p> 完整的错误码由：项目编号 + 模块编号 + 错误编号 组成 </p>
 * <p> 具体请参考《中间件错误码项目标号列表》 </p>
 * @date 16-9-29
 * @company 步步高教育电子有限公司
 */

public class CoreErrorCode extends ErrorCodeStandard {

    protected static final Map<String, String> mErrCodes = new HashMap<String, String>();

    /**
     * 检查版本更新失败,context不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_CHECK_CONTEXT_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "01";

    /**
     * 检查版本更新失败,OnCoreVerCheckListener不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_CHECK_LISTENER_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "02";

    /**
     * 检查版本更新失败,List<RequestVersionEntity>不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_CHECK_LIST_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "03";

    /**
     * 检查版本更新失败,请求服务器url不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_CHECK_URL_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "04";

    /**
     * 上报升级信息失败,context不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_REPORT_CONTEXT_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "05";

    /**
     * 上报升级信息失败,List<RequestReportEntity>不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_REPORT_LIST_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "06";

    /**
     * 上报升级信息失败,请求服务器url不能为空
     * <br> 模块：核心库
     * <br> 异常类型：参数异常
     */
    public static final String CORE_REPORT_URL_NULL = PROJECT_VERSION
            + MODULE_CORE + ILLEGAL_ARGUMENT_EXCEPTION + "07";

    /**
     * 请求失败,网络异常
     * <br> 模块：核心库
     * <br> 异常类型：数据处理异常
     */
    public static final String CORE_CHECK_NET_EXCEPTION = PROJECT_VERSION
            + MODULE_CORE + NET_EXCEPTION + "01";

    /**
     * 检查版本更新失败,参数序列化json数据异常
     * <br> 模块：核心库
     * <br> 异常类型：数据处理异常
     */
    public static final String CORE_CHECK_JSON_DATA_NULL = PROJECT_VERSION
            + MODULE_CORE + DATA_EXCEPTION + "01";

    /**
     * 上报升级信息失败,参数序列化json数据异常
     * <br> 模块：核心库
     * <br> 异常类型：数据处理异常
     */
    public static final String CORE_REPORT_JSON_DATA_NULL = PROJECT_VERSION
            + MODULE_CORE + DATA_EXCEPTION + "02";

    public static boolean isErrorMapEmpty(){
        return mErrCodes.isEmpty();
    }

    public static int size(){
        return mErrCodes.size();
    }

    /**
     * 打印所有错误码信息
     *
     * @return key:code  value:description
     */
    public static Map<String, String> getErrorCodes() {
        if (!isErrorMapEmpty()) {
            return mErrCodes;
        }
        //核心库
        //0D010301
        mErrCodes.put(CORE_CHECK_CONTEXT_NULL, "检查版本更新失败,context不能为空");
        //0D010302
        mErrCodes.put(CORE_CHECK_LISTENER_NULL, "检查版本更新失败,OnCoreVerCheckListener不能为空");
        //0D010303
        mErrCodes.put(CORE_CHECK_LIST_NULL, "检查版本更新失败,List<RequestVersionEntity>不能为空");
        //0D010304
        mErrCodes.put(CORE_CHECK_URL_NULL, "检查版本更新失败,请求服务器url不能为空");

        //0D010305
        mErrCodes.put(CORE_REPORT_CONTEXT_NULL, "上报升级信息失败,context不能为空");
        //0D010306
        mErrCodes.put(CORE_REPORT_LIST_NULL, "上报升级信息失败,List<RequestReportEntity>不能为空");
        //0D010307
        mErrCodes.put(CORE_REPORT_URL_NULL, "上报升级信息失败,请求服务器url不能为空");

        //0D010401
        mErrCodes.put(CORE_CHECK_NET_EXCEPTION, "请求失败,网络异常");

        //0D010501
        mErrCodes.put(CORE_CHECK_JSON_DATA_NULL, "检查版本更新失败,参数序列化json数据异常");
        //0D010502
        mErrCodes.put(CORE_REPORT_JSON_DATA_NULL, "上报升级信息失败,参数序列化json数据异常");

        return mErrCodes;
    }
}
