package com.ywangwang.yww.send;

import com.ywangwang.yww.Client;

public class CallBack {
	public interface LoginCallBack {
		public void onSuccess(String username, String password);

		public void onError(int code, String message);
	}

	public interface DataSendCallBack {
		public void onSuccess();

		public void onError(int code, String message);
	}

	public interface GetDeviceListCallBack {
		public void onSuccess(Client client);

		public void onError(int code, String message);
	}
}
