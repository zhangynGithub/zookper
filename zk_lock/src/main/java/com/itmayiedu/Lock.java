package com.itmayiedu;

//##定义锁封装方法
public interface Lock {
	// 获取锁
	public void getLock();

	// 释放锁
	public void unLock();

}
