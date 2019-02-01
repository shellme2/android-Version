package com.eebbk.bfc.core.sdk.version.url;

/**
 * @author hesn
 * @function 正式接口
 * @date 17-1-19
 * @company 步步高教育电子有限公司
 */

public class UrlReleaseImpl implements IUrl{
    @Override
    public String checkVersionAppUrl() {
        return "http://jiajiaoji.eebbk.net/appUpdate/apkInfoV2/getNewApkInfoList";
    }

    @Override
    public String reportAppUrl() {
        return "http://jiajiaoji.eebbk.net/appUpdate/apkInfoV2/updateApkDownCountByIdList";
    }

    @Override
    public String checkVersionMarketUrl() {
        return "http://appstore.eebbk.net/appOtherUpdate/getNewApkInfo";
    }

    @Override
    public String reportMarketUrl() {
        return "http://appstore.eebbk.net/appOtherUpdate/updateDownUpCountApkInfo";
    }

    @Override
    public boolean isBatchRequest() {
        return true;
    }
}
