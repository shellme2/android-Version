package com.eebbk.bfc.core.sdk.version.entity.request;

import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

public class Request {

    private List apps;

    public Request(List apps) {
        this.apps = apps;
    }

    public List getApps() {
        return apps;
    }

    public void setApps(List apps) {
        this.apps = apps;
    }
}
