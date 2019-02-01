package com.eebbk.bfc.sdk.version.demo;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.eebbk.bfc.common.app.AppUtils;
import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.SDKVersion;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.demo.common.ABaseActivity;
import com.eebbk.bfc.sdk.version.demo.corelib.CoreTestMenuActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.ServiceTestMenuActivity;
import com.eebbk.bfc.sdk.version.patch.algorithm.BBKPatchImpl;
import com.eebbk.bfc.sdk.version.patch.algorithm.BaiduPatchImpl;
import com.eebbk.bfc.sdk.version.patch.algorithm.IPatch;
import com.eebbk.bfc.sdk.version.util.ExecutorsUtils;
import com.eebbk.bfc.sdk.version.util.Md5Utils;

import java.io.File;

public class MenuActivity extends ABaseActivity {

    private TextView versionTv;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initView() {
        versionTv = findView(R.id.versionTv);
    }

    @Override
    protected void initData() {
        versionTv.setText(TextUtils.concat(
                "\r\n >>核心库版本信息<<",
                "\r\n 库名称: ", SDKVersion.getLibraryName(),
                "\r\n 版本序号: ", String.valueOf(SDKVersion.getSDKInt()),
                "\r\n 版本名称: ", SDKVersion.getVersionName(),
                "\r\n 构建版本: ", SDKVersion.getBuildName(),
                "\r\n 构建时间: ", SDKVersion.getBuildTime(),
                "\r\n TAG标签: ", SDKVersion.getBuildTag(),
                "\r\n HEAD值: ", SDKVersion.getBuildHead(),
                "\n\n",

                "\r\n >>业务库版本信息<<",
                "\r\n 库名称: ", com.eebbk.bfc.sdk.version.SDKVersion.getLibraryName(),
                "\r\n 版本序号: ", String.valueOf(com.eebbk.bfc.sdk.version.SDKVersion.getSDKInt()),
                "\r\n 版本名称: ", com.eebbk.bfc.sdk.version.SDKVersion.getVersionName(),
                "\r\n 构建版本: ", com.eebbk.bfc.sdk.version.SDKVersion.getBuildName(),
                "\r\n 构建时间: ", com.eebbk.bfc.sdk.version.SDKVersion.getBuildTime(),
                "\r\n TAG标签: ", com.eebbk.bfc.sdk.version.SDKVersion.getBuildTag(),
                "\r\n HEAD值: ", com.eebbk.bfc.sdk.version.SDKVersion.getBuildHead(),
                "\n\n",

                "\r\n >>demo版本信息<<",
                "\r\n 名称: ", AppUtils.getAppName(this),
                "\r\n 版本序号: ", String.valueOf(AppUtils.getVersionCode(this)),
                "\r\n 版本名称: ", AppUtils.getVersionName(this),
                "\r\n 包名: ", this.getPackageName()
        ));
    }

    @Override
    protected boolean isShowLog() {
        return false;
    }

    /**
     * 核心库
     *
     * @param v
     */
    public void onCoreLib(View v) {
        startActivity(new Intent(this, CoreTestMenuActivity.class));
//        testPatch();
//        testPatchDemo();
    }

    private void testPatch() {
        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory() + File.separator +
                        "patch_test" + File.separator;
                LogUtils.i("hesn", "versionCode:" + AppUtils.getVersionCode(MenuActivity.this, "com.baidu.wenku"));
                IPatch patch = new BaiduPatchImpl();
                int result = patch.patch(
                        path + "baiduwenku_37.apk",
                        path + "baiduwenku_38_patch.patch",
                        path + "baiduwenku_4050_new.apk"
                );
                LogUtils.i("hesn", "patch_result:" + result);
                LogUtils.i("hesn", "size:" + FileUtils.getFileSize(path + "baiduwenku_4050_new.apk"));
                LogUtils.i("hesn", "md5:" + Md5Utils.getMd5ByFile(new File(path + "baiduwenku_4050.apk")));
                LogUtils.i("hesn", "md5:" + Md5Utils.getMd5ByFile(new File(path + "baiduwenku_4050_new.apk")));
            }
        });
    }

    private void testPatchDemo() {
        ExecutorsUtils.execute(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory() + File.separator +
                        "patch_test" + File.separator;
//                LogUtils.i("hesn", "versionCode:" + AppUtils.getVersionCode(MenuActivity.this, "com.baidu.wenku"));
                IPatch patch = new BBKPatchImpl();
                int result = patch.patch(
                        path + "BfcVersionDemo_9.apk",
                        path + "BfcVersionDemo.patch",
                        path + "BfcVersionDemo_500_new.apk"
                );
                LogUtils.i("hesn", "patch_result:" + result);
                LogUtils.i("hesn", "size:" + FileUtils.getFileSize(path + "BfcVersionDemo_500_new.apk"));
                LogUtils.i("hesn", "md5:" + Md5Utils.getMd5ByFile(new File(path + "BfcVersionDemo_500.apk")));
                LogUtils.i("hesn", "md5:" + Md5Utils.getMd5ByFile(new File(path + "BfcVersionDemo_500_new.apk")));
            }
        });
    }

    private void testGetMd5() {
        //        LogUtils.i("hesn", "b-com.xiachufang:" + ParamUtils.getBaiduMd5(this, "com.xiachufang"));
//        LogUtils.i("hesn", "w-com.xiachufang:" + ParamUtils.getWanDouJiaMd5(this, "com.xiachufang"));
//        LogUtils.i("hesn", "z-com.xiachufang:" + AppUtils.getSignatureMd5(this, "com.xiachufang"));
//
//        LogUtils.i("hesn", "b-com.baidu.wenku:" + ParamUtils.getBaiduMd5(this, "com.baidu.wenku"));
//        LogUtils.i("hesn", "w-com.baidu.wenku:" + ParamUtils.getWanDouJiaMd5(this, "com.baidu.wenku"));
//        LogUtils.i("hesn", "z-com.baidu.wenku:" + AppUtils.getSignatureMd5(this, "com.baidu.wenku"));
//
//        LogUtils.i("hesn", "b-com.baidu.BaiduMap:" + ParamUtils.getBaiduMd5(this, "com.baidu.BaiduMap"));
//        LogUtils.i("hesn", "w-com.baidu.BaiduMap:" + ParamUtils.getWanDouJiaMd5(this, "com.baidu.BaiduMap"));
//        LogUtils.i("hesn", "z-com.baidu.BaiduMap:" + AppUtils.getSignatureMd5(this, "com.baidu.BaiduMap"));
//
//        LogUtils.i("hesn", "b-com.ximalaya.ting.android:" + ParamUtils.getBaiduMd5(this, "com.ximalaya.ting.android"));
//        LogUtils.i("hesn", "w-com.ximalaya.ting.android:" + ParamUtils.getWanDouJiaMd5(this, "com.ximalaya.ting.android"));
//        LogUtils.i("hesn", "z-com.ximalaya.ting.android:" + AppUtils.getSignatureMd5(this, "com.ximalaya.ting.android"));

//        String apkPath = VersionConstants.PATH_SD + "apk" + File.separator + "baiduwenku_37.apk";
//        LogUtils.i("hesn", "path:" + apkPath);
//        LogUtils.i("hesn", "md5:" + Md5Utils.getMd5ByFile(new File(apkPath)));
//        LogUtils.i("hesn", "versionName:" + AppUtils.getVersionName(this,"com.baidu.wenku"));
//        LogUtils.i("hesn", "versionCode:" + AppUtils.getVersionCode(this,"com.baidu.wenku"));

//        GDiffPatcher gDiffPatcher = new GDiffPatcher();
//        String oldapk = VersionConstants.PATH_SD + VersionConstants.DOWNLOAD_APK_FOLDER + File.separator + "com.xiachufang.apk";
//        String patch = VersionConstants.PATH_SD + VersionConstants.DOWNLOAD_PATCH_FOLDER + File.separator + "148300432763320.d";
//        String newApk = VersionConstants.PATH_SD + VersionConstants.NEW_APK_FOLDER + File.separator + "com.xiachufang.apk";
//        FileUtils.createFileOrExists(newApk);
//        try {
//            gDiffPatcher.patch(new File(oldapk), new File(patch), new File(newApk));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        LogUtils.i("hesn", "versionName:" + AppUtils.getVersionName(this,"com.estrongs.android.taskmanager"));
//        LogUtils.i("hesn", "versionCode:" + AppUtils.getVersionCode(this,"com.estrongs.android.taskmanager"));
//        LogUtils.i("hesn", "w-com.estrongs.android.taskmanager:" + ParamUtils.getWanDouJiaMd5(this, "com.estrongs.android.taskmanager"));
//        LogUtils.i("hesn", "1111:" + FileUtils.isFileExists(TextUtils.concat(
//                VersionConstants.PATH_B, VersionConstants.NEW_APK_FOLDER , File.separator
//                , "com.xiachufang.apk"
//                ).toString()
//        ));
    }

    static {
        System.loadLibrary("ApkPatchLibrary");
    }

    /**
     * 业务库
     *
     * @param v
     */
    public void onServiceLib(View v) {
        startActivity(new Intent(this, ServiceTestMenuActivity.class));
    }
}
