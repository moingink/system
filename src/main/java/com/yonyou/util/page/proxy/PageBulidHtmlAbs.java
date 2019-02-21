package com.yonyou.util.page.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.iuap.cache.CacheManager;
import com.yonyou.util.BussnissException;
import com.yonyou.util.SerializationUtil;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.entity.DataSourceRequestEntity;
import com.yonyou.util.sql.SqlTableUtil;

public abstract class PageBulidHtmlAbs {
	
	private static CacheManager cacheManager = (CacheManager) SpringContextUtil.getBean("cacheManager");
	
	public static final String REDIS_DATASOURCE_PAGEBUILD="REIDS_DATASOURCE_PAGEBUILD_";
	
	public String findPageHtml(String dataSourceCode) throws BussnissException{
		return this.findPageHtml(dataSourceCode, null);
	}
	
	public String findPageHtml(String dataSourceCode,Map<String,Object> dataMap) throws BussnissException{
		DataSourceRequestEntity entity =this.BulidDataSourceRequestEntity(dataSourceCode);
		return this.bulidPageHtmlString(entity, dataMap);
	}
	
	public Map<String,ConcurrentHashMap<String, String>> findData(String dataSourceCode) throws BussnissException{
		
		
		DataSourceRequestEntity entity =this.BulidDataSourceRequestEntity(dataSourceCode);
		
		Map<String,ConcurrentHashMap<String, String>> temp = this.findBulidClass(entity.getPageCode()).filedMap(entity);
		
		return temp;
	}
	
	protected abstract String  bulidPageHtmlString(DataSourceRequestEntity entity,Map<String,Object> dataMap) throws BussnissException;
	
	
	
	protected PageBulidApi findBulidClass(String proxyCode){
		String key ="";
		if(proxyCode!=null&&proxyCode.length()>0){
			key=PageUtil.BULID_PROXY;
		}else{
			key=PageUtil.BULID_META;
		}
		return PageUtil.findPageBulidClass(key);
	}
	
	
	protected abstract String []  findFiledArray(Map<String,String> dataSourceMap);
	
	protected abstract String findPageType();
	
	protected abstract String findProxyType();
	
	
	protected DataSourceRequestEntity BulidDataSourceRequestEntity(String dataSourceCode) throws BussnissException{
		ConcurrentHashMap<String, String> dataSourceMap = DataSourceUtil.getDataSource(dataSourceCode);
		String[] fields =this.findFiledArray(dataSourceMap);
		String metadataCode = dataSourceMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
		String proxyPageCode =dataSourceMap.get(DataSourceUtil.DATASOURCE_PROXY_PAGE_CODE);
		return new DataSourceRequestEntity(fields,proxyPageCode,metadataCode,this.findProxyType());
	}
}
