package com.yonyou.business.button.system.button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.system.SystemButtonUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlWhereEntity;

/**按钮菜单关联管理----删除
 * @author XIANGJIAN
 * @date 创建时间：2017年3月8日
 * @version 1.0
 */
public class SystemButtonButForDelete extends SystemButtonUtil {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String jsonMessage = "{\"message\":\"删除成功\"}";
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));	//获取json数组
		List<Map<String, Object>> relList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> butList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			//将删除关联表的记录放入该relMap下
			Map<String, Object> relMap = new HashMap<String, Object>();
			relMap.put("ID", id);
			relMap.put("DR","1");
			relList.add(relMap);
			//将删除按钮表的记录放入该butMap下
			/*Map<String, Object> butMap = new HashMap<String, Object>();
			butMap.put("ID", jb.get("BUTTON_ID"));
			butMap.put("DR", "1");
			butList.add(butMap);*/
		}
		
 		dcmsDAO.update("RM_BUTTON_RELATION_MENU", relList, new SqlWhereEntity());
 		//dcmsDAO.update("RM_BUTTON", butList, new SqlWhereEntity());
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
	}

}
