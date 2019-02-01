package com.eebbk.bfc.core.sdk.version.util.log;

import android.text.TextUtils;
import android.util.Log;

import com.eebbk.bfc.bfclog.BfcLog;
import com.eebbk.bfc.core.sdk.version.error.ErrorCodeAgent;
import com.eebbk.bfc.core.sdk.version.version.Build;

/**
 * @author hesn
 * @function
 * @date
 * @company 步步高教育电子有限公司
 */
public class LogUtils{
    private static final String TAG = "BfcVersion";
    private static boolean isLog = true;
    private static BfcLog mBfcLogDefault;
    private static final LogCache mLogCache = new LogCache();
    private static final int METHOD_COUNT = 10;
    private static final int METHOD_OFFSET = 4;
    private static final int JSON = -1;
    private static boolean isCacheLog = false;
    private static boolean isDebugMode = false;

    public static boolean isDebugMode(){
        return isDebugMode;
    }

    public static void setDebugMode(boolean isDebugMode){
        LogUtils.isDebugMode = isDebugMode;
    }

    public static boolean isCacheLog(){
        return isCacheLog;
    }

    public static void setCacheLog(boolean isCacheLog){
        LogUtils.isCacheLog = isCacheLog;
    }

    public static boolean isLog(){
        return isLog;
    }

    public static void setIsLog(boolean isLog){
        LogUtils.isLog = isLog;
    }

    public static void setListener(OnLogListener l){
        mLogCache.setListener(l);
    }

    public static void cancelListener(){
        mLogCache.setListener(null);
    }

    private static void addAllLogCache(String log){
        mLogCache.addAll(log + '\n');
    }

    public static void logStackTrace(){
        logChunkInSize(Log.DEBUG, TAG, "", true);
    }

    public static void v(String msg){
        v(TAG, msg);
    }

    public static void d(String msg){
        d(TAG, msg);
    }

    public static void i(String msg){
        i(TAG, msg);
    }

    public static void w(String msg){
        w(TAG, msg);
    }

    public static void e(String msg){
        e(TAG, msg);
    }

    public static void e(Exception msg){
        e(TAG, msg.toString());
    }

    public static void v(String tag, String msg){
        logChunkInSize(Log.VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg){
        logChunkInSize(Log.DEBUG, tag, msg);
    }

    public static void i(String tag, String msg){
        logChunkInSize(Log.INFO, tag, msg);
    }

    public static void w(String tag, String msg){
        logChunkInSize(Log.WARN, tag, msg);
    }

    public static void w(String tag, Exception msg){
        logChunkInSize(Log.WARN, tag, Log.getStackTraceString(msg));
    }

    public static void e(String tag, String msg){
        logChunkInSize(Log.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr){
        logChunkInSize(Log.ERROR, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    /**
     * bfc错误日志
     *
     * @param tag
     * @param eCode 错误码
     */
    public static String bfcExceptionLog(String tag, String eCode){
        return bfcExceptionLog(tag, eCode, "");
    }

    /**
     * bfc错误日志
     *
     * @param tag
     * @param eCode 错误码
     */
    public static String bfcExceptionLog(String tag, String eCode, String detail){
        String log = TextUtils.concat(
                "BfcVersionException:", eCode, "\n",
                ErrorCodeAgent.getErrorMsg(eCode), detail, "\n",
                "LibVersion:", Build.VERSION.VERSION_NAME
        ).toString();
        logChunkInSize(Log.ERROR, tag, log, true);
        return log;
    }

    /**
     * bfc警告日志
     * <p> 有不正常的地方，当时还是能兼容使用 <p/>
     *
     * @param tag
     * @param msg
     */
    public static void bfcWLog(String tag, String msg){
        w(tag, msg);
    }

    public static void json(String tag, String msg){
        logChunkInSize(JSON, tag, msg);
    }

    public static void log(String log){
        logChunkInSize(Log.ASSERT, TAG, log, true);
    }

    private static void logChunkInSize(int method, String tag, String msg){
        logChunkInSize(method, tag, msg, false, 0);
    }

    private static void logChunkInSize(int method, String tag, String msg, boolean isThread){
        logChunkInSize(method, tag, msg, isThread, METHOD_COUNT);
    }

    private static void logChunkInSize(int method, String tag, String msg, boolean isThread, int threadMethod){
        if(!isLog){
            return;
        }

        if(!isDebugMode && method < Log.INFO){
            return;
        }

        logByMode(getBfcLogByMode(), method, tag, msg, isThread, threadMethod);
        addAllLogCache(msg);
    }

    private static void logByMode(BfcLog bfcLog, int method, String tag, String msg, boolean isThread, int threadMethod){
        switch(method){
            case Log.VERBOSE:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).v(msg);
            }
            break;
            case Log.DEBUG:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).d(msg);
            }
            break;
            case Log.INFO:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).i(msg);
            }
            break;
            case Log.WARN:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).w(msg);
            }
            break;
            case Log.ERROR:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).e(msg);
            }
            break;
            case Log.ASSERT:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).wtf(msg);
            }
            break;
            case JSON:{
                bfcLog.thread(isThread).method(threadMethod).tag(tag).json(msg);
            }
            break;
            default:{

            }
            break;
        }
    }

    /**
     * 初始化默认的打印
     */
    private static void initDefaultLog(){
        if(mBfcLogDefault != null){
            return;
        }
        mBfcLogDefault = new BfcLog.Builder()
                .tag(TAG)
                .showLog(true)
                .methodCount(METHOD_COUNT)
                .methodOffset(METHOD_OFFSET)
                .build();
    }

    /**
     * 获取debug模式保存的日志文件
     *
     * @return
     */
    public static String readDebugModeLog(){
        return mLogCache.readCurrentAllLog();
    }

    /**
     * 调试模式处理
     */
    private static BfcLog getBfcLogByMode(){
        if(mBfcLogDefault == null){
            initDefaultLog();
        }
        return mBfcLogDefault;
    }

    private LogUtils(){
        throw new AssertionError();
    }

}
