package com.yonyou.business.button.notify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.NotifyFacade;

public class NotifyButForPlan extends ButtonAbs {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "{\"message\":\"成发送功！\"}" ;
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		NotifyFacade facade =new NotifyFacade();
		StringBuffer error =new StringBuffer();
		for(int i=0;i<jsonArray.size();i++){
			String planCode =jsonArray.getJSONObject(i).getString("PLAN_CODE");
			Map<String,Object> dataMap =new HashMap<String,Object>();
			error.append(facade.Notify(planCode, dataMap));
		}
		if(error.length()>0){
			jsonMessage ="{\"message\":\""+error+"\"}";
		}
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
