package com.eebbk.bfc.core.sdk.version.entity.response;

import java.util.List;

/**
 * 服务器返回版本信息Bean
 * <p>所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public class ResponseEntity {
    //升级信息
    private List<DataInfoEntity> data;
    //状态码 0标识成功
    private String stateCode;
    //状态描述信息
    private String stateInfo;


    public List<DataInfoEntity> getData() {
        return data;
    }

    public ResponseEntity setData(List<DataInfoEntity> data) {
        this.data = data;
        return this;
    }

    public String getStateCode() {
        return stateCode;
    }

    public ResponseEntity setStateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public ResponseEntity setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
        return this;
    }

//    public ResponseEntity set

    @Override
    public String toString() {
        return "ResponseEntity { data = " + data + "},[ stateCode =" + stateCode + "], [stateInfo =" + stateInfo + "]";
    }
}
