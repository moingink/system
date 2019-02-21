package com.yonyou.util.theme.path.impl;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.theme.path.ThemePathAbs;
import com.yonyou.util.theme.vo.ThemePathEntity;

public class ThemePathForCache extends ThemePathAbs{
	
	private Map<String,ThemePathEntity> cacheMap =new HashMap<String,ThemePathEntity>();
	
	protected ThemePathAbs proxy =new ThemePathForData();
	@Override
	protected void init() {
		// TODO 自动生成的方法存根
	}

	@Override
	public Map<String, ThemePathEntity> queryData(String themeCode) {
		// TODO 自动生成的方法存根
		if(cacheMap.size()==0){
			Map<String,ThemePathEntity> queryData=proxy.queryData(themeCode);
			for(String key :queryData.keySet()){
				cacheMap.put(key, queryData.get(key));
			}
		}
		return cacheMap;
	}

	
}
