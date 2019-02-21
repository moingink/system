package com.yonyou.util;

public interface IRmIdFactory {
	public void initBeanFactory();
	public String[] requestIdInner(String tableName, int length);
}
