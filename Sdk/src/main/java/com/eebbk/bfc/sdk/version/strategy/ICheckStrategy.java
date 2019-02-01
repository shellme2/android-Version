package com.eebbk.bfc.sdk.version.strategy;

import android.support.annotation.NonNull;

import com.eebbk.bfc.sdk.version.entity.CheckParams;

import java.util.List;

import javax.security.auth.Destroyable;

/**
 * @author hesn
 * @function 版本更新检查策略
 * @date 17-2-15
 * @company 步步高教育电子有限公司
 */

public interface ICheckStrategy extends Destroyable{

    void check(@NonNull List<CheckParams> entities);

    void setRequestHead(@NonNull String machineId, @NonNull String accountId, @NonNull String apkPackageName
            , @NonNull String apkVersionCode, @NonNull String deviceModel, @NonNull String deviceOSVersion);

}
