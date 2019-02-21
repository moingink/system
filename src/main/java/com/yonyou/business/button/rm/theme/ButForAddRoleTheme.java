package com.yonyou.business.button.rm.theme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.util.RmPartyConstants;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.jdbc.IBaseDao;

public class ButForAddRoleTheme extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode =request.getParameter("dataSourceCode"); 
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String themeCodes = json.getString("THEME_CODE");
		System.out.print(themeCodes);
		String[] codes = themeCodes.split(",");
		System.out.print(codes);
		JSONArray themeJsonArray = JSONArray.fromObject(codes);
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		for (int i=0;i<themeJsonArray.size();i++) {
			Map<String, Object> entity = new HashMap<String, Object>();
			String theCode = themeJsonArray.getString(i);
			String sql = "select theme_name as THEME_NAME from rm_theme_info where dr='0'and theme_code="+theCode;
			List<Map<String,Object>> list = dcmsDAO.query(sql, "");
			String theName = list.get(0).get("THEME_NAME").toString();
			String [] Ids = RmIdFactory.requestId(RmPartyConstants.rmParty, 1);
			String roleCode = json.getString("ROLE_CODE");
			String roleName = json.getString("ROLE_NAME");
			entity.put("ID",Ids[0]);
			entity.put("ROLE_CODE", roleCode);//角色编码
			entity.put("ROLE_NAME", roleName);//角色名称
			entity.put("THEME_CODE", theCode);//主题编码
			entity.put("THEME_NAME", theName);//主题名称
			entityList.add(entity);
	
		}
		dcmsDAO.insert(tabName, entityList);
 		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
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
