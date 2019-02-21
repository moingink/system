package com.yonyou.business.button.notify;


import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.NotifyFacade;

public class NoticeManageButForInsert extends ButForInsert {
	
	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String noticeCode =(String) request.getAttribute("NOTICE_CODE");
		String planCode = json.getString("PLAN_CODE");
		json.put("PUBLIC_PLANPKCODE", noticeCode);
		NotifyFacade facade =new NotifyFacade();
		facade.Notify(planCode, json);
	}
	
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		String planCode="";
		if(dataMap.containsKey("PLAN_CODE")){
			planCode=dataMap.get("PLAN_CODE");
			String noticeCode =planCode+"_"+(new Date()).getTime();
			dataMap.put("NOTICE_CODE", noticeCode);
			request.setAttribute("NOTICE_CODE", noticeCode);
		}
		dataMap.put("NOTICE_DATE", TokenUtil.findSystemDateString());
	}
	
	
	
}
