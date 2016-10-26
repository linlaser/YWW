package com.ywangwang.yww;

import com.ywangwang.yww.net.TcpManager;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity2 extends Activity {

	final String TAG = "MainActivity";

	private FragmentManager manager;
	// public static Socket s = null;
	// public static TcpManager tcpManager = null;
	private int temp = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_bak);

		manager = getFragmentManager();
		if (savedInstanceState == null) {
			Log.e(TAG, "creatFragment");
			Log.d(TAG, "TcpManager=" + TcpManager.isConnect());
			FragmentTransaction transaction = manager.beginTransaction();
			UserInfoFragment userInfoFragment = new UserInfoFragment();
			transaction.replace(R.id.fragmentContainer, userInfoFragment);
			transaction.commit();
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
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
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		// 加上【moveTaskToBack(false);】，并注释掉【super.onBackPressed();】，在按返回键后，使得根Activity不被销毁
		moveTaskToBack(false);
		// super.onBackPressed();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
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

}
