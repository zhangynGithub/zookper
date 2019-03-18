package com.itmayiedu;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {
	private CountDownLatch countDownLatch = null;

	@Override
	boolean tryLock() {
		try {
			zkClient.createEphemeral(PATH);
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}

	}

	@Override
	void waitLock() {
		IZkDataListener izkDataListener = new IZkDataListener() {

			public void handleDataDeleted(String path) throws Exception {
				// 唤醒被等待的线程
				if (countDownLatch != null) {
					countDownLatch.countDown();
				}
			}
			public void handleDataChange(String path, Object data) throws Exception {

			}
		};
		// 注册事件
		zkClient.subscribeDataChanges(PATH, izkDataListener);
		if (zkClient.exists(PATH)) {
			countDownLatch = new CountDownLatch(1);
			try {
				countDownLatch.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 删除监听
		zkClient.unsubscribeDataChanges(PATH, izkDataListener);
	}

}
