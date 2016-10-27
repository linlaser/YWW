package com.ywangwang.yww;

import com.ywangwang.yww.modata.MoData;
import com.ywangwang.yww.modata.MoDataPool;
import com.ywangwang.yww.net.TcpManager;

public class ConnectionHelper {

	public static final int NOT_LOGIN = 0;
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAIL = 2;
	public static final int LOGINING = 3;
	public static final int LOGIN_CONFLICT = 4;

	public static final int NOT_CONNECT = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECT_SUCCESS = 2;
	public static final int CONNECT_FAIL = 3;

	private static int loginStatus = 0;
	private static int connectionStatus = 0;
	private static boolean alreadyShowLoginActivity = false;

	public static void addSendData(MoData moData) {
		MoDataPool.getInstance().addMoData(moData);
	}

	public static MoData getNextSendData() {
		return MoDataPool.getInstance().getNextMoData();
	}

	public static void clearSendDataPool() {
		MoDataPool.getInstance().clearPool();
	}

	public static int getLoginStatus() {
		return loginStatus;
	}

	public static void setLoginStatus(int loginStatus) {
		ConnectionHelper.loginStatus = loginStatus;
	}

	public static int getConnectionStatus() {
		return connectionStatus;
	}

	public static void setConnectionStatus(int connectionStatus) {
		ConnectionHelper.connectionStatus = connectionStatus;
	}

	public static boolean isAlreadyShowLoginActivity() {
		return alreadyShowLoginActivity;
	}

	public static void setAlreadyShowLoginActivity(boolean alreadyShowLoginActivity) {
		ConnectionHelper.alreadyShowLoginActivity = alreadyShowLoginActivity;
	}

	public static void switchServer(String address) {
		loginStatus = NOT_LOGIN;
		connectionStatus = NOT_CONNECT;
		TcpManager.setServerAddress(address);
		TcpManager.reconnect();
	}

	public static void reConnect() {
		loginStatus = NOT_LOGIN;
		connectionStatus = NOT_CONNECT;
		TcpManager.reconnect();
	}
}
