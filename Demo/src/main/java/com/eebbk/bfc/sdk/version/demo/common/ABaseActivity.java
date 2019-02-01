package com.eebbk.bfc.sdk.version.demo.common;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author hesn
 * @function 测试基activity类
 * @date 16-8-24
 * @company 步步高教育电子有限公司
 */

public abstract class ABaseActivity extends Activity{

    private LogPresenter mLogPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isShowLog()){
            mLogPresenter.startRefreshLog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isShowLog()){
            mLogPresenter.stopRefreshLog();
        }
    }

    /**
     * 设置布局文件　layout id
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 是否显示打印日志
     */
    protected abstract boolean isShowLog();

    /**
     * 根据id找控件
     * @param id
     * @param <T>
     * @return
     */
    protected <T> T findView(int id){
        return (T)findViewById(id);
    }

    private void init(){
        setContentView(setLayoutId());
        initView();
        initData();
        if(isShowLog()){
            mLogPresenter = new LogPresenter(this);
        }
    }

}
