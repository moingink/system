package com.yonyou.util.page.proxy.bulid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.util.BussnissException;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.entity.DataSourceRequestEntity;
import com.yonyou.util.page.proxy.PageBulidApi;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;

public abstract  class PageBulidHtmlByMaintain extends PageBulidHtmlAbs {

	@Override
	public String bulidPageHtmlString(DataSourceRequestEntity entity, Map<String, Object> dataMap)
			throws BussnissException {
		// TODO 自动生成的方法存根
		String [] colArrays=entity.getColArrays();
		if(colArrays.length==1&&colArrays[0].equals("")){
			return colNullMessage();
		}else{
			PageBulidApi pageBulid =this.findBulidClass(entity.getPageCode());
			Map<String,ConcurrentHashMap<String, String>> filedMap =pageBulid.filedMap(entity);
			return PageUtil.findPageClass(this.findPageType()).findHtmlUtil(colArrays, filedMap, dataMap, pageBulid.findRows());
		}
	}

	protected abstract String colNullMessage();
	

}
