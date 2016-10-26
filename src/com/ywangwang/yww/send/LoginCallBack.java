package com.ywangwang.yww.send;

public interface LoginCallBack {
	public void onSuccess(String username, String password);

	public void onError(int code, String message);
}
