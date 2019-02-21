package com.yonyou.util.importdata;

import java.util.List;
import java.util.Map;

import com.yonyou.business.entity.TokenEntity;

public abstract class ExcelHandleData {
	
	public abstract List<Map<String, String>> HandleList(String dataSourceCode, List<Map<String, String>> dataList, TokenEntity tokenEntity);
}
