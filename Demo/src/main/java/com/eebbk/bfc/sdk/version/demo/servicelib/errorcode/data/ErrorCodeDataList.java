package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data;

import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core.ErrorCodeTest0D010303Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core.ErrorCodeTest0D010304Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core.ErrorCodeTest0D010306Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core.ErrorCodeTest0D010307Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.core.ErrorCodeTest0D010401Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020301Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020302Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020303Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020304Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020306Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020307Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020308Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020602Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020603Impl;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.service.ErrorCodeTest0D020604Impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function 错误码测试数据列表
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeDataList {

    private List<IErrorCodeTest> list = new ArrayList<IErrorCodeTest>();

    public ErrorCodeDataList(){
        initData();
    }

    private void initData(){
        list.clear();
        //业务库
        list.add(new ErrorCodeTest0D020301Impl());
        list.add(new ErrorCodeTest0D020302Impl());
        list.add(new ErrorCodeTest0D020303Impl());
        list.add(new ErrorCodeTest0D020304Impl());
        list.add(new ErrorCodeTest0D020306Impl());
        list.add(new ErrorCodeTest0D020307Impl());
        list.add(new ErrorCodeTest0D020308Impl());
        list.add(new ErrorCodeTest0D020602Impl());
        list.add(new ErrorCodeTest0D020603Impl());
        list.add(new ErrorCodeTest0D020604Impl());

        //核心库
        list.add(new ErrorCodeTest0D010303Impl());
        list.add(new ErrorCodeTest0D010304Impl());
        list.add(new ErrorCodeTest0D010306Impl());
        list.add(new ErrorCodeTest0D010307Impl());
        list.add(new ErrorCodeTest0D010401Impl());
    }

    public List<IErrorCodeTest> getList(){
        return list;
    }
}
