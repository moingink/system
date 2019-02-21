package com.yonyou.business.button.system.button;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

/**按钮管理---修改
 * @author XIANGJIAN
 * @date 创建时间：2017年3月7日
 * @version 1.0
 */
public class SystemButtonButForUpdate extends SystemButtonUtil {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		Map<String,Object> buttonMap = new HashMap<String, Object>();
			buttonMap.put("ID", json.get("BUTTON_ID"));
			buttonMap.put("BUTTON_NAME", json.get("BUTTON_NAME"));
			//buttonMap.put("BUTTON_CODE",  json.get("BUTTON_CODE"));
			buttonMap.put("BUTTON_TOKEN", json.get("BUTTON_TOKEN"));
			buttonMap.put("BUTTON_TYPE",  json.get("BUTTON_TYPE"));
			buttonMap.put("BUTTON_DESC", json.get("BUTTON_DESC"));
			buttonMap.put("BUTTON_BATCH", json.get("BUTTON_BATCH"));
			buttonMap.put("BUTTON_ENTITY", json.get("BUTTON_ENTITY"));
			buttonMap.put("BUTTON_DESCRIPTION",  json.get("BUTTON_DESCRIPTION"));
			buttonMap.put("HTML_CLICK_NAME",  json.get("HTML_CLICK_NAME"));
			buttonMap.put("HTML_ID",  json.get("HTML_ID"));
			buttonMap.put("HTML_POSITION",  json.get("HTML_POSITION"));
			buttonMap.put("ISCHECKBOX",  json.get("ISCHECKBOX"));
			buttonMap.put("ISHIDDEN",  json.get("ISHIDDEN"));
			buttonMap.put("BUTTON_CSS",  json.get("BUTTON_CSS"));
			buttonMap.put("SPAN_CSS",  json.get("SPAN_CSS"));
			buttonMap.put("JSCONTENT",  this.zipString(json.getString("JSCONTENT").toString()));
			buttonMap.put("REMARK",  json.get("REMARK"));
		dcmsDAO.update("RM_BUTTON", buttonMap, new SqlWhereEntity());
		
		
		Map<String,Object> relMap = new HashMap<String, Object>();
			relMap.put("ID",  json.get("ID"));
			relMap.put("MENU_ID",  json.get("MENU_ID"));
			relMap.put("MENU_CODE",  json.get("MENU_CODE"));
			relMap.put("MENU_NAME",  json.get("MENU_NAME"));
			relMap.put("BUTTON_ID",  json.get("BUTTON_ID"));
			relMap.put("MENU_ID",  json.get("MENU_ID"));
			relMap.put("BUTTON_CODE",  json.get("BUTTON_CODE"));
			relMap.put("BUTTON_NAME",  json.get("BUTTON_NAME"));
			relMap.put("UPDATE_AUTHORITY",  json.get("UPDATE_AUTHORITY"));
		dcmsDAO.update("RM_BUTTON_RELATION_MENU", relMap, new SqlWhereEntity());
		
		String jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
