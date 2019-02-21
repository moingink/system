package com.yonyou.business.button.demobutton.button;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;

public class DemoButForInsert extends DemoButton {

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
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		
		java.util.Date curTime =new Date(System.currentTimeMillis());
		java.sql.Timestamp  sqlDate1=new java.sql.Timestamp (curTime.getTime());
		json.put("SUB_TIME",this.getSdf().format(sqlDate1));
		json.put("CD_CHECK_STATUS","0");
		
		try {
			dcmsDAO.insertByTransfrom(tabName, json);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
