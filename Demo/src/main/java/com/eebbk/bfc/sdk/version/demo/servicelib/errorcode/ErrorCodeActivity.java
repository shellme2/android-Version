package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ASpinnerActivity;

import java.util.List;

/**
 * @author hesn
 * @function 错误码测试
 * @date 17-3-7
 * @company 步步高教育电子有限公司
 */

public class ErrorCodeActivity extends ASpinnerActivity implements ErrorCodePresenter.onResultListener {

    private ErrorCodePresenter presenter;
    private TextView mCheckResultTv;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_service_error_code;
    }

    @Override
    protected void initView() {
        mCheckResultTv = findView(R.id.checkResultTv);
    }

    @Override
    protected void initData() {
        presenter = new ErrorCodePresenter(this.getApplicationContext(), this);
        //添加滚动效果
        mCheckResultTv.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected boolean isShowLog() {
        return false;
    }

    @Override
    protected int setSpinnerId() {
        return R.id.spinner;
    }

    @Override
    protected List<String> setSpinnerList() {
        return presenter.getStringList();
    }

    @Override
    protected void initSpinnerData() {
        presenter.setErrorCodeSingleTest(presenter.getErrorCodeTestByCode(presenter.getStringList().get(0)));
    }

    @Override
    protected void onSpinnerSelected(int position, String name) {
        presenter.setErrorCodeSingleTest(presenter.getErrorCodeTestByCode(name));
    }

    /**
     * 开始单个错误码测试
     *
     * @param v
     */
    public void onSingle(View v) {
        presenter.onSingle();
    }

    /**
     * 开始一键测试
     *
     * @param v
     */
    public void onOneKey(View v) {
        presenter.onOneKey();
    }

    @Override
    public void onResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCheckResultTv.setText(result + mCheckResultTv.getText());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
