package com.eebbk.bfc.core.sdk.version.util;

import android.content.Context;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.Constants;
import com.eebbk.bfc.core.sdk.version.OnCoreVerCheckListener;
import com.eebbk.bfc.core.sdk.version.entity.request.Request;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseOldEntity;
import com.eebbk.bfc.core.sdk.version.error.CoreErrorCode;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private static final String TAG = "HttpUtils";
    private static final String PARAM_DATA = "data";
    private static final Map<String, String> mHeader = new HashMap<String, String>();

    /**
     * 设置通用请求头信息
     *
     * @param machineId       机器码
     * @param accountId       账户id
     * @param apkPackageName  app包名
     * @param apkVersionCode  app版本号
     * @param deviceModel     机型名
     * @param deviceOSVersion 系统版本名
     */
    public static void setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion) {
        mHeader.put("machineId", machineId);
        mHeader.put("accountId", accountId);
        mHeader.put("apkPackageName", apkPackageName);
        mHeader.put("apkVersionCode", apkVersionCode);
        mHeader.put("deviceModel", deviceModel);
        mHeader.put("deviceOSVersion", deviceOSVersion);
    }

    /**
     * 请求头信息是否已经设置
     *
     * @return
     */
    public static boolean isRequestHeadEmpty() {
        return mHeader.isEmpty();
    }

    /**
     * 向服务器获取版本信息
     *
     * @param context 上下文
     * @param entitys 获取版本信息参数封装entitys
     * @param url     服务器接口地址
     * @param l       回调接口
     */
    public static void versionRequest(Context context, List<RequestVersionEntity> entitys, String url
            , final OnCoreVerCheckListener l) {
        LogUtils.d(TAG, "version request start");
        String data = JsonUtils.toJson(new Request(entitys));
        if (TextUtils.isEmpty(data)) {
            LogUtils.bfcExceptionLog(TAG, CoreErrorCode.CORE_CHECK_JSON_DATA_NULL);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_DATA, data);
        LogUtils.d(TAG, "version request url:" + url);
        BfcHttp.post(context, url, params, mHeader, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, "JsonResult : " + response);
                ResponseEntity responseEntity = JsonUtils.fromJson(response, ResponseEntity.class);
                LogUtils.d(TAG, "responseEntity : " + responseEntity);
                l.onResponse(response, responseEntity);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                LogUtils.w(TAG, error.toString());
                l.onError(error);
            }
        }, TAG);
    }

    /**
     * 向服务器获取版本信息(旧接口,单个请求)
     *
     * @param context 上下文
     * @param entity  获取版本信息参数封装entitys
     * @param url     服务器接口地址
     * @param l       回调接口
     */
    public static void versionRequest(Context context, RequestVersionEntity entity, String url
            , final OnCoreVerCheckListener l) {
        LogUtils.d(TAG, "version request start");
//        String data = JsonUtils.toJson(entity);
        if (entity == null) {
            LogUtils.bfcExceptionLog(TAG, CoreErrorCode.CORE_CHECK_JSON_DATA_NULL);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();

        params.put(Constants.PARAMS_DEVICE_MODEL, entity.getDeviceModel());
        params.put(Constants.PARAMS_DEVICE_OS_VERSION, entity.getDeviceOSVersion());
        params.put(Constants.PARAMS_MACHINE_ID, entity.getMachineId());
        params.put(Constants.PARAMS_VERSION_NAME, entity.getVersionName());
        params.put(Constants.PARAMS_PACKAGE_NAME, entity.getPackageName());
        params.put(Constants.PARAMS_VERSION_CODE, String.valueOf(entity.getVersionCode()));

        LogUtils.d(TAG, "version request url:" + url);
        BfcHttp.post(context, url, params, mHeader, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, "JsonResult : " + response);
                ResponseOldEntity responseOldEntity = JsonUtils.fromJson(response, ResponseOldEntity.class);
                LogUtils.d(TAG, "responseOldEntity : " + responseOldEntity);
                ResponseEntity responseEntity = new ResponseEntity();
                responseEntity.setStateCode(responseOldEntity.getStateCode());
                responseEntity.setStateInfo(responseOldEntity.getStateInfo());
                l.onResponse(response, responseEntity);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                LogUtils.w(TAG, error.toString());
                l.onError(error);
            }
        }, TAG);
    }

    /**
     * 上报升级信息
     *
     * @param context 上下文
     * @param entitys 上报信息封装entitys
     * @param url     上报地址
     * @param l       回调接口
     */
    public static void report(Context context, List<RequestReportEntity> entitys, String url
            , final OnCoreVerCheckListener l) {
        LogUtils.d(TAG, "report start");
        String data = JsonUtils.toJson(new Request(entitys));
        if (TextUtils.isEmpty(data)) {
            LogUtils.bfcExceptionLog(TAG, CoreErrorCode.CORE_REPORT_JSON_DATA_NULL);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_DATA, data);
        LogUtils.d(TAG, "version report url:" + url);
        BfcHttp.post(context, url, params, mHeader, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doReportResponse(response, l);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                LogUtils.w(TAG, error.toString());
                if (l != null) {
                    l.onError(error);
                }
            }
        }, TAG);
    }

    /**
     * 上报升级信息(旧接口,单个请求)
     *
     * @param context 上下文
     * @param entity  上报信息封装entitys
     * @param url     上报地址
     * @param l       回调接口
     */
    public static void report(Context context, RequestReportEntity entity, String url
            , final OnCoreVerCheckListener l) {
        LogUtils.d(TAG, "report start");
        String data = JsonUtils.toJson(entity);
        if (TextUtils.isEmpty(data)) {
            LogUtils.bfcExceptionLog(TAG, CoreErrorCode.CORE_REPORT_JSON_DATA_NULL);
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_DATA, data);
        LogUtils.d(TAG, "version report url:" + url);
        BfcHttp.post(context, url, params, mHeader, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doReportResponse(response, l);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                LogUtils.w(TAG, error.toString());
                if (l != null) {
                    l.onError(error);
                }
            }
        }, TAG);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        BfcHttp.cancelAll(TAG);
    }

    /**
     * 上报升级信息返回信息回调
     *
     * @param response 返回信息
     * @param l        回调接口
     */
    private static void doReportResponse(String response, OnCoreVerCheckListener l) {
        LogUtils.d(TAG, "report jsonResult:" + response);
        ResponseEntity responseEntity = JsonUtils.fromJson(response, ResponseEntity.class);
        if (responseEntity != null) {
            LogUtils.d(TAG, "responseEntity analysis success:" + responseEntity.toString());
        } else {
            LogUtils.d(TAG, "cant't get report response json string to entity.");
        }
        if (l != null) {
            l.onResponse(response, responseEntity);
        }
    }

    private HttpUtils() {

    }

}
