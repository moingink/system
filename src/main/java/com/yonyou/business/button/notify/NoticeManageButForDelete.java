package com.yonyou.business.button.notify;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.util.ButForDelete;
import com.yonyou.business.button.util.ButForUpdate;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public class NoticeManageButForDelete extends ButForDelete {
	
	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		StringBuffer noticeCodes =new StringBuffer();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject json =jsonArray.getJSONObject(i);
			String noticeCode ="";
			if(json.containsKey("NOTICE_CODE")){
				noticeCode=json.getString("NOTICE_CODE");
			}
			if(noticeCode!=null&&noticeCode.length()>0){
				noticeCodes.append(noticeCode).append(",");
			}
		}
		if(noticeCodes.length()>0){
			Map<String,Object> dataMap =new HashMap<String,Object>();
			SqlWhereEntity whereEntity =new SqlWhereEntity();
			dataMap.put("DR", "1");
			noticeCodes.setLength(noticeCodes.length()-1);
			whereEntity.putWhere("NOTICE_CODE", noticeCodes.toString(), WhereEnum.IN);
			dcmsDAO.update("NF_NOTICE", dataMap, whereEntity);
		}
		
		
	}
}
