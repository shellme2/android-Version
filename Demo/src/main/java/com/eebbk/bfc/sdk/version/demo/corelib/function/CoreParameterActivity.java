package com.eebbk.bfc.sdk.version.demo.corelib.function;

import android.view.View;
import android.widget.EditText;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ABaseActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.BaiduWenkuTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.demo.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心库参数功能测试
 */
public class CoreParameterActivity extends ABaseActivity {

    private CoreParameterPresenter mPresenter;
    private EditText mApkPackageNameEt;
    private EditText mApkVersionCodeEt;
    private EditText mDeviceModelEt;
    private EditText mMachineIdEt;
    private EditText mDeviceOSVersionEt;
    private EditText mParamsVersionNameEt;
    private EditText mParamsVersionCodeEt;
    private EditText mParamsPackageNameEt;
    private EditText mParamsMd5Et;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_core_parameter;
    }

    @Override
    protected void initView() {
        mApkPackageNameEt = (EditText) findViewById(R.id.apkPackageNameEt);
        mApkVersionCodeEt = (EditText) findViewById(R.id.apkVersionCodeEt);
        mDeviceModelEt = (EditText) findViewById(R.id.deviceModelEt);
        mMachineIdEt = (EditText) findViewById(R.id.machineIdEt);
        mDeviceOSVersionEt = (EditText) findViewById(R.id.deviceOSVersionEt);
        mParamsVersionNameEt = (EditText) findViewById(R.id.paramsVersionNameEt);
        mParamsVersionCodeEt = (EditText) findViewById(R.id.paramsVersionCodeEt);
        mParamsPackageNameEt = (EditText) findViewById(R.id.paramsPackageNameEt);
        mParamsMd5Et = (EditText) findViewById(R.id.paramsMd5Et);
    }

    @Override
    protected void initData() {
        mPresenter = new CoreParameterPresenter(this);
        //请求头初始化
        mApkPackageNameEt.setText(TestUtils.getPackageName());
        mApkVersionCodeEt.setText(String.valueOf(TestUtils.getVersionCode()));
        mDeviceModelEt.setText(TestUtils.getDeviceModel());
        mMachineIdEt.setText(TestUtils.getMachineId());
        mDeviceOSVersionEt.setText(TestUtils.getDeviceOSVersion());

        //请求参数
        mParamsVersionNameEt.setText(TestUtils.getVersionName());
        mParamsVersionCodeEt.setText(String.valueOf(TestUtils.getVersionCode()));
        mParamsPackageNameEt.setText(TestUtils.getPackageName());
        mParamsMd5Et.setText(TestUtils.getMd5());
    }

    @Override
    protected boolean isShowLog() {
        return true;
    }

    public void onAppCheck(View v) {
        mPresenter.setRequestHead(
                getStrFromEt(mMachineIdEt),
                "神童升级",
                getStrFromEt(mApkPackageNameEt),
                getStrFromEt(mApkVersionCodeEt),
                getStrFromEt(mDeviceModelEt),
                getStrFromEt(mDeviceOSVersionEt)
        );
        List<ITestParameter> mITestParameters = new ArrayList<ITestParameter>();
        mITestParameters.add(new BaiduWenkuTestPImpl(this));
        mPresenter.appChcek(mITestParameters);
    }

    public void onAppReport(View v) {
        mPresenter.appReport(this);
    }

    private String getStrFromEt(EditText editText){
        return editText.getText().toString().trim();
    }

}
