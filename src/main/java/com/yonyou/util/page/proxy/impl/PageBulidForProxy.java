package com.yonyou.util.page.proxy.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.ProxyPageUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.entity.DataSourceRequestEntity;
import com.yonyou.util.page.proxy.PageBulidApi;
import com.yonyou.util.page.table.column.CheckBoxEnum;
import com.yonyou.util.page.table.column.IsShowEnum;
import com.yonyou.util.page.table.column.TableColumn;
import com.yonyou.util.page.table.column.TableColumnUtil;

public class PageBulidForProxy implements PageBulidApi{
	
	int rows =0;
	@Override
	public int findRows() {
		// TODO 自动生成的方法存根
		return rows;
	}

	@Override
	public  Map<String, ConcurrentHashMap<String, String>> filedMap(
			DataSourceRequestEntity entity ) throws BussnissException {
		// TODO 自动生成的方法存根
		//初始化
		this.setRow(entity.getPageCode(),entity.getType());
		Map<String,ConcurrentHashMap<String, String>> returnPageMap =new HashMap<String,ConcurrentHashMap<String, String>>();
		Map<String,ConcurrentHashMap<String, String>> metaMap =MetaDataUtil.getMetaDataFields(entity.getMetaCode());
		Map<String,ConcurrentHashMap<String, String>> pageUtilMap =ProxyPageUtil.getProxyDetailData(this.findPageCacheCode(entity.getPageCode(), PageUtil.PAGE_TYPE_FOR_UTIL));
		Map<String,ConcurrentHashMap<String, String>> pageAttrMap =ProxyPageUtil.getProxyDetailData(this.findPageCacheCode(entity.getPageCode(), entity.getType()));
		
		if(metaMap!=null&&metaMap.size()>0){
			returnPageMap.putAll(metaMap);
		}
		if(pageUtilMap!=null&&pageUtilMap.size()>0){
			returnPageMap.putAll(pageUtilMap);
		}
		if(pageAttrMap!=null&&pageAttrMap.size()>0){
			returnPageMap.putAll(pageAttrMap);
		}
		return returnPageMap;
		
	}

	@Override
	public String findTableColumn(DataSourceRequestEntity entity) throws BussnissException{
		// TODO 自动生成的方法存根
		TableColumn tableColumn =TableColumnUtil.findTableColumn();
		this.setCheckBoxType(tableColumn, entity.getPageCode());
		Map<String,String> showMap =new LinkedHashMap<String,String>();
		Map<String, ConcurrentHashMap<String, String>> filedMap =this.filedMap(entity);
		showMap.put(TableColumnUtil.RMRN, "序");
		for(String column:entity.getColArrays()){
			column=column.trim();
			String showFiled =PageUtil.findShowFiled(column);
			if(filedMap.containsKey(showFiled)){
				Map<String,String> columnMap = filedMap.get(showFiled);
				if(columnMap.containsKey(MetaDataUtil.FIELD_NAME)){
					showMap.put(column, columnMap.get(MetaDataUtil.FIELD_NAME));
					this.appendTableColumn(columnMap, column, tableColumn);
				}
			}else{
				showMap.put(column, column);
			}
		}
		;
		tableColumn.setFormatterByColumn(CheckBoxEnum.CHECK.getVal().toUpperCase(), "stateFormatter");
		return tableColumn.findColJosnStr(showMap);
	}
	
	private void setCheckBoxType(TableColumn tableColumn,String pageCode) throws BussnissException{
		String checkBoxType =ProxyPageUtil.getProxyPageData(pageCode).get(ProxyPageUtil.PROXYPAGE_CHECKBOX_TYPE);
		if(checkBoxType!=null&&checkBoxType.length()>0){
			
			if("0".equals(checkBoxType)){
				tableColumn.setCheckBoxEnum(CheckBoxEnum.CHECK);
			}else if("1".equals(checkBoxType)){
				tableColumn.setCheckBoxEnum(CheckBoxEnum.RADIO);
			}else if("2".equals(checkBoxType)){
				tableColumn.setCheckBoxEnum(CheckBoxEnum.NOCHECK);
				tableColumn.setSerialNumber(false);
			}else if("3".equals(checkBoxType)){
				tableColumn.setCheckBoxEnum(CheckBoxEnum.NOCHECK);
			}
			
		}
	}
	
	protected void appendTableColumn(Map<String,String> messageMap,String column,TableColumn tableColumn){
		if(messageMap.containsKey(ProxyPageUtil.PROXYLIST_IS_HIDDEN)){
			String is_hiddenString= messageMap.get(ProxyPageUtil.PROXYLIST_IS_HIDDEN);
			if("1".equals(is_hiddenString)){
				tableColumn.setVisibleByColumn(column, IsShowEnum.FALSE);
			}
		}
	}
	
	protected String findPageCacheCode(String pageCode,String type){
		return pageCode+"_"+type;
	}
	
	protected void setRow(String proxyCode,String type) throws BussnissException{
		String rowString ="";
		if(type.equals(PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE)||type.equals(PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE_TEXT)){
			rowString =ProxyPageUtil.getProxyPageData(proxyCode).get(ProxyPageUtil.PROXYPAGE_MAIN_ROW);
		}else if(type.equals(PageUtil.PAGE_TYPE_FOR_SELECT)){
			rowString =ProxyPageUtil.getProxyPageData(proxyCode).get(ProxyPageUtil.PROXYPAGE_QUERY_ROW);
		}
		if(rowString!=null&&rowString.length()>0){
			try{
				rows= Integer.parseInt(rowString);
			}catch(NumberFormatException e){
				rows =0;
			}
		}else{
			rows=0;
		}
	}
	

}
