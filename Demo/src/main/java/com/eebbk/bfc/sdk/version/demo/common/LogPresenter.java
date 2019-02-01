package com.eebbk.bfc.sdk.version.demo.common;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.core.sdk.version.util.log.OnLogListener;

import java.lang.ref.WeakReference;


/**
 * @author hesn
 * @function 显示打印log
 * @date 16-10-24
 * @company 步步高教育电子有限公司
 */

public class LogPresenter implements OnLogListener {

    private TextView mLogTv;
    private static final int PADDING = 5;
    private MyHandler mHandler;

    static class MyHandler extends Handler {
        WeakReference<TextView> mTv;

        MyHandler(TextView logTv) {
            mTv = new WeakReference(logTv);
        }

        @Override
        public void handleMessage(Message msg) {
            TextView logTv = mTv.get();
            if (logTv != null) {
                logTv.setText(String.valueOf(msg.obj));
            }
        }
    }

    @Override
    public void onLogRefresh(String log) {
        if (mLogTv != null && mHandler != null) {
            Message message = Message.obtain();
            message.obj = log;
            mHandler.sendMessage(message);
        }
    }

    public LogPresenter(Activity activity) {
        initTv(activity);
    }

    /**
     * 开始刷新监听日志
     */
    public void startRefreshLog() {
        LogUtils.setListener(this);
    }

    /**
     * 停止刷新监听日志
     */
    public void stopRefreshLog() {
        LogUtils.cancelListener();
    }

    private void initTv(Activity activity) {
        mLogTv = new TextView(activity);
        mLogTv.setBackgroundColor(Color.BLACK);
        mLogTv.setTextColor(Color.WHITE);
        mLogTv.setPadding(PADDING, PADDING, PADDING, PADDING);
        View view = getRootView(activity);
        ViewGroup viewGroup;
        if (view instanceof ScrollView) {
            //父容器是 ScrollView 则添加到其子类view中
            ScrollView rootView = (ScrollView) getRootView(activity);
            viewGroup = (ViewGroup) rootView.getChildAt(0);
        } else {
            //添加滚动效果
            mLogTv.setMovementMethod(new ScrollingMovementMethod());
            viewGroup = (ViewGroup) view;
        }
        viewGroup.addView(mLogTv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        mHandler = new MyHandler(mLogTv);
    }

    private View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

}
