package com.eebbk.bfc.sdk.version.demo.corelib.function;

import android.content.Context;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.core.sdk.version.OnCoreVerCheckListener;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestReportEntity;
import com.eebbk.bfc.core.sdk.version.entity.request.RequestVersionEntity;
import com.eebbk.bfc.core.sdk.version.entity.response.ResponseEntity;
import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.sdk.version.demo.servicelib.config.ServoceConfig;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.demo.utils.TestUtils;
import com.eebbk.bfc.sdk.version.util.ParamUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

class CoreParameterPresenter {
    private static final String TAG = CoreParameterPresenter.class.getName();
    private BfcCoreVersion bfcCoreVersion = null;
    private Context mContext;

    public CoreParameterPresenter(Context context) {
        mContext = context.getApplicationContext();
        bfcCoreVersion = new BfcCoreVersion.Builder()
                .setCacheLog(true)
                .setUrl(ServoceConfig.getUrl())
                .setDebugMode(true)
                //商城才调用
                .setMultipleRequests(true)
                .build(mContext);
    }

    public void setRequestHead(String machineId, String accountId, String apkPackageName
            , String apkVersionCode, String deviceModel, String deviceOSVersion) {
        bfcCoreVersion.setRequestHead(machineId, accountId, apkPackageName
                , apkVersionCode, deviceModel, deviceOSVersion);
    }

    public void appChcek(List<ITestParameter> mITestParameters) {
        if (ListUtils.isEmpty(mITestParameters)) {
            return;
        }
        bfcCoreVersion.checkVersion(getRequestVersionList(mITestParameters), new OnCoreVerCheckListener() {
            @Override
            public void onResponse(String respSrc, ResponseEntity respEntity) {
                LogUtils.i(TAG, "appCheck respSrc:" + respSrc);
            }

            @Override
            public void onError(BfcHttpError error) {
                LogUtils.i(TAG, "appCheck error:" + error.toString());
            }
        });
    }

    public List<RequestVersionEntity> getRequestVersionList(List<ITestParameter> mITestParameters) {
        List<RequestVersionEntity> list = new ArrayList<RequestVersionEntity>();
        for (ITestParameter params : mITestParameters) {
            if (params == null) {
                continue;
            }
            list.add(getRequestVersionEntity(
                    params.getVersionName(),
                    String.valueOf(params.getVersionCode()),
                    params.getPackageName(),
                    params.getMd5(),
                    params.getApkMd5()
            ));
        }
        return list;
    }

//    public void appCheck(Context context) {
////        manager.checkVersion();
//        List<RequestVersionEntity> requests = new ArrayList<RequestVersionEntity>();
//        for (int i = 0; i < 1; i++) {
//            RequestVersionEntity entity = TestUtils.getRequestVersionEntity();
//            requests.add(entity);
//        }
//        bfcCoreVersion.checkVersion(requests, new OnCoreVerCheckListener() {
//            @Override
//            public void onResponse(String respSrc, ResponseEntity respEntity) {
//                LogUtils.i(TAG, "appCheck respSrc:" + respSrc);
//            }
//
//            @Override
//            public void onError(BfcHttpError error) {
//                LogUtils.i(TAG, "appCheck error:" + error.toString());
//            }
//        });
//    }

    public void appCheck(String... params) {
        List<RequestVersionEntity> requests = new ArrayList<RequestVersionEntity>();
        RequestVersionEntity entity = getRequestVersionEntity(params);
        requests.add(entity);
        bfcCoreVersion.checkVersion(requests, new OnCoreVerCheckListener() {
            @Override
            public void onResponse(String respSrc, ResponseEntity respEntity) {
                LogUtils.i(TAG, "appCheck respSrc:" + respSrc);
            }

            @Override
            public void onError(BfcHttpError error) {
                LogUtils.i(TAG, "appCheck error:" + error.toString());
            }
        });
    }

    public void appReport(Context context) {
        List<RequestReportEntity> requests = new ArrayList<RequestReportEntity>();
        for (int i = 0; i < 5; i++) {
            RequestReportEntity entity = TestUtils.getRequestReportEntity();
            entity.setPackageName("com.eebbk.report" + i);
            requests.add(entity);
        }
        bfcCoreVersion.reportUpdate(requests, new OnCoreVerCheckListener() {
            @Override
            public void onResponse(String respSrc, ResponseEntity respEntity) {
                LogUtils.i(TAG, "appReport respSrc:" + respSrc);
            }

            @Override
            public void onError(BfcHttpError error) {
                LogUtils.i(TAG, "appReport error:" + error.toString());
            }
        });
    }

    private RequestVersionEntity getRequestVersionEntity(String... params) {
        RequestVersionEntity entity = new RequestVersionEntity();
        entity.setVersionName(params[0]);
        int versionCode = -1;
        try {
            versionCode = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        entity.setVersionCode(versionCode);
        entity.setPackageName(params[2]);
        entity.setMd5(params[3]);
        entity.setApkMd5(params[4]);
        entity.setDeviceModel(ParamUtils.getDeviceModel());
        entity.setDeviceOSVersion(ParamUtils.getDeviceOSVersion());
        entity.setMachineId(ParamUtils.getMachineId(mContext));

        return entity;
    }
}
