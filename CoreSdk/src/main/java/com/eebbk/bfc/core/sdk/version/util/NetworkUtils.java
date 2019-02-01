package com.eebbk.bfc.core.sdk.version.util;

import com.eebbk.bfc.core.sdk.version.Constants;

/**
 * Desc: 网络类型解析、获取、权限判断、添加、移除帮助类
 * Author: llp
 * Create Time: 2016年5月6日 下午10:32:53
 * Email: jacklulu29@gmail.com
 */
public class NetworkUtils {
	
	/**
	 * 是否可使用Wifi网络类型，默认支持
	 * 
	 * @param networkTypes 网络类型
	 * @return true支持，false不支持
	 */
	public static boolean containsWifi(int networkTypes){
		return (networkTypes & Constants.NETWORK_WIFI) != 0;
	}
	
	/**
	 * 是否可使用移动数据网络类型
	 * 
	 * @param networkTypes 网络类型
	 * @return true支持，false不支持
	 */
	public static boolean containsMobile(int networkTypes){
		return (networkTypes & Constants.NETWORK_MOBILE) != 0;
	}

	/**
	 * 是否可使用移动数据网络2G类型
	 *
	 * @param networkTypes 网络类型
	 * @return true支持，false不支持
	 */
	public static boolean containsMobile2G(int networkTypes){
		return (networkTypes & Constants.NETWORK_MOBILE_2G) != 0;
	}
	
	/**
	 * 将多种网络类型转换叠加为一个值，方便保存和使用
	 * <pre>
	 * 网络类型：
	 * {@link Constants#NETWORK_WIFI} wifi
	 * {@link Constants#NETWORK_MOBILE} mobile
	 * {@link Constants#NETWORK_MOBILE_2G} mobile_2G
	 * 
	 * 用法：int types = convertNetworkTypes(NetworkType.NETWORK_WIFI, NetworkType.NETWORK_MOBILE);
	 * </pre>
	 * 
	 * @param networkTypes 网络类型
	 * @return 叠加后的网络类型值
	 */
	public static int convertNetworkTypes(int... networkTypes){
		int result = Constants.NETWORK_DEFAULT;
		if(networkTypes == null || networkTypes.length < 1){
			return result;
		}
		if(networkTypes.length == 1){
			result |= networkTypes[0];
		}else{
			for(int type : networkTypes){
				result |= type;
			}
		}
		return result;
	}
	
	/**
	 * add by llp 20160429 10:03
	 * 校验网络类型，默认只允许使用wifi
	 * <pre>
	 * {@link Constants#NETWORK_WIFI }
	 * {@link Constants#NETWORK_MOBILE }
	 * {@link Constants#NETWORK_MOBILE_2G }
	 * </pre>
	 * 可以叠加使用，比如: wifi+mobile的值 = {@link Constants#NETWORK_WIFI } | {@link Constants#NETWORK_MOBILE }
	 * 
	 * @param networkTypes 网络类型
	 */
	public static int checkNetworkTypes(int networkTypes) {
		// 默认只允许使用wifi
		int networkFlags = Constants.NETWORK_DEFAULT;
		if (networkTypes > 0) {
			// 如果要求使用wifi
			if ((networkTypes & Constants.NETWORK_WIFI) != 0) {
				// is default add
				networkFlags |= Constants.NETWORK_WIFI;
			}
			// 如果要求使用移动数据
			if ((networkTypes & Constants.NETWORK_MOBILE) != 0) {
				networkFlags |= Constants.NETWORK_MOBILE;
			}
			// 如果要求使用移动数据2G
			if ((networkTypes & Constants.NETWORK_MOBILE_2G) != 0) {
				networkFlags |= Constants.NETWORK_MOBILE_2G;
			}
			// to add other network
		}

		return networkFlags;
	}
	
	/**
	 * 在原来的网络类型上加上某种网络类型
	 * 
	 * <pre>
	 * {@link Constants#NETWORK_WIFI }
	 * {@link Constants#NETWORK_MOBILE }
	 * {@link Constants#NETWORK_MOBILE_2G }
	 * </pre>
	 * 
	 * @param curNetworkTypes 原来的网络类型
	 * @param newNetworkType 要加上的网络类型
	 * @return 最终的网络类型值
	 */
	public static int addNetworkType(int curNetworkTypes, int newNetworkType){
		return curNetworkTypes | newNetworkType;
	}
	
	/**
	 * 从原来的网络类型里面移除某种网络类型
	 * <pre>
	 * {@link Constants#NETWORK_WIFI }
	 * {@link Constants#NETWORK_MOBILE }
	 * {@link Constants#NETWORK_MOBILE_2G }
	 * </pre>
	 * @param curNetworkTypes 原来的网络类型
	 * @param removeNetworkType 要移除的网络类型
	 * 
	 * @return 最终的网络类型值
	 */
	public static int removeNetworkType(int curNetworkTypes, int removeNetworkType){
		return curNetworkTypes & ~removeNetworkType;
	}

}
