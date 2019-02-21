package com.yonyou.business.button.rm.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;
import org.util.tools.helper.RmStringHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class UserButForRoleSetting extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		JSONObject user = (JSONObject) jsonArray.get(jsonArray.size()-1);
		
		String userId = user.getString("userId");
		String[] ids = new String[jsonArray.size()-1];
		for (int i = 0; i < jsonArray.size()-1; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		boolean mutex=false;
		
		if(!userId.isEmpty() && userId.length()>0 && ids.length>0){
			
			//查询所选角色的互斥角色（角色1--角色n）
			SqlTableUtil mutexSql = new SqlTableUtil(RmPartyConstants.rmRoleMutex,"");
			mutexSql.appendSelFiled("MUTEX_ROLE_ID");
			mutexSql.appendWhereAnd(" ROLE_ID IN ("+RmStringHelper.parseToString(ids)+") ");
			mutexSql.appendWhereAnd(" DR = '0' ");
 			List<Map<String, Object>> mutexRole = BaseDao.getBaseDao().query(mutexSql);
 			List<String> mutexRoleId = new ArrayList<String>(); 
 			for(int i=0;i<mutexRole.size();i++){
 				mutexRoleId.add(mutexRole.get(i).get("MUTEX_ROLE_ID").toString());
 			}
 			if(mutexRoleId.removeAll(Arrays.asList(ids))){
 				jsonMessage = "{\"message\":\"操作失败，所选角色中存在互斥关系\"}";
 				
 				this.ajaxWrite(jsonMessage, request, response);
 				return null;
 			}else{
 				
 				//查询用户当前的角色
 				SqlTableUtil partyRoleSql = new SqlTableUtil(RmPartyConstants.rmPartyRole,"");
 				partyRoleSql.appendSelFiled("ROLE_ID");
 				partyRoleSql.appendWhereAnd(" OWNER_PARTY_ID = '"+userId+"' ");
 				partyRoleSql.appendWhereAnd(" DR = '0' ");
 				List<Map<String, Object>> partyRole = BaseDao.getBaseDao().query(partyRoleSql);
 				String[] roles = new String[partyRole.size()];
 				if(partyRole.size()>0){
 					for(int i=0;i<partyRole.size();i++){
 						roles[i] = partyRole.get(i).get("ROLE_ID").toString();
 					}
 				}
 				if(mutexRoleId.remove(Arrays.asList(roles))){
 					jsonMessage = "{\"message\":\"操作失败，所选角色与已有角色存在互斥关系\"}";
 					
 					this.ajaxWrite(jsonMessage, request, response);
 					return null;
 				}else{
 					//查询用户当前的角色是否有重复
 					SqlTableUtil userRoleSql = new SqlTableUtil(RmPartyConstants.rmPartyRole,"");
 					userRoleSql.appendSelFiled("ID");
 					userRoleSql.appendWhereAnd(" OWNER_PARTY_ID = '"+userId+"' ");
 					userRoleSql.appendWhereAnd(" ROLE_ID IN ("+RmStringHelper.parseToString(ids)+") ");
 					//userRoleSql.appendWhereAnd(" DR = '0' ");
 					List<Map<String, Object>> userRole = BaseDao.getBaseDao().query(userRoleSql);
 					if(userRole.size()>0){
 						String[] strs = new String[userRole.size()];
 						
 						for (int i=0;i<userRole.size();i++)
 						{
 							strs[i] = userRole.get(i).get("ID").toString();
 						}
 						// 	先删除原有的角色关系
 						/*Map<String, Object> entityUser = new HashMap<String, Object>();
 						entityUser.put("DR","1");
 						Map<String,String> mWhere = new HashMap<String,String>();
 						mWhere.put("ID", "ID IN ( "+RmStringHelper.parseToString(strs)+") " );
 						SqlWhereEntity userWhere = new SqlWhereEntity();
 						userWhere.setWhereMap(mWhere);
 						dcmsDAO.update(RmPartyConstants.rmPartyRole, entityUser, userWhere);*/
 						//物理删除
 						Map<String,String> mWhere = new HashMap<String,String>();
 	     	 			mWhere.put("ID", "ID IN ( "+RmStringHelper.parseToString(strs)+") " );
 	     	 			SqlWhereEntity whereEntity = new SqlWhereEntity();
 	     	 			whereEntity.setWhereMap(mWhere);
 	     	 			dcmsDAO.delete(RmPartyConstants.rmPartyRole, whereEntity);
 						
 					}
 					//新增角色关系
 					if(!userId.isEmpty() && userId.length()>0){
 						List<Map<String,Object>> lRmPartyRole = new ArrayList<Map<String,Object>>(); 
 						for(int i=0;i<ids.length;i++){
 							Map<String,Object> RmPartyRole = new HashMap<String,Object>();
 							RmPartyRole.put("ROLE_ID", ids[i]);
 							RmPartyRole.put("OWNER_PARTY_ID", userId);
 							RmPartyRole.put("OWNER_ORG_ID", "");
 							lRmPartyRole.add(RmPartyRole);
 						}
 						dcmsDAO.insert(RmPartyConstants.rmPartyRole, lRmPartyRole);
 					}
 					
 				}
 				
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
