package com.eebbk.bfc.core.sdk.version.url;

/**
 * @author hesn
 * @function 测试接口
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public class UrlTestImpl implements IUrl{

    @Override
    public String checkVersionAppUrl() {
        return "http://172.28.1.138:6480/appUpdate/apkInfoV2/getNewApkInfoList";
    }

    @Override
    public String reportAppUrl() {
        return "http://172.28.1.138:6480/appUpdate/apkInfoV2/updateApkDownCountByIdList";
    }

    @Override
    public String checkVersionMarketUrl() {
        return "http://172.28.1.138:6080/h600s/appOtherUpdate/getNewApkInfo";
    }

    @Override
    public String reportMarketUrl() {
        return "http://172.28.1.138:6080/h600s/appOtherUpdate/updateDownUpCountApkInfo";
    }

    @Override
    public boolean isBatchRequest() {
        return true;
    }
}
