package com.eebbk.bfc.sdk.version.demo.servicelib.performance.loop;

import android.os.PowerManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.eebbk.bfc.sdk.version.demo.R;
import com.eebbk.bfc.sdk.version.demo.common.ASpinnerActivity;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.BaiduWenkuTestPImpl;
import com.eebbk.bfc.sdk.version.demo.servicelib.function.test.ZhihuTestPImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-12-12
 * @company 步步高教育电子有限公司
 */

public class ServiceLoopActivity extends ASpinnerActivity implements RadioGroup.OnCheckedChangeListener {

    private ServiceLoopPresenter mPresenter;
    private PowerManager.WakeLock mWakelock;
    private ProgressBar mBar;
//    private CheckBox baiduwenkuCb;
//    private CheckBox zhihuCb;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_service_loop;
    }

    @Override
    protected void initView() {
        mBar = findView(R.id.progressBar);
//        baiduwenkuCb = findView(R.id.baiduwenkuParamsAppRbtn);
//        zhihuCb = findView(R.id.zhihuParamsAppRbtn);
    }

    @Override
    protected void initData() {
        mBar.setMax(100);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);// init powerManager
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_DIM_WAKE_LOCK, "target"); // this target for tell OS which app

        mPresenter = new ServiceLoopPresenter(this, onProgressBarListener, new BaiduWenkuTestPImpl(this));

//        baiduwenkuCb.setVisibility(View.INVISIBLE);
//        zhihuCb.setVisibility(View.INVISIBLE);
    }

    @Override
    protected boolean isShowLog() {
        return true;
    }

    /**
     * 开始循环测试
     *
     * @param view
     */
    public void onStartLoop(View view) {
        mPresenter.appCheck(this);
        mWakelock.acquire();
    }

    /**
     * 停止循环测试
     *
     * @param view
     */
    public void onStopLoop(View view) {
        mPresenter.stop();
        if (mWakelock.isHeld()) {
            mWakelock.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        if (mWakelock.isHeld()) {
            mWakelock.release();
        }
    }

    private final ServiceLoopPresenter.OnProgressBarListener onProgressBarListener = new ServiceLoopPresenter.OnProgressBarListener() {
        @Override
        public void setProgress(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBar.setProgress(progress);
                }
            });
        }
    };

    @Override
    protected int setSpinnerId() {
        return R.id.spinner;
    }

    @Override
    protected List<String> setSpinnerList() {
        List<String> list = new ArrayList<String>();
        list.add("本地接口app版本更新信息");
        list.add("商城接口app版本更新信息");
        return list;
    }

    @Override
    protected void initSpinnerData() {

    }

    @Override
    protected void onSpinnerSelected(int position, String name) {
        boolean isMultipleRequests = position == 1;
//        baiduwenkuCb.setVisibility(isMultipleRequests ? View.VISIBLE : View.INVISIBLE);
//        zhihuCb.setVisibility(isMultipleRequests ? View.VISIBLE : View.INVISIBLE);
        mPresenter.change(this, isMultipleRequests);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.baiduwenkuParamsAppRbtn:
                mPresenter.setTestData(new BaiduWenkuTestPImpl(this));
                break;
            case R.id.zhihuParamsAppRbtn:
                mPresenter.setTestData(new ZhihuTestPImpl(this));
                break;
            default:
                break;
        }
    }
}
