package com.ywangwang.yww.lib;

import com.ywangwang.yww.GlobalInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesConfig {
	public static void read(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalInfo.S_P_NAME_CONFIG, Context.MODE_PRIVATE);
		GlobalInfo.serverAddress = sharedPreferences.getString(GlobalInfo.S_P_KEY_SERVER_ADDRESS, "192.168.0.123");
		GlobalInfo.username = sharedPreferences.getString(GlobalInfo.S_P_KEY_USERNAME, "");
		GlobalInfo.password = sharedPreferences.getString(GlobalInfo.S_P_KEY_PASSWORD, "");
		GlobalInfo.savePassword = sharedPreferences.getBoolean("savePassword", true);
		GlobalInfo.autoLogin = sharedPreferences.getBoolean("autoLogin", true);

		GlobalInfo.debug = sharedPreferences.getBoolean(GlobalInfo.S_P_KEY_DEBUG, false);
		GlobalInfo.debugTimes = sharedPreferences.getInt(GlobalInfo.S_P_KEY_DEBUG_TIMES, 0);
		if (GlobalInfo.debug == true) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			if (GlobalInfo.debugTimes > 1) {
				editor.putInt(GlobalInfo.S_P_KEY_DEBUG_TIMES, --GlobalInfo.debugTimes);
			} else {
				editor.remove(GlobalInfo.S_P_KEY_DEBUG);
				editor.remove(GlobalInfo.S_P_KEY_DEBUG_TIMES);
				GlobalInfo.debug = false;
				GlobalInfo.debugTimes = 0;
			}
			editor.commit();
		}
	}
}
