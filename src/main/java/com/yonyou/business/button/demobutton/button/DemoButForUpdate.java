package com.yonyou.business.button.demobutton.button;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

public class DemoButForUpdate extends DemoButton {

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
		Map<String,Object> insertEntity =new HashMap<String,Object>();
		String bt = json.getString("BUSINESS_DATE");
		try {
			java.util.Date utilDate =this.getSdf().parse(bt);
			java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
			insertEntity.put("BUSINESS_DATE",sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		insertEntity.put("ID",json.get("ID"));
		insertEntity.put("REC_MONEY",json.getDouble("REC_MONEY"));//应收金额
		dcmsDAO.update(tabName, insertEntity, new SqlWhereEntity());
		
		String jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
