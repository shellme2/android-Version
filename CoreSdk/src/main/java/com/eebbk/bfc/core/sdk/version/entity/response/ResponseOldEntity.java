package com.eebbk.bfc.core.sdk.version.entity.response;

/**
 * 服务器返回版本信息Bean
 * <br> 老接口单个请求用到
 * <p> 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public class ResponseOldEntity {
    //升级信息
    private DataInfoEntity data;
    //状态码 0标识成功
    private String stateCode;
    //状态描述信息
    private String stateInfo;


    public DataInfoEntity getData() {
        return data;
    }

    public void setData(DataInfoEntity data) {
        this.data = data;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return "ResponseEntity { data = " + data + "},[ stateCode =" + stateCode + "], [stateInfo =" + stateInfo + "]";
    }
}
