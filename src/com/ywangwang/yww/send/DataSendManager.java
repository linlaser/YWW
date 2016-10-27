package com.ywangwang.yww.send;

import com.ywangwang.yww.Client;
import com.ywangwang.yww.ConnectionHelper;
import com.ywangwang.yww.GlobalInfo;
import com.ywangwang.yww.LoginActivity;
import com.ywangwang.yww.TcpService;
import com.ywangwang.yww.User;
import com.ywangwang.yww.lib.SessionKey;
import com.ywangwang.yww.modata.MoData;
import com.ywangwang.yww.net.SocketOperaton;
import com.ywangwang.yww.send.CallBack.GetDeviceListCallBack;
import com.ywangwang.yww.send.CallBack.LoginCallBack;

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
	private static final int DELAY = -2;

	public static final int NOT_LOGIN = ConnectionHelper.NOT_LOGIN;
	public static final int LOGIN_SUCCESS = ConnectionHelper.LOGIN_SUCCESS;
	public static final int LOGIN_FAIL = ConnectionHelper.LOGIN_FAIL;
	public static final int LOGINING = ConnectionHelper.LOGINING;
	public static final int LOGIN_CONFLICT = ConnectionHelper.LOGIN_CONFLICT;

	private static final int GET_DEVICE_LIST_SUCCESS = 6;
	private static final int GET_DEVICE_LIST_FAIL = 7;

	private Context context;
	private static DataSendManager dataSendManager = null;

	protected LoginCallBack loginCallBack;
	private SessionKey loginSessionKey = new SessionKey();
	protected GetDeviceListCallBack getDeviceListCallBack;
	private SessionKey getDeviceListSessionKey = new SessionKey();

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
		getDeviceListCallBack = null;
		context.unregisterReceiver(broadcastReceiver);
		loginHandler.removeCallbacksAndMessages(null);
		getDeviceListHandler.removeCallbacksAndMessages(null);
	}

	public void setLoginCallBack(LoginCallBack c) {
		loginCallBack = c;
	}

	public void removeLoginCallBack() {
		loginCallBack = null;
	}

	public void setGetDeviceListCallBack(GetDeviceListCallBack c) {
		getDeviceListCallBack = c;
	}

	public void removeGetDeviceListCallBack() {
		getDeviceListCallBack = null;
	}

	public void logout() {
		ConnectionHelper.setLoginStatus(ConnectionHelper.NOT_LOGIN);
		SocketOperaton.logout(0);
		ConnectionHelper.reConnect();
		if (ConnectionHelper.isAlreadyShowLoginActivity() == false) {
			context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			ConnectionHelper.setAlreadyShowLoginActivity(true);
		}
	}

	private void delay(Handler handler, Message msg, int count, Object obj) {
		if (count == 0) {
			count = 6;
		}
		msg.what = DELAY;
		msg.arg2 = --count;
		msg.obj = obj;
		handler.sendMessageDelayed(msg, 2 * 1000L);
	}

	public void login(String username, String password) {
		User user = new User(username, password);
		login(user, 0);
	}

	public void login(User user, int count) {
		Message msg = Message.obtain();
		msg.arg1 = loginSessionKey.generateNewSessionKey();
		ConnectionHelper.setLoginStatus(ConnectionHelper.LOGINING);
		if (ConnectionHelper.getConnectionStatus() != ConnectionHelper.CONNECT_SUCCESS) {
			delay(loginHandler, msg, count, user);
			return;
		}
		int result = SocketOperaton.login(user, msg.arg1);
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
			loginHandler.removeCallbacksAndMessages(null);
			switch (msg.what) {
			case LOGIN_SUCCESS:
				TcpService.toast.setText("登录成功！").show();
				if (loginCallBack != null) {
					loginCallBack.onSuccess(((User) msg.obj).username, ((User) msg.obj).password);
				}
				ConnectionHelper.setLoginStatus(ConnectionHelper.LOGIN_SUCCESS);
				break;
			case LOGIN_FAIL:
				TcpService.toast.setText("登录失败！").show();
				if (loginCallBack != null) {
					loginCallBack.onError(LOGIN_FAIL, (String) msg.obj);
				}
				ConnectionHelper.setLoginStatus(ConnectionHelper.LOGIN_FAIL);
				if (msg.arg2 == TIMEOUT) {
					ConnectionHelper.reConnect();
				}
				break;
			case LOGIN_CONFLICT:
				TcpService.toast.setText("账号在其他设备登录！").show();
				ConnectionHelper.setLoginStatus(ConnectionHelper.LOGIN_CONFLICT);
				logout();
				break;
			case DELAY:
				Log.e(TAG, "loginHandler.DELAY,count=" + msg.arg2);
				if (msg.arg2 > 0) {
					login((User) msg.obj, msg.arg2);
				} else {
					TcpService.toast.setText("登录失败！").show();
					if (loginCallBack != null) {
						loginCallBack.onError(LOGIN_FAIL, "登录超时！");
					}
					ConnectionHelper.setLoginStatus(ConnectionHelper.LOGIN_FAIL);
					ConnectionHelper.reConnect();
				}
				break;
			default:
				break;
			}
		}
	};

	public void getDeviceList() {
		getDeviceList(0);
	}

	public void getDeviceList(int count) {
		Message msg2 = Message.obtain();
		msg2.arg1 = getDeviceListSessionKey.generateNewSessionKey();

		if (ConnectionHelper.getLoginStatus() != ConnectionHelper.LOGIN_SUCCESS) {
			delay(getDeviceListHandler, msg2, count, null);
			return;
		}

		int result2 = SocketOperaton.getGxj(GlobalInfo.username, GlobalInfo.password, msg2.arg1);
		if (result2 == msg2.arg1) {
			msg2.what = GET_DEVICE_LIST_FAIL;
			msg2.arg2 = TIMEOUT;
			msg2.obj = "获取超时";
			getDeviceListHandler.sendMessageDelayed(msg2, 10 * 1000L);
		} else {
			msg2.what = GET_DEVICE_LIST_FAIL;
			msg2.obj = "获取信息发送失败";
			getDeviceListHandler.sendMessage(msg2);
		}
	}

	Handler getDeviceListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 如果会话KEY和最后一次不同，就忽略此msg
			if (getDeviceListSessionKey.getSessionKey() != msg.arg1) {
				return;
			}
			getDeviceListSessionKey.cleanSessionKey();
			getDeviceListHandler.removeCallbacksAndMessages(null);
			switch (msg.what) {
			case GET_DEVICE_LIST_SUCCESS:
				if (getDeviceListCallBack != null) {
					getDeviceListCallBack.onSuccess((Client) msg.obj);
				}
				break;
			case GET_DEVICE_LIST_FAIL:
				if (getDeviceListCallBack != null) {
					getDeviceListCallBack.onError(LOGIN_FAIL, (String) msg.obj);
				}
				break;
			case DELAY:
				Log.e(TAG, "getDeviceListHandler.DELAY,count=" + msg.arg2);
				if (msg.arg2 > 0) {
					getDeviceList(msg.arg2);
				} else {
					if (getDeviceListCallBack != null) {
						getDeviceListCallBack.onError(DELAY, "获取超时！");
					}
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
				MoData moData = MoData.analyzeJsonData(intent.getExtras().getString(GlobalInfo.BROADCAST_RECEIVE_NEW_MESSAGE));
				if (moData == null) {
					return;
				}
				Log.d(TAG, moData.toString());
				// toast.setText(moData.toString()).show();
				if (moData.getCmd() == MoData.LOGIN_KEY_ERR) {
					// Message newMsg = Message.obtain();
					// newMsg.arg1 = sessionKey.getSessionKey();
					// newMsg.what = MoData.LOGIN_KEY_ERR;
					// newMsg.obj = moData.info;
					// handler.sendMessage(newMsg);
				} else if (moData.getCmd() == MoData.LOGIN_CONFLICT) {
					Message newMsg = Message.obtain();
					newMsg.arg1 = loginSessionKey.getSessionKey();
					newMsg.what = LOGIN_CONFLICT;
					newMsg.obj = moData.getInfo();
					loginHandler.sendMessage(newMsg);
				} else if (moData.getCmd() == MoData.LOGIN_SUCCESS) {
					User user = User.analyzeJsonData(moData.getJsonData());
					Message newMsg = Message.obtain();
					newMsg.arg1 = moData.getSessionKey();
					if (user != null) {
						newMsg.what = LOGIN_SUCCESS;
						GlobalInfo.id = moData.getId();
						newMsg.obj = user;
					} else {
						newMsg.what = LOGIN_FAIL;
						newMsg.obj = "数据解析失败";
					}
					loginHandler.sendMessage(newMsg);
				} else if (moData.getCmd() == MoData.LOGIN_FAIL) {
					Message newMsg = Message.obtain();
					newMsg.arg1 = moData.getSessionKey();
					newMsg.what = LOGIN_FAIL;
					newMsg.obj = moData.getInfo();
					loginHandler.sendMessage(newMsg);
				} else if (moData.getCmd() == MoData.GET_GXJ_SUCCESS) {
					Client client = Client.analyzeJsonData(moData.getJsonData());
					Message newMsg = Message.obtain();
					newMsg.arg1 = moData.getSessionKey();
					if (client != null) {
						newMsg.what = GET_DEVICE_LIST_SUCCESS;
						newMsg.obj = client;
					} else {
						newMsg.what = GET_DEVICE_LIST_FAIL;
						newMsg.obj = "数据解析失败";
					}
					getDeviceListHandler.sendMessage(newMsg);
				} else if (moData.getCmd() == MoData.GET_GXJ_FAIL) {
					Message newMsg = Message.obtain();
					newMsg.arg1 = moData.getSessionKey();
					newMsg.what = GET_DEVICE_LIST_FAIL;
					newMsg.obj = moData.getInfo();
					getDeviceListHandler.sendMessage(newMsg);
				}
			} else if (intent.getExtras().getBoolean(GlobalInfo.BROADCAST_CONNECT_SOCKET_SUCCESS, false) == true) {
				ConnectionHelper.setConnectionStatus(ConnectionHelper.CONNECT_SUCCESS);
				context.sendBroadcast(new Intent(GlobalInfo.BROADCAST_ACTION).putExtra(GlobalInfo.BROADCAST_UPDATE_CONNECT_STATUS, true));
				Log.d(TAG, "BROADCAST_CONNECT_SOCKET_SUCCESS");
				if (ConnectionHelper.getLoginStatus() != ConnectionHelper.LOGIN_SUCCESS && ConnectionHelper.isAlreadyShowLoginActivity() == false && GlobalInfo.password.length() > 0 && GlobalInfo.autoLogin == true) {
					// AutoLogin
					Log.d(TAG, "AutoLogin");
					login(GlobalInfo.username, GlobalInfo.password);
				}
			}
		}
	};
}
