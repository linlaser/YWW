package com.ywangwang.yww;

import com.ywangwang.yww.send.DataSendManager;
import com.ywangwang.yww.send.LoginCallBack;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity";
	private EditText edtTxtUsernameLogin, edtTxtPasswordLogin;
	private CheckBox chkBoxSavePassword, chkBoxAutoLogin;
	private Button btnLogin;
	private TextView tvConnectStatus;
	private boolean logining = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tvConnectStatus = (TextView) findViewById(R.id.tvConnectStatus);
		chkBoxSavePassword = (CheckBox) findViewById(R.id.chkBoxSavePassword);
		chkBoxAutoLogin = (CheckBox) findViewById(R.id.chkBoxAutoLogin);
		chkBoxSavePassword.setChecked(GlobalInfo.savePassword);
		chkBoxAutoLogin.setChecked(GlobalInfo.autoLogin);
		chkBoxSavePassword.setOnClickListener(clickListener);
		chkBoxAutoLogin.setOnClickListener(clickListener);
		edtTxtUsernameLogin = (EditText) findViewById(R.id.edtTxtUsername);
		edtTxtPasswordLogin = (EditText) findViewById(R.id.edtTxtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(clickListener);
		findViewById(R.id.rdoBtnLocal).setOnClickListener(clickListener);
		findViewById(R.id.rdoBtnRemote).setOnClickListener(clickListener);
		if (GlobalInfo.serverAddress.equals("www.ywangwang.com")) {
			((RadioButton) findViewById(R.id.rdoBtnRemote)).setChecked(true);
		}
		registerReceiver(broadcastReceiver, new IntentFilter(GlobalInfo.BROADCAST_ACTION));
		DataSendManager.getInstance().setLoginListener(new LoginCallBack() {
			@Override
			public void onSuccess(String username, String password) {
				loginOrRegisterSuccess(username, password);
				TcpService.toast.setText("成功！").show();
			}

			@Override
			public void onError(int code, String message) {
				btnLogin.setText("登录");
				logining = false;
				btnLogin.setEnabled(true);
				TcpService.toast.setText("失败！\n" + message).show();
			}
		});
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnLogin:
				if (GlobalInfo.unableConnectToServer == true)
					return;
				final String username = edtTxtUsernameLogin.getText().toString().trim();
				final String password = edtTxtPasswordLogin.getText().toString().trim();
				if (username == null || username.length() <= 0) {
					edtTxtUsernameLogin.requestFocus();
					edtTxtUsernameLogin.setError("用户名不能为空！");
					return;
				}
				if (password == null || password.length() <= 0) {
					edtTxtPasswordLogin.requestFocus();
					edtTxtPasswordLogin.setError("密码不能为空！");
					return;
				}
				btnLogin.setText("正在登录...");
				logining = true;
				btnLogin.setEnabled(false);
				String[] user = { username, password };
				// sendBroadcast(new Intent(GlobalInfo.BROADCAST_SERVICE_ACTION).putExtra(GlobalInfo.BROADCAST_LOGIN, user));
				DataSendManager.getInstance().login(username, password);
				break;
			case R.id.chkBoxSavePassword:
				chkBoxAutoLogin.setChecked(false);
				break;
			case R.id.chkBoxAutoLogin:
				chkBoxSavePassword.setChecked(true);
				break;
			case R.id.rdoBtnLocal:
				switchServer((String) ((RadioButton) v).getText());
				break;
			case R.id.rdoBtnRemote:
				switchServer((String) ((RadioButton) v).getText());
				break;
			default:
				break;
			}
		}
	};
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras().getBoolean(GlobalInfo.BROADCAST_UPDATE_CONNECT_STATUS, false) == true) {
				if (GlobalInfo.unableConnectToServer == true) {
					tvConnectStatus.setVisibility(View.VISIBLE);
				} else {
					tvConnectStatus.setVisibility(View.GONE);
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d(TAG, "onNewIntent");
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onPostCreate");
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onPostResume() {
		Log.d(TAG, "onPostResume");
		super.onPostResume();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		if (GlobalInfo.unableConnectToServer == true) {
			tvConnectStatus.setVisibility(View.VISIBLE);
		} else {
			tvConnectStatus.setVisibility(View.GONE);
		}
		GlobalInfo.manualLogout = true;
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		outState.putBoolean("logining", logining);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "onRestoreInstanceState");
		logining = savedInstanceState.getBoolean("logining");
		if (logining) {
			btnLogin.setText("正在登录...");
			btnLogin.setEnabled(false);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		// 加上【moveTaskToBack(true);】，并注释掉【super.onBackPressed();】，在按返回键后，使得根Activity不被销毁,也不会退回上级Activity
		moveTaskToBack(true);
		// super.onBackPressed();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	private void switchServer(String address) {
		GlobalInfo.serverAddress = address;
		SharedPreferences sharedPreferences = getSharedPreferences(GlobalInfo.S_P_NAME_CONFIG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(GlobalInfo.S_P_KEY_SERVER_ADDRESS, GlobalInfo.serverAddress);
		editor.commit();
		sendBroadcast(new Intent(GlobalInfo.BROADCAST_SERVICE_ACTION).putExtra(GlobalInfo.BROADCAST_SWITCH_SERVER, GlobalInfo.serverAddress));
	}

	private void loginOrRegisterSuccess(String username, String password) {
		GlobalInfo.manualLogout = false;
		GlobalInfo.online = true;
		GlobalInfo.username = username;
		GlobalInfo.password = password;
		GlobalInfo.savePassword = chkBoxSavePassword.isChecked();// 保存密码
		GlobalInfo.autoLogin = chkBoxAutoLogin.isChecked();// 自动登录

		SharedPreferences sharedPreferences = getSharedPreferences(GlobalInfo.S_P_NAME_CONFIG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(GlobalInfo.S_P_KEY_USERNAME, GlobalInfo.username);
		if (chkBoxSavePassword.isChecked() == true) {
			editor.putString(GlobalInfo.S_P_KEY_PASSWORD, GlobalInfo.password);
		} else {
			editor.remove(GlobalInfo.S_P_KEY_PASSWORD);
		}
		editor.putBoolean(GlobalInfo.S_P_SAVE_PASSWORD, GlobalInfo.savePassword);
		editor.putBoolean(GlobalInfo.S_P_AUTO_LOGIN, GlobalInfo.autoLogin);
		editor.commit();
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		finish();
	}
}
