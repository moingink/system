package com.yonyou.util.page.proxy.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.entity.DataSourceRequestEntity;
import com.yonyou.util.page.proxy.PageBulidApi;
import com.yonyou.util.page.table.column.CheckBoxEnum;
import com.yonyou.util.page.table.column.TableColumn;
import com.yonyou.util.page.table.column.TableColumnUtil;

public class PageBulidByMeta implements PageBulidApi {

	@Override
	public int findRows() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public Map<String, ConcurrentHashMap<String, String>> filedMap(
			DataSourceRequestEntity entity)
			throws BussnissException {
		// TODO 自动生成的方法存根
		Map<String,ConcurrentHashMap<String, String>> metaMap =MetaDataUtil.getMetaDataFields(entity.getMetaCode());
		return metaMap;
	}

	@Override
	public String findTableColumn(DataSourceRequestEntity entity) throws BussnissException {
		TableColumn tableColumn =TableColumnUtil.findTableColumn();
		// TODO 自动生成的方法存根
		Map<String,String> showMap =new LinkedHashMap<String,String>();
		showMap.put(TableColumnUtil.RMRN, "序");
		Map<String, ConcurrentHashMap<String, String>> filedMap =this.filedMap(entity);
		for(String column:entity.getColArrays()){
			column=column.trim();
			column=column.toUpperCase();
			String showFiled=PageUtil.findShowFiled(column);
			if(filedMap.containsKey(showFiled)){
				Map<String,String> columnMap = filedMap.get(showFiled);
				if(columnMap.containsKey(MetaDataUtil.FIELD_NAME)){
					showMap.put(column, columnMap.get(MetaDataUtil.FIELD_NAME));
				}
			}else{
				showMap.put(column, column);
			}
		}
		tableColumn.setFormatterByColumn(CheckBoxEnum.CHECK.getVal().toUpperCase(), "stateFormatter");
		return tableColumn.findColJosnStr(showMap);
	}
	
	
	
	
	

}
