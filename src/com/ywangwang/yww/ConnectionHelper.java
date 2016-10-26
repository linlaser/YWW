package com.ywangwang.yww;

import com.ywangwang.yww.modata.MoData;
import com.ywangwang.yww.modata.MoDataPool;

public class ConnectionHelper {
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAIL = 2;
	public static final int LOGINING = 3;
	public static final int LOGIN_CONFLICT = 4;

	private static int loginStatus = 0;

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

	public static void setLoginStatus(int status) {
		loginStatus = status;
	}

}
