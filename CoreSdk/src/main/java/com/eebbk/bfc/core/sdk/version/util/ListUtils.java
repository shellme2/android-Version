package com.eebbk.bfc.core.sdk.version.util;

import java.util.List;

/**
 * @author hesn
 * @function
 * @date 16-8-18
 * @company 步步高教育电子有限公司
 */

public class ListUtils {

    /**
     * 判断数组是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        return list == null || list.isEmpty();
    }

    private ListUtils(){
        //prevent the instance
    }
}
