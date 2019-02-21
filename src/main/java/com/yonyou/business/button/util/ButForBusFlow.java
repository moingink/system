package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.workflow.entity.ReturnJsonEntity;

public class ButForBusFlow extends ButtonAbs   {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String node_code =request.getParameter("NODE_CODE");
		if(node_code==null||node_code.length()==0){
			node_code=dataSourceCode;
		}
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		handleTask(IBusFlowOperationType.HANDLE_TASK, node_code,json,request);
		this.ajaxWrite("{\"message\":\"发送通知\"}", request, response);
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
