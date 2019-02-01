package com.eebbk.bfc.sdk.version.patch.algorithm.baidu;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * This class patches an input file with a GDIFF patch file.
 * <p>
 * The patch file follows the GDIFF file specification available at .
 */
public class GDiffPatcher {
    /**
     * debug
     */
    private static final boolean DEBUG = true;
    /**
     * TAG
     */
    private static final String TAG = "GDiffPatcher";
    /**
     * chunk size
     */
    public static final int CHUNK_SIZE = Short.MAX_VALUE;
    /**
     * 1M缂撳啿鍖1024*1024
     */
    public static final int ONE_MB = 1024 * 1024;
    /**
     * 0.25M
     */
    public static final int QUARTER_MB = 262144;
    /**
     * eof is 0
     */
    public static final byte EOF = 0;
    /**
     * Max length for single length data encode.
     */
    /**
     * 0xf6
     */
    public static final int DATA_MAX = 246;
    /**
     * 0xf7
     */
    public static final int DATA_USHORT = 247;
    /**
     * 0xf8
     */
    public static final int DATA_INT = 248;
    /**
     * 0xf3
     */
    public static final int COPY_UBYTE_UBYTE = 244;
    /**
     * 0xf4
     */
    public static final int COPY_UBYTE_USHORT = 245;
    /**
     * 0xf5
     */
    public static final int COPY_UBYTE_INT = 246;
    /**
     * 0xf9
     */
    public static final int COPY_USHORT_UBYTE = 249;
    /**
     * 0xfa
     */
    public static final int COPY_USHORT_USHORT = 250;
    /**
     * 0xfb
     */
    public static final int COPY_USHORT_INT = 251;
    /**
     * 0xfc
     */
    public static final int COPY_INT_UBYTE = 252;
    /**
     * 0xfd
     */
    public static final int COPY_INT_USHORT = 253;
    /**
     * 0xfe
     */
    public static final int COPY_INT_INT = 254;
    /**
     * 0xff
     */
    public static final int COPY_LONG_INT = 255;
    /**
     * 10
     */
    public static final int DEFAULT_ZERO_MIN_BLOCK = 10;
    /**
     * 0.9d
     */
    public static final double DEFAULT_ZERO_RATIO = 0.9d;
    /**
     * 5kb constant
     */
    private static final int BUF_SIZE = 5120;
    /**
     * 1024byte buffer
     */
    private ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);
    /**
     * buf2
     */
    private byte[] buf2 = buf.array();
    /**
     * diffent
     */
    private boolean differential = false;
    /**
     * currentOffSet
     */
    private long currentOffset = 0L;
    /**
     * defualt value is DATA_MAX
     */
    private int dataMax = DATA_MAX;
    /**
     * 鏂囦欢鎬婚暱
     */
    public long totalLength = 0;
    /**
     * 鍒涘缓缂撳啿鍖猴紝鍐欏叆鏂囦欢鍓嶏紝鍏堝啓鍏ョ紦鍐插尯锛屾弧浜嗕箣鍚庯紝鍐嶄竴娆″啓鍏ワ紝鐢变簬鍐欏叆娆℃暟澶锛屼細瀵艰嚧IO缂撴參銆
     */
    private byte[] mDatas = new byte[ONE_MB];

    /**
     * 褰撳墠buffer鐨勫唴瀹瑰ぇ灏
     */
    private int datalength = 0;

    /**
     * Constructs a new GDiffPatcher.
     */
    public GDiffPatcher() {
    }

    /**
     * Patches to an output file.
     *
     * @param sourceFile sourcefile @param patchFile path
     * @param outputFile outputfile
     * @throws IOException           io exception
     * @throws FileNotFoundException FileNotFoundException
     */
    public void patch(File sourceFile, File patchFile, File outputFile) throws IOException, FileNotFoundException {
        // System.out.println("sourcefile:" + sourceFile);
        RandomAccessFileSeekableSource source = null;
        InputStream patch = null;
        OutputStream output = null;
        try {
            source = new RandomAccessFileSeekableSource(new RandomAccessFile(sourceFile, "r"));
            patch = new FileInputStream(patchFile);
            output = new FileOutputStream(outputFile, false);
            patch(source, patch, output);
        } finally {
            try {
                if (source != null) {
                    source.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (patch != null) {
                    patch.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // outputFile.length();
            // System.out.println("outputfile:" + outputFile.length());
        }
    }

    /**
     * Patches to an output stream.
     *
     * @param source source byte[]
     * @param patch  patch InputStream
     * @param output OutputStream
     * @throws IOException ioexception
     */
    public void patch(byte[] source, InputStream patch, OutputStream output) throws IOException {
        patch(new ByteBufferSeekableSource(source), patch, output);
    }

    /**
     * Patches in memory, returning the patch result.
     *
     * @param source source
     * @param patch  patch
     * @return byte[]
     * @throws IOException io exception
     */
    public byte[] patch(byte[] source, byte[] patch) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        patch(source, new ByteArrayInputStream(patch), os);
        byte[] osByteArray = os.toByteArray();
        os.close();
        return osByteArray;
    }

    /**
     * Patches to an output stream.
     *
     * @param source source SeekableSource
     * @param patch  InputStream
     * @param out    OutputStream
     * @throws IOException ioexception
     */

    public void patch(SeekableSource source, InputStream patch, OutputStream out)
            throws IOException { // SUPPRESS CHECKSTYLE
        // long time = System.currentTimeMillis();
        DataOutputStream outOS = new DataOutputStream(out);
        DataInputStream patchIS = new DataInputStream(new BufferedInputStream(patch, QUARTER_MB));

        // the magic string is 'd1 ff d1 ff' + the version number
        if (patchIS.readUnsignedByte() != 0xd1 || patchIS.readUnsignedByte() != 0xff // SUPPRESS CHECKSTYLE
                || patchIS.readUnsignedByte() != 0xd1 || patchIS.readUnsignedByte() != 0xff) { // SUPPRESS CHECKSTYLE
            throw new PatchException("magic string not found, aborting! head error!");
        }
        int flag = patchIS.readUnsignedByte();
        if (flag == 0x05) { // SUPPRESS CHECKSTYLE
            differential = true;
            dataMax = DATA_MAX - 3; // SUPPRESS CHECKSTYLE
        } else if (flag != 0x04) {// SUPPRESS CHECKSTYLE
            throw new PatchException("magic string not found, aborting! version = " + flag);
        }
        totalLength = 0;

        int length;
        long offset;
        while (true) {
            int command = patchIS.readUnsignedByte();
            if (command == EOF) {
                break;
            }

            if (command <= dataMax) {
                append(command, patchIS, outOS);
                totalLength += command;
                continue;
            }

            switch (command) {
                case DATA_USHORT: // ushort, n bytes following; append
                    length = patchIS.readUnsignedShort();
                    append(length, patchIS, outOS);
                    totalLength += length;
                    break;
                case DATA_INT: // int, n bytes following; append
                    length = patchIS.readInt();
                    append(length, patchIS, outOS);
                    totalLength += length;
                    break;
                case COPY_USHORT_UBYTE:
                    if (differential) {
                        offset = patchIS.readShort();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedShort();
                    }
                    length = patchIS.readUnsignedByte();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_USHORT_USHORT:
                    if (differential) {
                        offset = patchIS.readShort();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedShort();
                    }
                    length = patchIS.readUnsignedShort();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_USHORT_INT:
                    if (differential) {
                        offset = patchIS.readShort();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedShort();
                    }
                    length = patchIS.readInt();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_UBYTE_UBYTE:
                    if (differential) {
                        offset = patchIS.readByte();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedByte();
                    }
                    length = patchIS.readUnsignedByte();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_UBYTE_USHORT:
                    if (differential) {
                        offset = patchIS.readByte();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedByte();
                    }
                    length = patchIS.readUnsignedShort();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_UBYTE_INT:
                    if (differential) {
                        offset = patchIS.readByte();
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    } else {
                        offset = patchIS.readUnsignedByte();
                    }
                    length = patchIS.readInt();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_INT_UBYTE:
                    offset = patchIS.readInt();
                    if (differential) {
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    }
                    length = patchIS.readUnsignedByte();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_INT_USHORT:
                    offset = patchIS.readInt();
                    if (differential) {
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    }
                    length = patchIS.readUnsignedShort();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_INT_INT:
                    offset = patchIS.readInt();
                    if (differential) {
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    }
                    length = patchIS.readInt();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                case COPY_LONG_INT:
                    offset = patchIS.readLong();
                    if (differential) {
                        offset = offset + currentOffset;
                        currentOffset = offset;
                    }
                    length = patchIS.readInt();
                    copy(offset, length, source, outOS);
                    totalLength += length;
                    break;
                default:
                    throw new IllegalStateException("command " + command);
            }
        }
        // long endtime = System.currentTimeMillis();
        // System.out.println(" packapp patch time :" + (endtime - time));
        flush(outOS);
        // System.out.println(" packapp flush time:" +
        // (System.currentTimeMillis() - endtime));
    }

    /**
     * 灏唒atch涓唴瀹筩opy鍒版柊鏂囦欢涓
     *
     * @param offset offset
     * @param length length
     * @param source SeekableSource
     * @param output OutputStream @throws IOException io exception
     */
    void copy(long offset, int length, SeekableSource source, OutputStream output)
            throws IOException {
        // int l = length;
        // long time = System.currentTimeMillis();
        source.seek(offset);
        // int i = 0;
        // long readtime = time;
        // long readfinishedtime = time;
        // long cleartime = time;
        while (length > 0) {
            // i++;
            int len = Math.min(buf.capacity(), length);
            // readtime = System.currentTimeMillis();
            buf.clear().limit(len);
            // cleartime = System.currentTimeMillis();
            // System.out.println("copy clear time:" + (cleartime - readtime));
            int res = source.read(buf);
            // readfinishedtime = System.currentTimeMillis();
            // System.out.println("copy read time:" + (readfinishedtime -
            // cleartime));
            if (res == -1) {
                throw new EOFException("in copy " + offset + " " + length);
            }
            // output.write(buf.array(), 0, res);
            writeToBuffer(buf.array(), 0, res, output);
            // System.out
            // .println("copy write time:" + (System.currentTimeMillis() -
            // readfinishedtime));
            length -= res;
        }
        // System.out.println("total:" + total + "  packapp copy " + l +
        // " use time:"
        // + (System.currentTimeMillis() - time)
        // + "   copy times:" + i);
    }

    /**
     * 灏嗗埗瀹氬ぇ灏忕殑鏁版嵁鍐欏叆缂撳啿鍖恒
     *
     * @param data   鍒跺畾鐨勬暟缁勶紝鏁版嵁婧
     * @param offset 鍋忕Щ閲
     * @param length 瑕佸啓鍏ョ殑鏁版嵁闀垮害
     * @param output 杈撳嚭娴侊紝褰撶紦鍐插尯婊′簡涔嬪悗锛岄渶瑕佸啓鍑虹殑鏂囦欢娴
     */
    private void writeToBuffer(byte[] data, int offset, int length, OutputStream output) {

        try {
            if (datalength + length >= mDatas.length) {
                output.write(mDatas, 0, datalength);
                datalength = 0;
                if (length > mDatas.length) {
                    output.write(data, 0, length);
                } else {
                    System.arraycopy(data, 0, mDatas, 0, length);
                    datalength = length;
                }
                // System.out.println("data is full:" + total);
            } else {
                // error:src.length=1024 srcPos=0 dst.length=62767 dstPos=15
                // length=32763
                // System.out.println("before :" + length + "  length:" +
                // datalength);
                if (datalength == 0) {
                    System.arraycopy(data, 0, mDatas, 0, length);
                } else {
                    // error:src.length=1024 srcPos=0 dst.length=62767
                    // dstPos=1995 length=1227
                    System.arraycopy(data, 0, mDatas, datalength, length);
                }
                datalength = datalength + length;
            }
            // System.out.println("totallegnth:"+total+"  length:"+length +
            // "  datalength:" + datalength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * append 灏唒atch涓殑Data,append鏂版枃浠朵腑
     *
     * @param length length
     * @param patch  InputStream patch 杈撳叆Patch
     * @param output OutputStream
     * @throws IOException io exception
     */
    void append(int length, InputStream patch, OutputStream output) throws IOException {
        //int l = length;
        // long time = System.currentTimeMillis();
        // long readtime = time;
        // long readfinishedtime = time;
        // int i = 0;
        while (length > 0) {
            // i++;
            int len = Math.min(buf2.length, length);
            // readtime = System.currentTimeMillis();
            int res = patch.read(buf2, 0, len);
            // readfinishedtime = System.currentTimeMillis();
            // System.out.println("append read time:" + (readfinishedtime -
            // readtime));
            if (res == -1) {
                throw new EOFException("cannot read " + length);
            }
            // output.write(buf2, 0, res);
            writeToBuffer(buf2, 0, res, output);
            // System.out.println("append write time:"
            // + (System.currentTimeMillis() - readfinishedtime));
            length -= res;
        }
        // System.out.println("total:" + total + "  packapp append " + l +
        // " use time:"
        // + (System.currentTimeMillis() - time) + "   append times:" + i);
    }

    /**
     * flush
     *
     * @param os outputstream
     * @throws IOException ioexception
     */
    void flush(OutputStream os) throws IOException {
        if (datalength > 0) {
            os.write(mDatas, 0, datalength);
            datalength = 0;
        }
        os.flush();
    }

    /**
     * Simple command line tool to patch a file.
     */
    // public static void main(String argv[]) {
    // // unGZip();
    // if (argv.length != 3) {
    // // System.err.println("usage GDiffPatch source patch output");
    // // System.err.println("aborting..");
    // // return;
    // }
    // try {
    // File sourceFile = new
    // File("D:\\docs\\softwaresearch\\javaxdelta\\AppSearch_21.apk");
    // File patchFile = new
    // File("D:\\docs\\softwaresearch\\javaxdelta\\newupgradepatch");
    // File outputFile = new
    // File("D:\\docs\\softwaresearch\\javaxdelta\\output.apk");
    //
    // if (sourceFile.length() > Integer.MAX_VALUE || patchFile.length() >
    // Integer.MAX_VALUE) {
    // System.err.println("source or patch is too large, max length is "
    // + Integer.MAX_VALUE);
    // System.err.println("aborting..");
    // return;
    // }
    // GDiffPatcher patcher = new GDiffPatcher();
    // patcher.patch(sourceFile, patchFile, outputFile);
    //
    // System.out.println("finished patching file");
    //
    // } catch (Exception ioe) { // gls031504a
    // System.err.println("error while patching: " + ioe);
    // ioe.printStackTrace();
    // }
    // }

}