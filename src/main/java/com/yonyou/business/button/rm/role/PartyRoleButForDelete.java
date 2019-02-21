package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class PartyRoleButForDelete extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			String roleName = jb.getString("NAME");
			//验证角色是否被用户使用
			if(isUsing(id)){
				jsonMessage = jsonMessage + "角色:"+roleName+" 已被用户使用，不能删除；";
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}else{
				Map<String, Object> entity = new HashMap<String, Object>();
				entity.put("ID", id);
				entity.put("DR","1");
				entityList.add(entity);
			}
			
		}
 		dcmsDAO.update(tabName, entityList, new SqlWhereEntity());
		
 		jsonMessage = "{\"message\":\"删除成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	/**
	 * 验证角色是否被用户使用
	 * @param roleId
	 * @return
	 */
	protected boolean isUsing(String roleId){
		boolean status = true;
		if(!roleId.isEmpty()){
			SqlTableUtil sql = new SqlTableUtil(RmPartyConstants.rmPartyRole,"");
			sql.init("ID", "");
			sql.appendWhereAnd(" ROLE_ID = '"+roleId+"' ");
			sql.appendWhereAnd(" DR = '0' ");
			List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
			if(mapList.isEmpty()){
				status = false;
			}
		}
		return status;
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
