package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.ExcelTools;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

public class IncomeExpor extends ButtonAbs {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		String pageParam = this.findPageParam(request);
		if (pageParam != null && pageParam.length() > 0) {
			sqlEntity.appendWhereAnd(pageParam);
		}
		
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, 0,Integer.MAX_VALUE);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		Map<String, String> headMap = MetaDataUtil.getFiledNameMap(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE), "");
		Map<String, String> headMap2 = new LinkedHashMap<String, String>();  
		List<Map<String, Object>> list = new ArrayList<>();
		
		
			
		 String map[]={
	     "CONTRACT_CODE",
		 "CONTRACT_NAME",
		 "PROJ_CODE",
		 "PROJ_NAME",
		 "OPPORTY_CODE",
		 "CLIENT_NAME",
		 "DEPT",
		 "ORG_DEPT_NAME",
		 "BUSINESS_TYPE",
		 "PRO_DEFINE",
		 "PRO_NAME",
		 "INVOICE_NO",
		 "NATURE_OF_INCOME",
		 "ESTIMATED_INCOME",
		 "CONFIRM_INCOME",
		 "ADJUST_INCOME",
		 "AUDIT_TAX",
		 "IS_RETURN",
		 "RECEIVE_AMOUNT",
		 "SETTLE_METHOD",
		 "INCREMENTAL_STOCK",
		 "BUS_MONTH",
		 "FIN_ACCOUNT",
		 "PAYBACK_PERIOD",
		 "CONFIRM_DATE",
		 "ACTU_PAY_PERIOD",
		 "OPPOSITE_UNIT",
		 "OPEN_DATA",
		 "OPEN_OFFICE"
		 };
		
		 for(int i=0;i<map.length;i++){
			 headMap2.put(map[i],headMap.get(map[i]));		 
		 }
		 for(Map<String, Object> maps : mapList){
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			for(int i=0;i<map.length;i++){
				dataMap.put(map[i],maps.get(map[i]));
			}
			list.add(dataMap);
		 }
		 ExcelTools.downloadExcelFile(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_NAME), headMap2, list,response);
		
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

}
