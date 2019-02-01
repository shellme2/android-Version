package com.eebbk.bfc.sdk.version.util;

import android.os.Environment;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.util.NetworkUtils;
import com.eebbk.bfc.sdk.download.net.NetworkType;
import com.eebbk.bfc.sdk.version.VersionConstants;
import com.eebbk.bfc.sdk.version.entity.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

public class Utils {

    /**
     * 计算指定路径下的剩余空间
     *
     * @param path 指定路径
     * @return 空间大小B
     */
    public static long calculateSpace(String path) {
        File file = new File(path);
        FileUtils.createDirOrExists(file);
        return FileUtils.getFileAvaiableSize(file);
    }

    private static String createDirectoryFromSD(Version remoteVersion, String folderName) {
        if (Utils.calculateSpace(VersionConstants.PATH_SD) >= remoteVersion.getFileSize()) {
            return new File(VersionConstants.PATH_SD + folderName).getAbsolutePath();
        } else {
            return null;
        }
    }

    private static String creatorDirectory(Version remoteVersion, String folderName) {
        //SD卡未被挂起，优先选择sd目录
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = createDirectoryFromSD(remoteVersion, folderName);
            if (path != null) {
                return path;
            }
        }

        if (Utils.calculateSpace(VersionConstants.PATH_B) >= remoteVersion.getFileSize()) {

            return new File(VersionConstants.PATH_B + folderName).getAbsolutePath();
        }

        return null;
    }

    /**
     * 创建下载保存文件夹路径
     *
     * @param remoteVersion 版本信息
     * @return 下载文件夹路径
     */
    public static String creatorDownloadDirectory(Version remoteVersion) {
        String folderName = remoteVersion.getSavePatchFolderName();
        return creatorDirectory(remoteVersion, folderName);
    }

    /**
     * 获取差分合并，生成新apk文件夹保存路径
     *
     * @param remoteVersion 版本信息
     * @return 新合成APK路径
     */
    public static String creatorNewApkDirectory(Version remoteVersion) {
        return creatorDirectory(remoteVersion, VersionConstants.NEW_APK_FOLDER);
    }

    /**
     * 复制或移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @param isMove   是否移动
     * @return {@code true}: 复制或移动成功<br>{@code false}: 复制或移动失败
     */
    public static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        // 源文件不存在或者不是文件则返回false
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        // 目标目录不存在返回false
        if (!FileUtils.createDirOrExists(destFile.getParentFile())) return false;
        FileUtils.deleteFile(destFile);
        try {
            return FileUtils.writeFile(destFile, new FileInputStream(srcFile), false)
                    && !(isMove && !FileUtils.deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 版本更新库转下载库网络类型
     *
     * @param versionNetworkType
     * @return
     */
    public static int versionNetType2DownloadNetType(int versionNetworkType) {
        int downloadNetworkType = 0 ;
        if (NetworkUtils.containsWifi(versionNetworkType)) {
            downloadNetworkType = NetworkUtils.addNetworkType(downloadNetworkType, NetworkType.NETWORK_WIFI);
        }

        if (NetworkUtils.containsMobile(versionNetworkType)) {
            downloadNetworkType = NetworkUtils.addNetworkType(downloadNetworkType, NetworkType.NETWORK_MOBILE);
        }
        return downloadNetworkType;
    }

    /**
     * 销毁
     *
     * @param destroyable
     */
    public static void destroy(Destroyable destroyable) {
        if (destroyable == null) {
            return;
        }
        try {
            destroyable.destroy();
        } catch (DestroyFailedException e) {
            e.printStackTrace();
        }
    }

    private Utils() {

    }
}
