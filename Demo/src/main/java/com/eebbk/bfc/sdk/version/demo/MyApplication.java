package com.eebbk.bfc.sdk.version.demo;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.eebbk.bfc.sdk.version.demo.utils.TestUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.Settings;

/**
 * @author hesn
 * @function
 * @date 16-12-9
 * @company 步步高教育电子有限公司
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initStrictMode();
        LeakCanary.install(this, new Settings().setMonkeyTest(TestUtils.isMonkeyTest));
        BlockCanary.install(this, new AppContext());
    }

    private void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDialog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }

    public static Context getContext() {
        return context;
    }
}
