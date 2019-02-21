package com.yonyou.business.button.rm.role;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.util.RmPartyConstants;
import org.util.tools.helper.RmDateHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class RoleButForRoleMutexSetting extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		JSONObject role = (JSONObject) jsonArray.get(jsonArray.size()-1);
		
		String roleId = role.getString("roleId");
		String[] ids = new String[jsonArray.size()-1];
		for (int i = 0; i < jsonArray.size()-1; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		
		if(!roleId.isEmpty() && roleId.length()>0 && ids.length>0){
			List<String> mutex = Arrays.asList(ids);
			//角色自身不能互斥
			if(mutex.contains(roleId)){
				jsonMessage = "{\"message\":\"角色自身不能互斥\"}";
				
				this.ajaxWrite(jsonMessage, request, response);
				return null;
			}
			
			//查询本角色的互斥角色（角色1--角色n）
			SqlTableUtil mutexSql = new SqlTableUtil(RmPartyConstants.rmRoleMutex,"");
			mutexSql.appendSelFiled("MUTEX_ROLE_ID");
			mutexSql.appendWhereAnd(" ROLE_ID = '"+roleId+"' ");
			mutexSql.appendWhereAnd(" DR = '0' ");
 			List<Map<String, Object>> mutexRole = BaseDao.getBaseDao().query(mutexSql);
 			List<String> mutexRoleId = new ArrayList<String>(); 
 			for(int i=0;i<mutexRole.size();i++){
 				mutexRoleId.add(mutexRole.get(i).get("MUTEX_ROLE_ID").toString());
 			}
 			//删除重复互斥关系
 			mutex.removeAll(mutexRoleId);
 			if(mutex.isEmpty()){
 				jsonMessage = "{\"message\":\"没有需要新增的互斥关系\"}";
				
				this.ajaxWrite(jsonMessage, request, response);
				return null;
 			}else{
 				
 				//组装互斥关系
 				List<Map<String,Object>> lRmRoleMutex = new ArrayList<Map<String,Object>>(); 
 				for(int i=0;i<ids.length;i++){
 					Timestamp sysTimestamp = RmDateHelper.getSysTimestamp();
					Map<String,Object> rmRoleMutex1 = new HashMap<String,Object>();
					rmRoleMutex1.put("ROLE_ID", roleId);
					rmRoleMutex1.put("MUTEX_ROLE_ID", ids[i]);
					rmRoleMutex1.put("MODIFY_DATE", sysTimestamp);
					lRmRoleMutex.add(rmRoleMutex1);
					
					Map<String,Object> rmRoleMutex2 = new HashMap<String,Object>();
					rmRoleMutex2.put("ROLE_ID", ids[i]);
					rmRoleMutex2.put("MUTEX_ROLE_ID", roleId);
					rmRoleMutex2.put("MODIFY_DATE", sysTimestamp);
					lRmRoleMutex.add(rmRoleMutex2);
					
 				}
 				dcmsDAO.insert(RmPartyConstants.rmRoleMutex, lRmRoleMutex);
 				
 			}
		}
		
 		jsonMessage = "{\"message\":\"设置成功\"}";
		
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
