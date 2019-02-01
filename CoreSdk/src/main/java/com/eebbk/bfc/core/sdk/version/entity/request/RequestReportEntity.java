package com.eebbk.bfc.core.sdk.version.entity.request;

/**
 * 所有属性名不可更改，否则Gson解析失败
 * Created by lcg on 16-4-7.
 */
public class RequestReportEntity extends RequestEntity {

    /**
     * 刚升级的资源id
     */
    private int id;

    /**
     * 下载的新版本名
     */
    private String newVersionName;

    /**
     * 下载新版本版本号
     */
    private int newVersionCode;

    /**
     * 升级状态
     * <br> 成功:Constants.REPORT_UPDATE_SUCCESS
     * <br> 失败:Constants.REPORT_UPDATE_FAILED
     */
    private int stateCode;

    /**
     * 升级异常信息
     */
    private String stateInfo;

    public RequestReportEntity(){

    }

//    public RequestReportEntity(Context context) {
//        super(context);
//
//    }

    /**
     * 刚升级的资源id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 下载的新版本名
     */
    public String getNewVersionName() {
        return newVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        this.newVersionName = newVersionName;
    }

    /**
     * 下载新版本版本号
     */
    public int getNewVersionCode() {
        return newVersionCode;
    }

    public void setNewVersionCode(int newVersionCode) {
        this.newVersionCode = newVersionCode;
    }

    /**
     * 升级状态
     * <br> 成功:Constants.REPORT_UPDATE_SUCCESS
     * <br> 失败:Constants.REPORT_UPDATE_FAILED
     */
    public int getStateCode() {
        return stateCode;
    }

    /**
     * 升级状态
     * <br> 成功:Constants.REPORT_UPDATE_SUCCESS
     * <br> 失败:Constants.REPORT_UPDATE_FAILED
     */
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * 升级异常信息
     */
    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return super.toString()+",[id =" + id + "], [newVersionName =" + newVersionName + "],[newVersionCode ="
                + newVersionCode + "],[stateCode =" + stateCode + "],[stateInfo =" + stateInfo + "]" ;
    }
}
