package com.ywangwang.yww.net;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.ywangwang.yww.GlobalInfo;
import com.ywangwang.yww.MoMessage;
import com.ywangwang.yww.User;

public class SocketOperaton {

	public SocketOperaton() {
	}

	public static int login(String username, String password, int sessionKey) {
		MoMessage moMsg = new MoMessage();
		moMsg.sessionKey = sessionKey;
		moMsg.cmd = MoMessage.LOGIN;
		moMsg.id = GlobalInfo.id;
		moMsg.loginKey = GlobalInfo.loginKey = new Random().nextInt(100000) + 1;
		User user = new User(username, password);
		try {
			moMsg.jsonData = new JSONObject(user.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (TcpManager.sendMSG(moMsg.toString())) {
			return moMsg.sessionKey;
		}
		return -1;
	}

	public static int logout(int sessionKey) {
		MoMessage moMsg = new MoMessage();
		moMsg.sessionKey = sessionKey;
		moMsg.cmd = MoMessage.LOGOUT;
		moMsg.id = GlobalInfo.id;
		moMsg.loginKey = GlobalInfo.loginKey;
		if (TcpManager.sendMSG(moMsg.toString())) {
			TcpManager.reconnect();
			return moMsg.sessionKey;
		}
		return -1;
	}

	public static int getGxj(String username, String password, int sessionKey) {
		MoMessage moMsg = new MoMessage();
		moMsg.sessionKey = sessionKey;
		moMsg.cmd = MoMessage.GET_GXJ;
		moMsg.id = GlobalInfo.id;
		moMsg.loginKey = GlobalInfo.loginKey;
		User user = new User(username, password);
		try {
			moMsg.jsonData = new JSONObject(user.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (TcpManager.sendMSG(moMsg.toString())) {
			return moMsg.sessionKey;
		}
		return -1;
	}

}
