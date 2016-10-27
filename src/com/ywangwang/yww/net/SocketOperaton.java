package com.ywangwang.yww.net;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.ywangwang.yww.GlobalInfo;
//import com.ywangwang.yww.MoData;
import com.ywangwang.yww.User;
import com.ywangwang.yww.modata.MoData;

public class SocketOperaton {

	public SocketOperaton() {
	}

	public static int login(User user, int sessionKey) {
		MoData moData = new MoData();
		moData.setSessionKey(sessionKey);
		moData.setCmd(MoData.LOGIN);
		moData.setId(GlobalInfo.id);
		moData.setLoginKey(GlobalInfo.loginKey = new Random().nextInt(100000) + 1);
		try {
			moData.setJsonData(new JSONObject(user.toString()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (TcpManager.sendMSG(moData.toString())) {
			return moData.getSessionKey();
		}
		return -1;
	}

	public static int logout(int sessionKey) {
		MoData moData = new MoData();
		moData.setSessionKey(sessionKey);
		moData.setCmd(MoData.LOGOUT);
		moData.setId(GlobalInfo.id);
		moData.setLoginKey(GlobalInfo.loginKey);
		if (TcpManager.sendMSG(moData.toString())) {
			return moData.getSessionKey();
		}
		return -1;
	}

	public static int getGxj(String username, String password, int sessionKey) {
		MoData moData = new MoData();
		moData.setSessionKey(sessionKey);
		moData.setCmd(MoData.GET_GXJ);
		moData.setId(GlobalInfo.id);
		moData.setLoginKey(GlobalInfo.loginKey);
		User user = new User(username, password);
		try {
			moData.setJsonData(new JSONObject(user.toString()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (TcpManager.sendMSG(moData.toString())) {
			return moData.getSessionKey();
		}
		return -1;
	}

}
