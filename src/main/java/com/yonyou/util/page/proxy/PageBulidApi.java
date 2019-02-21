package com.yonyou.util.page.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.util.BussnissException;
import com.yonyou.util.page.entity.DataSourceRequestEntity;

public interface PageBulidApi {
	
	public int findRows();
	
	public Map<String,ConcurrentHashMap<String, String>> filedMap(DataSourceRequestEntity entity) throws BussnissException;
	
	public String findTableColumn(DataSourceRequestEntity entity) throws BussnissException;
	
}
