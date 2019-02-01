package com.eebbk.bfc.core.sdk.version.url;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public interface IUrl {
    /**
     * 应用APP升级接口
     *
     * @return
     */
    String checkVersionAppUrl();

    /**
     * 应用app上报增量更新接口
     *
     * @return
     */
    String reportAppUrl();

    /**
     * 商城增量升级获取APP升级信息
     *
     * @return
     */
    String checkVersionMarketUrl();

    /**
     * 商城增量升级上报APP升级信息
     *
     * @return
     */
    String reportMarketUrl();

    /**
     * 是否为批量处理请求
     *
     * @return
     */
    boolean isBatchRequest();
}
