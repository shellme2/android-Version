package com.eebbk.bfc.sdk.version.demo.servicelib.errorcode;

import android.content.Context;
import android.text.TextUtils;

import com.eebbk.bfc.core.sdk.version.util.ListUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.ErrorCodeDataList;
import com.eebbk.bfc.sdk.version.demo.servicelib.errorcode.data.IErrorCodeTest;
import com.eebbk.bfc.sdk.version.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-11-15
 * @company 步步高教育电子有限公司
 */

class ErrorCodePresenter {
    private static final String TAG = ErrorCodePresenter.class.getName();
    private List<IErrorCodeTest> dataList;
    private IErrorCodeTest mErrorCodeSingleTest;
    private List<String> titleList = new ArrayList<String>();
    private Context mContext;
    private onResultListener l;
    private static final String OK = "√";
    private static final String NO = "×";

    public ErrorCodePresenter(Context context, onResultListener l) {
        mContext = context.getApplicationContext();
        ErrorCodeDataList list = new ErrorCodeDataList();
        dataList = list.getList();
        this.l = l;
    }

    public void onSingle() {
        if (mErrorCodeSingleTest == null) {
            return;
        }
        mErrorCodeSingleTest.run(mContext, onCheckErrorCodeListener);
    }

    /**
     * 开始一键测试
     */
    public void onOneKey() {
        if (ListUtils.isEmpty(dataList)) {
            return;
        }
        for (IErrorCodeTest iErrorCodeTest : dataList) {
            if (iErrorCodeTest == null) {
                continue;
            }
            iErrorCodeTest.run(mContext, onCheckErrorCodeListener);
        }
    }

    /**
     * 设置单个错误码测试数据
     *
     * @param errorCodeSingleTest
     */
    public void setErrorCodeSingleTest(IErrorCodeTest errorCodeSingleTest) {
        this.mErrorCodeSingleTest = errorCodeSingleTest;
    }

    /**
     * 根据错误码获取 IErrorCodeTest 数据
     *
     * @param errorCode
     * @return
     */
    public IErrorCodeTest getErrorCodeTestByCode(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) {
            return null;
        }
        for (IErrorCodeTest iErrorCodeTest : dataList) {
            if (iErrorCodeTest == null) {
                continue;
            }
            if (TextUtils.equals(iErrorCodeTest.getErrorCode(), errorCode)) {
                return iErrorCodeTest;
            }
        }
        return null;
    }

    /**
     * 获取要测试的错误码列表
     *
     * @return
     */
    public List<String> getStringList() {
        if (!ListUtils.isEmpty(titleList)) {
            return titleList;
        }
        for (IErrorCodeTest iErrorCodeTest : dataList) {
            if (iErrorCodeTest == null) {
                continue;
            }
            titleList.add(iErrorCodeTest.getErrorCode());
        }
        return titleList;
    }

    private OnCheckErrorCodeListener onCheckErrorCodeListener = new OnCheckErrorCodeListener() {
        @Override
        public void checkResult(String errorCode, String errorMsg, boolean isOK) {
            LogUtils.i(TAG, "errorCode:" + errorCode + " errorMsg:" + errorMsg + " isOK:" + isOK);
            if (l == null) {
                return;
            }
            l.onResult(logFormat(errorCode, errorMsg, isOK));
        }
    };

    void destroy() {
        if (ListUtils.isEmpty(dataList)) {
            return;
        }
        for (IErrorCodeTest iErrorCodeTest : dataList) {
            Utils.destroy(iErrorCodeTest);
        }
        dataList.clear();
        titleList.clear();
    }

    private String logFormat(String errorCode, String errorMsg, boolean isOK) {
        return TextUtils.concat(
                "错误编码: ", errorCode, "\n",
                "错误提示: ", errorMsg, "\n",
                "验证结果: ", "     ", isOK ? OK : NO, "\n\n"
        ).toString();
    }

    interface onResultListener {
        void onResult(String result);
    }
}
