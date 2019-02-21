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

/**按钮菜单关联---新增
 * @author XIANGJIAN
 * @date 创建时间：2017年3月7日
 * @version 1.0
 */
public class SystemButtonButForInsert extends SystemButtonUtil {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		
		return false;
	}
//FIXME 未做事务处理
	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		Map<String,Object> buttonMap = new HashMap<String, Object>();
		buttonMap.put("BUTTON_NAME", json.get("BUTTON_NAME"));
		buttonMap.put("BUTTON_CODE",  json.get("BUTTON_CODE"));
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
		buttonMap.put("JSCONTENT", this.zipString(json.get("JSCONTENT").toString()));//压缩JS内容
		buttonMap.put("REMARK",  json.get("REMARK"));
		String buttonId = dcmsDAO.insert("RM_BUTTON", buttonMap);
		
		
		Map<String,Object> relMap = new HashMap<String, Object>();
		relMap.put("MENU_ID",  json.get("MENU_ID"));
		relMap.put("MENU_CODE",  json.get("MENU_CODE"));
		relMap.put("MENU_NAME",  json.get("MENU_NAME"));
		relMap.put("BUTTON_ID",  buttonId);
		relMap.put("MENU_ID",  json.get("MENU_ID"));
		relMap.put("BUTTON_CODE",  json.get("BUTTON_CODE"));
		relMap.put("BUTTON_NAME",  json.get("BUTTON_NAME"));
		relMap.put("UPDATE_AUTHORITY", "000");
		dcmsDAO.insert("RM_BUTTON_RELATION_MENU", relMap);
		
		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
