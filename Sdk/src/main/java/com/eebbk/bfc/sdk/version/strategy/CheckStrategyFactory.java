package com.eebbk.bfc.sdk.version.strategy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eebbk.bfc.core.sdk.version.BfcCoreVersion;
import com.eebbk.bfc.sdk.version.BfcVersion;
import com.eebbk.bfc.sdk.version.Settings;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.download.DownLoadController;
import com.eebbk.bfc.sdk.version.listener.OnCheckStrategyListener;

/**
 * @author hesn
 * @function
 * @date 17-1-12
 * @company 步步高教育电子有限公司
 */

public class CheckStrategyFactory {

    public static ICheckStrategy create(int type,@NonNull Context context, @NonNull BfcVersion bfcVersion
            , @NonNull BfcCoreVersion bfcCoreVersion, @NonNull Settings settings
            , @NonNull DownLoadController downLoadController, @NonNull OnCheckStrategyListener l) {
        switch (type) {
            case VersionConstants.CHECK_STRATEGY_LOCAL_FIRST:
                // 本地已有下载的版本优先
                return new LocalCheckStrategyImpl(context, bfcVersion, bfcCoreVersion, settings
                        , downLoadController, l);
            case VersionConstants.CHECK_STRATEGY_REMOTE_FIRST:
                // 服务器远端版本为准
                return new RemoteCheckStrategyImpl(context, bfcVersion, bfcCoreVersion, settings
                        , downLoadController, l);
            default:
                return new LocalCheckStrategyImpl(context, bfcVersion, bfcCoreVersion, settings
                        , downLoadController, l);
        }
    }

}
