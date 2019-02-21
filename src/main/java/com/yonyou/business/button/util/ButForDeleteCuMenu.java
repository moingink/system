package com.yonyou.business.button.util;

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
import org.util.tools.helper.RmStringHelper;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.RmDictReferenceUtil;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class ButForDeleteCuMenu extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		 System.out.println("-------------ButForDeleteCuMenu--------------------------------");
		// TODO 自动生成的方法存根
		
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			String name = jb.getString("NAME");
			String status = jb.getString("STATUS");
			if(status.equals("0")){
				String jsonMessage = "{\"message\":\"自定义菜单还没有添加，不可以删除\"}";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}
			
			Map<String, Object> entity = new HashMap<String, Object>();
			entity.put("ID", id);
			entity.put("STATUS","0");
			entityList.add(entity);
			String sql="UPDATE TM_CUSTOM_MENU SET DR='1' WHERE MENU_NAME="+"'"+name+"'";
			dcmsDAO.updateBySql(sql);
			
		}
 		dcmsDAO.update("RM_FUNCTION_NODE", entityList, new SqlWhereEntity());
		String jsonMessage = "{\"message\":\"修改成功\"}";
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
	
	/**
	 * 获得join模式的取权限sql
	 * 
	 * @param party_ids
	 * @param tableName
	 * @param joinColumnName
	 * @param selectStr
	 * @return
	 */
	public static String sqlJoinAuthorize_resource(String[] party_ids, String tableName,  String tableNameAlias, String joinColumnName, String selectStr, String whereOrderStr) {
		StringBuilder sb = new StringBuilder();
		//sb.append("select distinct(ar.OLD_RESOURCE_ID), ar.DEFAULT_ACCESS, ar.DEFAULT_IS_AFFIX_DATA, ar.DEFAULT_ACCESS_TYPE, ar.DEFAULT_IS_RECURSIVE, arr.AUTHORIZE_STATUS, arr.IS_AFFIX_DATA, arr.ACCESS_TYPE, arr.IS_RECURSIVE, ");
		sb.append("select distinct(ar.OLD_RESOURCE_ID), ");
		sb.append(selectStr);
		sb.append(" from RM_AUTHORIZE_RESOURCE ar left join RM_AUTHORIZE_RESOURCE_RECORD arr on ar.id=arr.AUTHORIZE_RESOURCE_ID join ");
		sb.append(tableName);
		sb.append(" ");
		sb.append(tableNameAlias);
		sb.append(" on ar.OLD_RESOURCE_ID=");
		sb.append(tableNameAlias);
		sb.append(".");
		sb.append(joinColumnName);
		sb.append(" where (ar.DEFAULT_ACCESS='1' or arr.PARTY_ID in(");
		sb.append(RmStringHelper.parseToSQLString(party_ids));
		sb.append(") )");
		sb.append(whereOrderStr != null ? whereOrderStr : "");
		return sb.toString();
	}

}
