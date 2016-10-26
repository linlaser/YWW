package com.ywangwang.yww.send;

import com.ywangwang.yww.Client;
import com.ywangwang.yww.GlobalInfo;
import com.ywangwang.yww.MoMessage;
import com.ywangwang.yww.TcpService;
import com.ywangwang.yww.User;
import com.ywangwang.yww.lib.SessionKey;
import com.ywangwang.yww.net.Operaton;
import com.ywangwang.yww.net.SocketOperaton;
import com.ywangwang.yww.net.TcpManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataSendManager {
	private static final String TAG = "DataSendManager";

	private static final int TIMEOUT = -1;

	private static final int LOGIN_SUCCESS = 1;
	private static final int LOGIN_FAIL = 2;
	private static final int REGISTER_SUCCESS = 3;
	private static final int REGISTER_FAIL = 4;
	private static final int GET_DEVICE_LIST_SUCCESS = 5;
	private static final int GET_DEVICE_LIST_FAIL = 6;

	private Context context;
	private static DataSendManager dataSendManager = null;

	protected LoginCallBack loginCallBack;
	private SessionKey loginSessionKey = new SessionKey();

	public static DataSendManager getInstance() {
		if (null == dataSendManager) {
			dataSendManager = new DataSendManager();
		}
		return dataSendManager;
	}

	public void init(Context context) {
		this.context = context;
		context.registerReceiver(broadcastReceiver, new IntentFilter(GlobalInfo.BROADCAST_DATA_SEND_ACTION));
	}

	public void destroy() {
		loginCallBack = null;
		context.unregisterReceiver(broadcastReceiver);
	}

	public void setLoginListener(LoginCallBack c) {
		loginCallBack = c;
	}

	public void removeLoginListener() {
		loginCallBack = null;
	}

	public void login(String username, String password) {
		Message msg = Message.obtain();
		msg.arg1 = loginSessionKey.generateNewSessionKey();
		int result = SocketOperaton.login(username, password, msg.arg1);
		if (result == msg.arg1) {
			msg.what = LOGIN_FAIL;
			msg.arg2 = TIMEOUT;
			msg.obj = "登录超时";
			loginHandler.sendMessageDelayed(msg, 10 * 1000L);
		} else {
			msg.what = LOGIN_FAIL;
			msg.obj = "登录信息发送失败";
			loginHandler.sendMessage(msg);
		}
	}

	Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 如果会话KEY和最后一次不同，就忽略此msg
			if (loginSessionKey.getSessionKey() != msg.arg1) {
				return;
			}
			loginSessionKey.cleanSessionKey();
			switch (msg.what) {
			case LOGIN_SUCCESS:
				loginHandler.removeMessages(LOGIN_FAIL);
				if (loginCallBack != null) {
					loginCallBack.onSuccess(((User) msg.obj).username, ((User) msg.obj).password);
				}
				break;
			case LOGIN_FAIL:
				if (loginCallBack != null) {
					loginCallBack.onError(LOGIN_FAIL, (String) msg.obj);
				}
				break;
			default:
				break;
			}
		}
	};
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras().getString(GlobalInfo.BROADCAST_RECEIVE_NEW_MESSAGE, null) != null) {
				MoMessage moMsg = MoMessage.analyzeJsonData(intent.getExtras().getString(GlobalInfo.BROADCAST_RECEIVE_NEW_MESSAGE));
				if (moMsg == null) {
					return;
				}
				Log.d(TAG, moMsg.toString());
				// toast.setText(moMsg.toString()).show();
				if (moMsg.cmd == MoMessage.LOGIN_KEY_ERR) {
					// Message newMsg = Message.obtain();
					// newMsg.arg1 = sessionKey.getSessionKey();
					// newMsg.what = MoMessage.LOGIN_KEY_ERR;
					// newMsg.obj = moMsg.info;
					// handler.sendMessage(newMsg);
				} else if (moMsg.cmd == MoMessage.LOGIN_SUCCESS) {
					User user = User.analyzeJsonData(moMsg.jsonData);
					Message newMsg = Message.obtain();
					newMsg.arg1 = moMsg.sessionKey;
					if (user != null) {
						newMsg.what = LOGIN_SUCCESS;
						GlobalInfo.id = moMsg.id;
						newMsg.obj = user;
					} else {
						newMsg.what = LOGIN_FAIL;
						newMsg.obj = "数据解析失败";
					}
					loginHandler.sendMessage(newMsg);
				} else if (moMsg.cmd == MoMessage.LOGIN_FAIL) {
					Message newMsg = Message.obtain();
					newMsg.arg1 = moMsg.sessionKey;
					newMsg.what = LOGIN_FAIL;
					newMsg.obj = moMsg.info;
					loginHandler.sendMessage(newMsg);
				} else if (moMsg.cmd == MoMessage.GET_GXJ_SUCCESS) {
					Client client = Client.analyzeJsonData(moMsg.jsonData);
					Message newMsg = Message.obtain();
					newMsg.arg1 = moMsg.sessionKey;
					if (client != null) {
						newMsg.what = GET_DEVICE_LIST_SUCCESS;
						newMsg.obj = client;
					} else {
						newMsg.what = GET_DEVICE_LIST_FAIL;
						newMsg.obj = "数据解析失败";
					}
					// handler.sendMessage(newMsg);
				} else if (moMsg.cmd == MoMessage.GET_GXJ_FAIL) {
					Message newMsg = Message.obtain();
					newMsg.arg1 = moMsg.sessionKey;
					newMsg.what = GET_DEVICE_LIST_FAIL;
					newMsg.obj = moMsg.info;
					// handler.sendMessage(newMsg);
				}
			} else if (intent.getExtras().getBoolean(GlobalInfo.BROADCAST_CONNECT_SOCKET_SUCCESS, false) == true) {
				GlobalInfo.unableConnectToServer = false;
				context.sendBroadcast(new Intent(GlobalInfo.BROADCAST_ACTION).putExtra(GlobalInfo.BROADCAST_UPDATE_CONNECT_STATUS, true));
				Log.d(TAG, "BROADCAST_CONNECT_SOCKET_SUCCESS");
				if (GlobalInfo.online == false && GlobalInfo.manualLogout == false && GlobalInfo.password.length() > 0 && GlobalInfo.autoLogin == true) {
					// AutoLogin
					Log.d(TAG, "AutoLogin");
					login(GlobalInfo.username, GlobalInfo.password);
				}
			}
		}
	};
}
