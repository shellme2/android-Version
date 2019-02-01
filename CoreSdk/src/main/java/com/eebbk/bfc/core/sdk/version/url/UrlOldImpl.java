package com.eebbk.bfc.core.sdk.version.url;

/**
 * @author hesn
 * @function 老的正式请求接口(单个请求)
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public class UrlOldImpl implements IUrl{

    @Override
    public String checkVersionAppUrl() {
        return "http://jiajiaoji.eebbk.net/appUpdate/apkInfo/getNewApkInfo";
    }

    @Override
    public String reportAppUrl() {
        return "http://jiajiaoji.eebbk.net/appUpdate/apkInfoV2/updateApkDownCountById";
    }

    @Override
    public String checkVersionMarketUrl() {
        return null;
    }

    @Override
    public String reportMarketUrl() {
        return null;
    }

    @Override
    public boolean isBatchRequest() {
        return false;
    }
}
