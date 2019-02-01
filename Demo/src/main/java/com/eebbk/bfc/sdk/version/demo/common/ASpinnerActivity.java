package com.eebbk.bfc.sdk.version.demo.common;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.eebbk.bfc.core.sdk.version.util.ListUtils;

import java.util.List;

/**
 * @author hesn
 * @function 有选择采集事件的下拉选择框界面基类
 * @date 16-10-13
 * @company 步步高教育电子有限公司
 */

public abstract class ASpinnerActivity extends ABaseActivity{

    private SpinnerPresenter mSpinnerPresenter;

    /**
     * 下拉框控件id
     * @return
     */
    protected abstract int setSpinnerId();

    /**
     * 设置下拉框数据
     * <br> 如果为空,则赋予 SpinnerPresenter 里的默认数据数据
     * @return
     */
    protected abstract List<String> setSpinnerList();

    /**
     * 初始化下拉框控件相关数据
     * <br> 在此下拉框才初始化完成,避免空指针
     * @return
     */
    protected abstract void initSpinnerData();

    /**
     * 选中的采集事件
     * @param position
     * @param name 选中的项名称
     */
    protected abstract void onSpinnerSelected(int position, String name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventSpinner();
        initSpinnerData();
    }

    private void initEventSpinner(){
        mSpinnerPresenter = new SpinnerPresenter();
        Spinner mSpinner = findView(setSpinnerId());
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getSpinnerList());
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerSelected(position, getSpinnerList().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取下拉框数据
     * <br> 如果上层有设置 List<String> setSpinnerList() 数据,则获取setSpinnerList()数据
     * <br> 否则获取 SpinnerPresenter 中的数据
     * @return
     */
    private List<String> getSpinnerList(){
        return ListUtils.isEmpty(setSpinnerList()) ?
                mSpinnerPresenter.getSpinnerList() : setSpinnerList();
    }
}
