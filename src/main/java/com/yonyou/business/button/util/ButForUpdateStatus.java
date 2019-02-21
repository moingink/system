package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

public class ButForUpdateStatus extends ButtonAbs{

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		String fieldCode = request.getParameter("fieldCode");
		String fieldVal = request.getParameter("fieldVal");
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		List<Map<String, Object>> updateEntityList = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			Map<String, Object> updateEntity = new HashMap<String, Object>();
			updateEntity.put("ID",jb.getString("ID"));
			updateEntity.put(fieldCode, fieldVal);
			updateEntityList.add(updateEntity);
		}
		dcmsDAO.update(tabName, updateEntityList, new SqlWhereEntity());
		
		this.ajaxWrite( "{\"message\":\"操作成功\"}", request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}
	
}
