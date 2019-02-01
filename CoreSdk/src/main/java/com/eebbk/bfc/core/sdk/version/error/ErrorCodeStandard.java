package com.eebbk.bfc.core.sdk.version.error;

/**
 * @author hesn
 * @function 错误码规范
 * @date 16-9-30
 * @company 步步高教育电子有限公司
 */
public class ErrorCodeStandard {
    /**
     * 项目编号
     * <p> 根据《中间件错误码项目标号列表》中决定 </p>
     */
    protected static final String PROJECT_VERSION = "0D";

    /**
     * 核心库
     */
    protected static final String MODULE_CORE = "01";
    /**
     * 业务库-检查版本模块
     */
    protected static final String MODULE_SERVICE_CHECK = "02";

    //01XX：初始化异常
    //02XX：没有按规范操作
    //03XX：参数异常
    //04XX：网络异常
    //05XX：数据处理异常
    //06XX：空指针
    //ffXX：其他
    /**
     * 01XX：初始化异常
     */
    protected static final String INIT_EXCEPTION = "01";
    /**
     * 02XX：规范操作
     */
    protected static final String OPERATION_EXCEPTION = "02";
    /**
     * 03XX：参数异常
     */
    protected static final String ILLEGAL_ARGUMENT_EXCEPTION = "03";
    /**
     * 04XX：网络异常
     */
    protected static final String NET_EXCEPTION = "04";
    /**
     * 05XX：数据处理异常
     */
    protected static final String DATA_EXCEPTION = "05";
    /**
     * 06XX：空指针
     */
    protected static final String NULL_POINTER_EXCEPTION = "06";
    /**
     * ffXX：其他
     */
    protected static final String OTHER_EXCEPTION = "ff";

}
