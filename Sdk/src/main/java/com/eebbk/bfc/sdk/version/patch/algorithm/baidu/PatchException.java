/**
 * Copyright (c) 2012 Baidu Inc.
 * 
 * @author      Qingbiao Liu <liuqingbiao@baidu.com>
 * 
 * @date 2012-11-7
 */

package com.eebbk.bfc.sdk.version.patch.algorithm.baidu;

import java.io.IOException;

/**
 * Thrown when a patch is invalid.
 */
public class PatchException extends IOException {
    /** serialVersionUID */
    private static final long serialVersionUID = 1234235436346L;

    /**
     * Creates a new instance of <code>PatchException</code> without detail message.
     */
    public PatchException() {
    }
    
    /**
     * Constructs an instance of <code>PatchException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PatchException(String msg) {
        super(msg);
    }
}
