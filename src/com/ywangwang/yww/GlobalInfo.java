package com.ywangwang.yww;

import android.graphics.Color;

public class GlobalInfo {
	public static final String SERVER = "www.ywangwang.com";

	public static final String BROADCAST_ACTION = "com.ywangwang.yww.BROADCAST";
	public static final String BROADCAST_SERVICE_ACTION = "com.ywangwang.yww.TcpService.BROADCAST";
	public static final String BROADCAST_DATA_SEND_ACTION = "com.ywangwang.yww.DataSendManager.BROADCAST";
	public static final String BROADCAST_LOGIN = "LOGIN";
	public static final String BROADCAST_LOGOUT = "LOGOUT";
	public static final String BROADCAST_LOGIN_SUCCESS = "LOGIN_SUCCESS";
	public static final String BROADCAST_LOGIN_FAIL = "LOGIN_FAIL";
	public static final String BROADCAST_RECEIVE_NEW_MESSAGE = "RECEIVE_NEW_MESSAGE";
	public static final String BROADCAST_CONNECT_SOCKET_SUCCESS = "CONNECT_SOCKET_SUCCESS";
	public static final String BROADCAST_SWITCH_SERVER = "SWITCH_SERVER";
	public static final String BROADCAST_UPDATE_CONNECT_STATUS = "UPDATE_CONNECT_STATUS";
	public static final String BROADCAST_GET_DEVICE_LIST = "GET_DEVICE_LIST";
	public static final String BROADCAST_GET_DEVICE_LIST_SUCCESS = "GET_DEVICE_LIST_SUCCESS";
	public static final String BROADCAST_GET_DEVICE_LIST_FAIL = "GET_DEVICE_LIST_FAIL";

	public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
	public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
	public static final int COLOR_BLUE = Color.parseColor("#33B5E5");
	public static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
	public static final int COLOR_GREEN = Color.parseColor("#46C01B");
	// public static final int COLOR_GREEN = Color.parseColor("#99CC00");
	public static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
	public static final int COLOR_RED = Color.parseColor("#FF4444");

	public static final String S_P_NAME_CONFIG = "config";

	public static final int MIN_WATER_TEMPRATURE = 40;// 最低水温
	public static final int MAX_WATER_TEMPRATURE = 100;// 最高水温
	public static final int MIN_WATER_AMOUNT = 50;// 最少水量
	public static final int MAX_WATER_AMOUNT = 2500;// 最多水量

	public static final int OUT_WATER_COUNT_DOWN_TIME = 120;// 出水倒计时设定，单位秒

	public static final String S_P_KEY_DEBUG = "debug";
	public static final String S_P_KEY_DEBUG_TIMES = "debugTimes";
	public static boolean debug = false; // DEBUG模式标记
	public static int debugTimes = 0; // 剩余DEBUG次数
	public static boolean testMode = false; // 检测模式模式标记

	public static int setTemperature = 0; // 设定温度0，40~100
	public static int setWaterAmount = 0; // 设定出水量10~2500mL
	public static String setMode = "请选择"; // 设定出水模式

	public static final int COOL_WATER = 10; // 冰水
	public static final int RoomTemperatureValue = 25; // 室温温度值
	public static final int MilkTemperatureValue = 45; // 冲奶温度值
	public static final int HoneyTemperatureValue = 55; // 蜂蜜温度值
	public static final int BoilingTemperatureValue = 100; // 沸水温度值
	public static final int WaterAmount150Value = 150; // 设定出水量150
	public static final int WaterAmount260Value = 260; // 设定出水量260
	public static final int WaterAmount300Value = 300; // 设定出水量300
	public static boolean selectCoolWater = false;// 选择冰水
	public static boolean selectRoomTemperatureWater = false;// 选择常温水

	public static final String S_P_KEY_USERNAME = "username";
	public static final String S_P_KEY_PASSWORD = "password";
	public static final String S_P_SAVE_PASSWORD = "savePassword";
	public static final String S_P_AUTO_LOGIN = "autoLogin";
	public static String username = "";// 用户名
	public static String password = "";// 用户密码
	public static Boolean online = false;// 登录状态
	public static Boolean savePassword = true;// 保存密码
	public static Boolean autoLogin = true;// 自动登录

	public static final String S_P_KEY_SERVER_ADDRESS = "serverAddress";
	public static String serverAddress = "192.168.0.123";// 服务器地址

	public static Boolean isBoundWaterCode = false;// 是否绑定的取水码标记
	public static final String KEY_IS_BOUND_WATER_CODE = "isBoundWaterCode";
	public static final String KEY_NUMBER = "boundWaterCode_number";
	public static final String KEY_TYPE = "boundWaterCode_type";
	public static final String KEY_STATUS = "boundWaterCode_status";
	public static final String KEY_BOUND_DEVICE_ID = "boundWaterCode_boundDeviceID";
	public static final String KEY_PERIOD_VALIDITY = "boundWaterCode_periodValidity";
	public static final String KEY_ACTIVATION_TIME = "boundWaterCode_activationTime";

	// public static final int FILTER_RECOMMEND_DAYS_PP = 90; // PP棉推荐使用天数
	// public static final int FILTER_RECOMMEND_DAYS_FC = 90; // 前置颗粒活性炭(front_carbon)推荐使用天数
	// public static final int FILTER_RECOMMEND_DAYS_RO = 720; // RO反渗透推荐使用天数
	// public static final int FILTER_RECOMMEND_DAYS_BC = 90; // 后置颗粒活性炭(behind_carbon)推荐使用天数
	// public static long filterInstallTimePP = 0L; // PP棉安装时间
	// public static long filterInstallTimeFC = 0L; // 前置颗粒活性炭(front_carbon)安装时间
	// public static long filterInstallTimeRO = 0L; // RO反渗透安装时间
	// public static long filterInstallTimeBC = 0L; // 后置颗粒活性炭(behind_carbon)安装时间
	public static final int[] FILTER_RECOMMEND_DAYS = { 90, 90, 720, 90 }; // 滤芯使用天数
	public static long[] filterInstallTime = { 0L, 0L, 0L, 0L }; // 滤芯安装时间

	public static long id = 0L;
	public static int loginKey = 0;
	public static boolean unableConnectToServer = false;
//	public static boolean manualLogout = false;

	public static Client client = null;

	public void reset() {
	}

	public static class SubDevice {
		public boolean used = false;
		public int add = 0;
		public int deviceType = 0;
		public int itemId = 0;

		public String getDeviceType() {
			switch (this.deviceType) {
			case 0:
				return "主机";
			case 1:
				return "台上式净水器";
			case 2:
				return "台下式净水器";
			case 3:
				return "管线机";
			default:
				break;
			}
			return "其他";
		}
	}

	public static class GxjOutWaterDetails {
		public int averageTDS = 0;// 平均TDS
		public int waterAmount = 0; // 出水量单位毫升 mL
		public int temperature = 0;// 设定温度，10=冰水，25=常温水
		public long time = 0L; // 出水时间

		public void clear() {
			this.averageTDS = 0;
			this.waterAmount = 0;
			this.temperature = 0;
			this.time = 0L;
		}
	}

	public static class JsqDataStatistics {
		public int averageTDSIn = 0; // 进水平均TDS
		public int averageTDSOut = 0; // 日出水平均TDS
		public float totalWaterIn = 0f; // 进水总量单位升 L
		public float totalWaterOut = 0f; // 出水总量单位升 L
		public int totalFilterWaterTimes = 0; // 过滤水次数
		public long time = 0L; // 过滤水次数

		public void clear() {
			this.averageTDSIn = 0;
			this.averageTDSOut = 0;
			this.totalWaterIn = 0f;
			this.totalWaterOut = 0f;
			this.totalFilterWaterTimes = 0;
			this.time = 0L;
		}

		// public void addData(JsqDataStatistics newData) {
		// this.averageTDSIn += newData.averageTDSIn;
		// this.averageTDSOut += newData.averageTDSOut;
		// this.totalWaterIn += newData.totalWaterIn;
		// this.totalWaterOut += newData.totalWaterOut;
		// this.totalFilterWaterTimes += newData.totalFilterWaterTimes;
		// }
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
