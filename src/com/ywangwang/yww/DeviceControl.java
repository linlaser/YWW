package com.ywangwang.yww;

import com.ywangwang.yww.lib.CustomToast;
import com.ywangwang.yww.lib.StrConv;
import com.ywangwang.yww.modata.MoData;
import com.ywangwang.yww.net.TcpManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class DeviceControl extends Activity {

	final String TAG = "DeviceControl";
	private long deviceId = 0L;

	private EditText edtTxtMessage;
	private CustomToast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_control);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("返回");
		getActionBar().setDisplayShowHomeEnabled(false);

		edtTxtMessage = (EditText) findViewById(R.id.edtTxtMessage);
		findViewById(R.id.btnSendMessage).setOnClickListener(buttonListener);

		toast = new CustomToast(this);
		toast.setText(null, 16f);

		Intent intent = getIntent();
		if (intent != null) {
			deviceId = intent.getLongExtra("deviceId", -1);
			if (deviceId > 0) {
				((TextView) findViewById(R.id.tvDeviceId)).setText("设备ID：" + String.format("%012X", deviceId));
				Log.d(TAG, "deviceId=" + deviceId);
			}
		}
	}

	OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnSendMessage:
				if (edtTxtMessage.getText().toString().trim().equals("") == false) {
					sendMessage(edtTxtMessage.getText().toString().trim());
				} else {
					toast.setText("请输入消息内容").show();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendMessage(String message) {
		MoData moData = new MoData();
		moData.setCmd(MoData.SEND_MESSAGE);
		moData.setId(GlobalInfo.id);
		moData.setLoginKey(GlobalInfo.loginKey);
		moData.setToId(new long[1]);
		moData.getToId()[0] = deviceId;
		moData.setInfo(StrConv.Encode(message));
		TcpManager.sendMSG(moData.toString());
	}
}
