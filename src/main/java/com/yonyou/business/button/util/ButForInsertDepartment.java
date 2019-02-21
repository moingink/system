package com.yonyou.business.button.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

public class ButForInsertDepartment extends ButtonAbs{
	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String jsonMessage=null;
		try {
			java.util.Date curTime =new Date(System.currentTimeMillis());
			java.sql.Timestamp  sqlDate1=new java.sql.Timestamp (curTime.getTime());
			json.put("SUB_TIME",new SimpleDateFormat().format(sqlDate1));
			json.put("CD_CHECK_STATUS","0");
			//此处校验资金编码是否可用
			String tmCode = json.get("TM_CODE").toString();
			boolean status = validatePartyRelation(tabName,tmCode);
			if(!status){
				jsonMessage = "{\"message\":\"保存失败，资金编码已被使用！\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}
			json.remove("PARENT_PARTY_ID");
			dcmsDAO.insertByTransfrom(tabName, json);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		
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
	
	protected boolean validatePartyRelation(String tabName,String tmCode){
		boolean status = false;
		if(!tmCode.isEmpty()){
			SqlTableUtil sql = new SqlTableUtil(tabName,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" TM_CODE = '"+tmCode+"' ");
			sql.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.isEmpty()){
				status = true;
			}
		}
		return status;
	}
}
