package com.ywangwang.yww.net;

public class Heartbeat {
	public static final int TIMEOUT = 1;
	public static final int SEND_ACK = 2;
	private static long time = 0L;
	private static long timeoutStart = 30 * 1000L;
	private static long ackInterval = 5 * 1000L;
	private static int timeoutCount = -1;
	private static int timeoutCountLimit = 3;

	public static int checkTimeout() {
		if (System.currentTimeMillis() - time > timeoutStart) {
			int temp = timeoutCount;
			timeoutCount = (int) ((System.currentTimeMillis() - time - timeoutStart) / ackInterval);
			if (timeoutCount > timeoutCountLimit) {
				return TIMEOUT;
			} else if (timeoutCount > temp) {
				return SEND_ACK;
			}
		}
		return 0;
	}

	public static void reset() {
		time = System.currentTimeMillis();
		timeoutCount = -1;
	}
}
