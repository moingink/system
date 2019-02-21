package com.yonyou.util.importdata.bus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.util.importdata.ExcelHandleData;

public class FinBusinessImportForExcel extends ExcelHandleData{

	@Override
	public List<Map<String, String>> HandleList(String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (dataList.size() > 0) {
			for (int i = 0; i < dataList.size() - 1; i++) {
				Map<String, String> dataMap = dataList.get(i);
				
				dataMap.put("IMPORT_PEOPLE", tokenEntity.USER.getName());// 导入人
				dataMap.put("IMPORT_TIME", sdf.format(new Date()));// 导入时间
				if (!tokenEntity.isEmpty()) {
					// 制单人
					dataMap.put(IPublicBusColumn.CREATOR_ID,tokenEntity.USER.getId());
					dataMap.put(IPublicBusColumn.CREATOR_NAME,tokenEntity.USER.getName());
					// 组织字段
					dataMap.put(IPublicBusColumn.ORGANIZATION_ID,tokenEntity.COMPANY.getCompany_id());
					dataMap.put(IPublicBusColumn.ORGANIZATION_NAME,tokenEntity.COMPANY.getCompany_name());
				}
			}
		}
		
		return dataList;
	}

}