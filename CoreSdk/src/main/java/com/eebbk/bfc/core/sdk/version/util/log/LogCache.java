package com.eebbk.bfc.core.sdk.version.util.log;

/**
 * @author hesn
 * @function 日志缓存
 * @date 16-10-14
 * @company 步步高教育电子有限公司
 */

class LogCache {

    private final StringBuffer mAllLogSb = new StringBuffer();
    private OnLogListener mListener;

    void setListener(OnLogListener l) {
        mListener = l;
    }

    LogCache(){
        cleanAllLog();
    }

    void addAll(String log){
        if(!LogUtils.isCacheLog()){
            return;
        }
        mAllLogSb.insert(0,log + "\n");
        if(mAllLogSb.length() >= 5000){
            mAllLogSb.delete(1500,mAllLogSb.length());
        }

        callBack();
    }

    /**
     * 读取当前日志
     *
     * @return
     */
    String readCurrentAllLog(){
        return mAllLogSb.toString();
    }

    private void cleanAllLog(){
        mAllLogSb.delete(0, mAllLogSb.length());
    }

    private void callBack(){
        if(mListener == null){
            return;
        }
        mListener.onLogRefresh(mAllLogSb.toString());
    }
}
