package com.eebbk.bfc.sdk.version.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.eebbk.bfc.common.file.FileUtils;
import com.eebbk.bfc.core.sdk.version.util.log.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 类说明：  	Md5工具类
 *
 */
public class Md5Utils {

    private Md5Utils(){

    }

	/**
	 * byte转十六进制
	 *
	 * @param src
	 * @return
     */
	private static String bytes2Hex(byte[] src) {
		char[] res = new char[src.length * 2];
		final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		for (int i = 0, j = 0; i < src.length; i++) {
			res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
			res[j++] = hexDigits[src[i] & 0x0f];
		}

		return new String(res);
	}

	/**
	 * 获取文件md5
	 *
	 * @param file
	 * @return
     */
	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);

			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
			int byteCount;
			while ((byteCount = in.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			value = bytes2Hex(digester.digest());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeIO(in);
		}
		return value;
	}

	/**
	 * 文件md5校验，判断文件的MD5是否为指定值
	 *
	 * @param file
	 * @param md5
	 * @return
	 */
	public static boolean checkMd5(File file, String md5) {
		if (TextUtils.isEmpty(md5)) {
			LogUtils.w("md5 cannot be empty");
			return false;
		}

		String fileMd5 = getMd5ByFile(file);

		LogUtils.d(String.format("file's md5=%s, real md5=%s", fileMd5, md5));

		return md5.equals(fileMd5);
	}

	/**
	 * 判断文件的MD5是否为指定值
	 *
	 * @param filePath
	 * @param md5
	 * @return
	 */
	public static boolean checkMd5(String filePath, String md5) {
		return checkMd5(new File(filePath), md5);
	}

	/**
	 * 获取百度计算后的签名md5值
	 *
	 * @param context
	 * @return
	 */
	@SuppressLint("PackageManagerGetSignatures")
	public static String getBaiduMd5(Context context, String packageName) {
		PackageInfo packageinfo;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
		} catch (PackageManager.NameNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		if (packageinfo == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(packageinfo.signatures[0].toCharsString().getBytes());
			byte[] b = md.digest();
			char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
					'c', 'd', 'e', 'f'};
			StringBuilder sb = new StringBuilder(b.length * 2);
			for (byte aB : b) {
				sb.append(HEXCHAR[(aB & 0xf0) >>> 4]);
				sb.append(HEXCHAR[aB & 0x0f]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 获取豌豆荚计算后的签名md5值
	 *
	 * @param context
	 * @return
	 */
	@SuppressLint("PackageManagerGetSignatures")
	public static String getWanDouJiaMd5(Context context, String packageName) {
		PackageInfo packageinfo;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
		} catch (PackageManager.NameNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		if (packageinfo == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] hex = packageinfo.signatures[0].toByteArray();
			if (hex == null) {
				return null;
			}
			byte[] HEX_BYTES = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68,
					69, 70};
			int i1;
			int i2;
			byte[] byteBuffers = new byte[2 * hex.length];
			for (int i = 0; i < hex.length; ++i) {
				i1 = (hex[i] & 0xf0) >> 4;
				byteBuffers[2 * i] = HEX_BYTES[i1];
				i2 = hex[i] & 0xf;
				byteBuffers[2 * i + 1] = HEX_BYTES[i2];
			}

			byte[] md5Bytes = messageDigest.digest(byteBuffers);
			StringBuilder hexValue = new StringBuilder();
			for (byte md5Byte : md5Bytes) {
				int val = ((int) md5Byte) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}