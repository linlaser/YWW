package com.ywangwang.yww.modata;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class MoDataPool {

	private static MoDataPool moDataPool;
	private static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();
	private static Queue<MoData> dataQueue = new LinkedList<MoData>();

	public static MoDataPool getInstance() {
		if (null == moDataPool) {
			moDataPool = new MoDataPool();
		}
		return moDataPool;
	}

	public synchronized MoData getNextMoData() {
		MoData moData = dataQueue.poll();
		if (moData == null) {
			return null;
		} else if ((moData = dataQueue.peek()) != null) {
			if (map.get(moData.getCmd()) == moData.getSessionKey()) {
				return moData;
			} else {
				return getNextMoData();
			}
		} else {
			return null;
		}
	}

	public synchronized MoData getCurrentMoData() {
		MoData moData = dataQueue.peek();
		if (null != moData) {
			if (map.get(moData.getCmd()) == moData.getSessionKey()) {
				return moData;
			} else {
				return getNextMoData();
			}
		} else {
			return null;
		}
	}

	public synchronized boolean addMoData(MoData moData) {
		map.put(moData.getCmd(), moData.getSessionKey());
		return dataQueue.offer(moData);
	}

	public synchronized void clearPool() {
		while (dataQueue.poll() != null) {
		}
	}

}
