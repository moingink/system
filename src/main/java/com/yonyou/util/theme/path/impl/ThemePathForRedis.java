package com.yonyou.util.theme.path.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.theme.vo.ThemePathEntity;

public class ThemePathForRedis extends ThemePathForCache{
	
	
	//redis缓存
	private static CacheManager cacheManager = (CacheManager) SpringContextUtil.getBean("cacheManager");
	
	
	@Override
	public Map<String, ThemePathEntity> queryData(String themeCode) {
		// TODO 自动生成的方法存根
		ConcurrentHashMap<String, ThemePathEntity> themeRedisMap =null;
		if(cacheManager.exists(this.findRedisKey(themeCode))){
			themeRedisMap=cacheManager.get(this.findRedisKey(themeCode));
		}else{
			themeRedisMap =new ConcurrentHashMap<String, ThemePathEntity>();
			Map<String,ThemePathEntity> queryDate =this.proxy.queryData(themeCode);
			for(String key:queryDate.keySet()){
				themeRedisMap.put(key, queryDate.get(key));
			}
			cacheManager.set(this.findRedisKey(themeCode), themeRedisMap);
		}
		return themeRedisMap;
	}
	
	
	private String findRedisKey(String themeCode){
		return "theme_redis_key_"+themeCode;
	}
}
