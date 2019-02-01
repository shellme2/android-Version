package com.eebbk.bfc.sdk.version.demo.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function 下拉框选择默认配置数据
 * @date 16-10-13
 * @company 步步高教育电子有限公司
 */

class SpinnerPresenter {

    private final List<String> list = new ArrayList<String>();

    public SpinnerPresenter(){
        list.add("获取本地app版本更新信息");
        list.add("上报本地app升级信息");
        list.add("获取指定app版本更新信息");
        list.add("上报指定app升级信息");
    }

    public List<String> getSpinnerList(){
        return list;
    }
}
