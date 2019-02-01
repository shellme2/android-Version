package com.eebbk.bfc.sdk.version.patch.algorithm.baidu;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Wraps a random access file.
 */
public class RandomAccessFileSeekableSource implements SeekableSource {
    /**
     * RandomAccessFile，用于动态读写文件
     */
    private RandomAccessFile mRandomAccessFile;

    /**
     * Constructs a new RandomAccessFileSeekableSource.
     * 
     * @param raf
     *            RandomAccessFile
     */
    public RandomAccessFileSeekableSource(RandomAccessFile raf) {
        if (raf == null) {
            throw new NullPointerException("input RandomAccessFile is null");
        }
        this.mRandomAccessFile = raf;
    }

    @Override
    public void seek(long pos) throws IOException {
        mRandomAccessFile.seek(pos);
    }

    /**
     * 
     * Reads at most byteCount bytes from the current position in this file and
     * stores them in the byte array buffer starting at byteOffset. Blocks until
     * at least one byte has been read, the end of the file is detected, or an
     * exception is thrown.
     * 
     * @param buffer
     *            用于存储读取到的文件内容
     * @param off
     *            offset
     * @param len
     *            length
     * 
     * @return the number of bytes actually read or -1 if the end of the stream
     *         has been reached.
     * 
     * @throws IOException
     *             ioexception
     */

    public int read(byte[] buffer, int off, int len) throws IOException {
        return mRandomAccessFile.read(buffer, off, len);
    }

    /**
     * Returns the length of this file in bytes.
     * 
     * @return the file's length in bytes.
     * 
     * @throws IOException
     *             ioexception
     */
    public long length() throws IOException {
        return mRandomAccessFile.length();
    }

    /**
     * close
     * 
     * @throws IOException
     *             io
     */
    public void close() throws IOException {
        mRandomAccessFile.close();
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
        int count = mRandomAccessFile.read(bb.array(), bb.position(), bb.remaining());
        if (count == -1) {
            return -1;
        }
        bb.position(bb.position() + count);
        return count;
    }

}
