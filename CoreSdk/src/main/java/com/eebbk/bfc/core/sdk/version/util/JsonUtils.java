package com.eebbk.bfc.core.sdk.version.util;

import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;
import com.eebbk.bfc.sequence.SequenceTools;

/**
 * Json工具类，数据解析
 * Created by lcg on 16-4-10.
 */
class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static String toJson(Object obj) {
        String jsonString = "";
        try {
            jsonString = SequenceTools.serialize(obj);
        } catch (Exception e) {
            LogUtils.w(TAG," Can't change the Entity to Json");
            e.printStackTrace();
        }
        LogUtils.d(TAG," Parse Entity to Json :" + jsonString);
        return jsonString;
    }

    public static <T> T fromJson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = SequenceTools.deserialize(jsonString, cls);
        } catch (Exception e) {
            LogUtils.w(TAG," Can't change the Json to Entity");
            e.printStackTrace();
        }
        return t;
    }

    private JsonUtils(){

    }
}
