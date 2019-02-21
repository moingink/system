package com.yonyou.util.bus;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.workflow.http.client.notity.WorkEvent;



public class AuditWorkEvent extends WorkEvent{

	
	@Override
	public String findAuditColumn() {
		// TODO 自动生成的方法存根
		return "BILL_STATUS";
	}
	
	
	@Override
	public String findAuditTableName(String nodeCode) {
		// TODO 自动生成的方法存根
		String tableName=nodeCode;
		Map<String, String> dataMap;
		try {
			dataMap = DataSourceUtil.getDataSource(nodeCode);
			if(dataMap!=null&&dataMap.containsKey(DataSourceUtil.DATASOURCE_METADATA_CODE)){
				tableName= dataMap.get(DataSourceUtil.DATASOURCE_METADATA_CODE);
			}
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return tableName;
	}

}
