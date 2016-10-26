package com.ywangwang.yww.net;

import java.net.NetworkInterface;
import java.net.SocketException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Net {

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService("connectivity");
		NetworkInfo _networkInfo = connectivityMgr.getActiveNetworkInfo();
		if (_networkInfo == null || !_networkInfo.isAvailable() || !_networkInfo.isConnected()) {
			return false;
		} else {
			return true;
		}
	}

	public static String getMacAddress(Context context) {
		String macAddress = null;
		StringBuffer buf = new StringBuffer();
		NetworkInterface networkInterface = null;
		try {
			networkInterface = NetworkInterface.getByName("wlan0");
			if (networkInterface == null) {
				return "02:00:00:00:00:02";
			}
			byte[] addr = networkInterface.getHardwareAddress();

			for (byte b : addr) {
				buf.append(String.format("%02X:", b));
			}
			if (buf.length() > 0) {
				buf.deleteCharAt(buf.length() - 1);
			}
			macAddress = buf.toString();
		} catch (SocketException e) {
			e.printStackTrace();
			return "02:00:00:00:00:02";
		}
		return macAddress;
	}
}
