package com.yonyou.business.button.util;

import java.io.IOException;
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


public class ButForSelectExport extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode = request.getParameter("dataSourceCode");
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(dataSourceCode);
		sqlEntity.appendSqlWhere(getQueryCondition(request));
		String pageParam=this.findPageParam(request);
		if(pageParam!=null&&pageParam.length()>0){
			sqlEntity.appendWhereAnd(pageParam);
		}
		List<Map<String, Object>> mapList = dcmsDAO.query(sqlEntity, 0, Integer.MAX_VALUE);
		mapList = RmDictReferenceUtil.transByDict(mapList, dataSourceCode);
		
		Map<String, String> headMap=MetaDataUtil.getFiledNameMap(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_METADATA_CODE),"");
			
	
		ExcelTools.downloadExcelFile(DataSourceUtil.getDataSource(dataSourceCode).get(DataSourceUtil.DATASOURCE_NAME), headMap, mapList,  response);

		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
