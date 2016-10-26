package com.ywangwang.yww.send;

public interface DataSendCallBack {
	public void onSuccess();

	public void onError(int code, String message);
}
