/**
 * Copyright (c) 2012 Baidu Inc.
 * 
 */
package com.eebbk.bfc.sdk.version.patch.algorithm.baidu;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Wraps a byte buffer as a source
 */
public class ByteBufferSeekableSource implements SeekableSource {
    /** bb */
    private ByteBuffer mByteBuffer;

    // private ByteBuffer cur;

    /**
     * Constructs a new ByteArraySeekableSource.
     * 
     * @param source
     *            输入的字节
     */
    public ByteBufferSeekableSource(byte[] source) {
        this(ByteBuffer.wrap(source));
    }

    /**
     * Constructs a new ByteArraySeekableSource.
     * 
     * @param bb
     *            ByteBuffer
     */
    private ByteBufferSeekableSource(ByteBuffer bb) {
        if (bb == null) {
            throw new NullPointerException("input parameter bb is null");
        }
        this.mByteBuffer = bb;
        bb.rewind();
        try {
            seek(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 跳转到指定位置
     * 
     * @param pos
     *            position
     * @throws IOException
     *             io exception
     */
    public void seek(long pos) throws IOException {
        // cur = bb.slice();
        if (pos > mByteBuffer.limit()) {
            throw new IOException("pos " + pos + " cannot seek " + mByteBuffer.limit());
        }
        mByteBuffer.position((int) pos);
    }

    /**
     * 读取buffer中的数据到dest
     * 
     * @param dest
     *            要输出的 dest
     * @throws IOException
     *             io exception
     * @return 读取的ByteBuffer size
     */
    public int read(ByteBuffer dest) throws IOException {
        if (!mByteBuffer.hasRemaining()) {
            return -1;
        }
        int count = 0;
        while (mByteBuffer.hasRemaining() && dest.hasRemaining()) {
            dest.put(mByteBuffer.get());
            count++;
        }
        return count;
    }

    /**
     * close
     * 
     * @throws IOException
     *             io exception
     */
    public void close() throws IOException {
        mByteBuffer = null;
        // cur = null;
    }

    /**
     * Returns a debug <code>String</code>.
     * 
     * @return toString value
     */
    @Override
    public String toString() {
        return "BBSeekable" + " bb=" + this.mByteBuffer.position() + "-" + mByteBuffer.limit();
    }

}
