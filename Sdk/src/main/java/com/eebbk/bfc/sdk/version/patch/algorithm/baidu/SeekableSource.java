package com.eebbk.bfc.sdk.version.patch.algorithm.baidu;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * For sources of random-access data.
 */
public interface SeekableSource extends Closeable {

    /**
     * Sets the position for the next {@link #read(ByteBuffer)}.
     * 
     * @param pos
     *            position
     * @throws IOException
     *             io exception
     */
    void seek(long pos) throws IOException;

    /**
     * Reads up to {@link ByteBuffer#remaining()} bytes from the source,
     * returning the number of bytes read, or -1 if no bytes were read and EOF
     * was reached.
     * 
     * @param bb
     *            bytebuffer
     * @throws IOException
     *             io exception
     * @return read size
     */
    int read(ByteBuffer bb) throws IOException;

}
