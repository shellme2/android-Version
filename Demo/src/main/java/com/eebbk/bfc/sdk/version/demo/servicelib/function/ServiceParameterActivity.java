package com.eebbk.bfc.sdk.version.demo.servicelib.function;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ASpinnerActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.BaiduWenkuTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ITestParameter;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ZhihuTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.elemeTestPImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心库参数功能测试
 */
public class ServiceParameterActivity extends ASpinnerActivity
        implements CompoundButton.OnCheckedChangeListener, ServiceParameterPresenter.OnParameterListener {

    private ServiceParameterPresenter mPresenter;
    private EditText mApkPackageNameEt;
    private EditText mApkVersionCodeEt;
    private EditText mDeviceModelEt;
    private EditText mMachineIdEt;
    private EditText mDeviceOSVersionEt;
    private EditText mParamsVersionNameEt;
    private EditText mParamsVersionCodeEt;
    private EditText mParamsPackageNameEt;
    private EditText mParamsMd5Et;
    private EditText mParamsApkMd5Et;

    private Button mLoaclCheckbtn;
    private Button mLocalReportUpdateBtn;
    private LinearLayout mCheckLayout;
    private LinearLayout mReportUpdateLayout;

    private CheckBox baiduwenkuCb;
    private CheckBox zhihuCb;
    private CheckBox elemeCb;

    private EditText mIgnorePackageNameEt;
    private EditText mIgnoreVersionCodeEt;
    private CheckBox mIgnoreAutoCb;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_service_parameter;
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
        mParamsApkMd5Et = findView(R.id.paramsApkMd5Et);

        mLoaclCheckbtn = findView(R.id.localAppCheckBtn);
        mLocalReportUpdateBtn = findView(R.id.localReportBtn);
        mCheckLayout = findView(R.id.appCheckLayout);
        mReportUpdateLayout = findView(R.id.reportUpdateLayout);

        baiduwenkuCb = findView(R.id.baiduwenkuParamsAppRbtn);
        zhihuCb = findView(R.id.zhihuParamsAppRbtn);
        elemeCb = findView(R.id.elemeParamsAppRbtn);

        mIgnorePackageNameEt = findView(R.id.ignorePackageNameEt);
        mIgnoreVersionCodeEt = findView(R.id.ignoreVersionCodeEt);
        mIgnoreAutoCb = findView(R.id.ignoreAutoCb);
        mIgnoreAutoCb.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new ServiceParameterPresenter(this, this);
        initEt(new BaiduWenkuTestPImpl(this));
        initFunctionView(0);
        mIgnoreAutoCb.setChecked(mPresenter.isGetNewVersion4Ignore());
    }

    private void initEt(ITestParameter iTestParameter) {
        //请求头初始化
        mApkPackageNameEt.setText(iTestParameter.getPackageName());
        mApkVersionCodeEt.setText(String.valueOf(iTestParameter.getVersionCode()));
        mDeviceModelEt.setText(iTestParameter.getDeviceModel());
        mMachineIdEt.setText(iTestParameter.getMachineId());
        mDeviceOSVersionEt.setText(iTestParameter.getDeviceOSVersion());

        //请求参数
        mParamsVersionNameEt.setText(iTestParameter.getVersionName());
        mParamsVersionCodeEt.setText(String.valueOf(iTestParameter.getVersionCode()));
        mParamsPackageNameEt.setText(iTestParameter.getPackageName());
        mParamsMd5Et.setText(iTestParameter.getMd5());
        mParamsApkMd5Et.setText(iTestParameter.getApkMd5());
    }

    @Override
    protected boolean isShowLog() {
        return true;
    }

    @Override
    protected int setSpinnerId() {
        return R.id.spinner;
    }

    @Override
    protected List<String> setSpinnerList() {
        return null;
    }

    @Override
    protected void initSpinnerData() {

    }

    @Override
    protected void onSpinnerSelected(int position, String name) {
        initFunctionView(position);
    }

    /**
     * 获取当前更新版本信息
     *
     * @param v
     */
    public void onLocalAppCheck(View v) {
        mPresenter.setRequestHead(
                getStrFromEt(mMachineIdEt),
                "神童升级",
                getStrFromEt(mApkPackageNameEt),
                getStrFromEt(mApkVersionCodeEt),
                getStrFromEt(mDeviceModelEt),
                getStrFromEt(mDeviceOSVersionEt)
        );
        mPresenter.appCheck(this);
    }

    /**
     * @param v
     */
    public void onLocalReport(View v) {
        mPresenter.localReport();
    }

    /**
     * 获取某指定更新版本信息
     *
     * @param v
     */
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
        if (baiduwenkuCb.isChecked()) {
            mITestParameters.add(new BaiduWenkuTestPImpl(this));
        }
        if (zhihuCb.isChecked()) {
            mITestParameters.add(new ZhihuTestPImpl(this));
        }
        if (elemeCb.isChecked()) {
            mITestParameters.add(new elemeTestPImpl(this));
        }
        mPresenter.appChcek(mITestParameters);
    }

    /**
     * 上报升级信息
     *
     * @param v
     */
    public void onAppReport(View v) {
        mPresenter.appReport();
    }

    /**
     * 删除所有任务
     *
     * @param v
     */
    public void onDeleteAll(View v) {
        mPresenter.deleteAll();
    }

    /**
     * 无视版本
     *
     * @param v
     */
    public void onIgnore(View v) {
        mPresenter.ignoreVersion(this, getStrFromEt(mIgnorePackageNameEt),
                getIntFromEt(mIgnoreVersionCodeEt));
    }

    /**
     * 打印忽略版本
     *
     * @param v
     */
    public void onLogIgnore(View v) {
        mPresenter.logIgnore(this);
    }

    /**
     * 清除忽略版本
     * @param v
     */
    public void onClearIgnore(View v){
        mPresenter.clearIgnore(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    /**
     * 初始化测试功能界面
     *
     * @param index
     */
    private void initFunctionView(int index) {
        mLoaclCheckbtn.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        mLocalReportUpdateBtn.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        mCheckLayout.setVisibility(index == 2 ? View.VISIBLE : View.GONE);
        mReportUpdateLayout.setVisibility(index == 3 ? View.VISIBLE : View.GONE);
        mPresenter.change(this, index > 1);
//        mPresenter.change(this, false);
    }

    private String getStrFromEt(EditText editText) {
        return editText.getText().toString().trim();
    }

    private int getIntFromEt(EditText editText) {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.ignoreAutoCb) {
            mPresenter.setGetNewVersion4Ignore(isChecked);
        }
    }

    @Override
    public void setIgnoreInfo(final String packageName,final int version) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIgnorePackageNameEt.setText(packageName);
                mIgnoreVersionCodeEt.setText(String.valueOf(version));
            }
        });
    }
}
