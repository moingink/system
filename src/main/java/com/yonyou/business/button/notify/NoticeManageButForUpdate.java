package com.yonyou.business.button.notify;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.util.ButForUpdate;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public class NoticeManageButForUpdate extends ButForUpdate {
	
	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		if(json.containsKey("PLAN_CODE")){
			json.remove("PLAN_CODE");
		}
		String noticeCode ="";
		if(json.containsKey("NOTICE_CODE")){
			noticeCode=json.getString("NOTICE_CODE");
		}
		if(noticeCode!=null&&noticeCode.length()>0){
			SqlWhereEntity whereEntity =new SqlWhereEntity();
			whereEntity.putWhere("NOTICE_CODE", noticeCode, WhereEnum.EQUAL_STRING);
			try {
				dcmsDAO.updateByTransfrom("NF_NOTICE", json, whereEntity);
			} catch (BussnissException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}
}
