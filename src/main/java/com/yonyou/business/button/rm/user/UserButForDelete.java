package com.yonyou.business.button.rm.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.util.RmPartyConstants;
import org.util.RmPartyRelationCode;
import org.util.tools.helper.RmStringHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.button.demobutton.DemoButton;
import com.yonyou.util.BussnissException;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class UserButForDelete extends DemoButton {

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {

		// TODO 自动生成的方法存根
		String jsonMessage = "";
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		System.out.println("jsonArray:"+jsonArray);
		String[] ids = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			String id = jb.getString("ID");
			ids[i] = id;
		}
		
		//删除用户团体和团体关系
		List<Map<String, Object>> entityList = new ArrayList<Map<String, Object>>();
		Map<String, Object> entity = new HashMap<String, Object>();
		Map<String,String> whereMap = new HashMap<String,String>();
		SqlWhereEntity sqlWhere = new SqlWhereEntity();
		
		//rm_party_role
		entity.put("DR","1");
		entityList.add(entity);
		whereMap.put("OWNER_PARTY_ID", "OWNER_PARTY_ID in ("+RmStringHelper.parseToString(ids)+")" );
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmPartyRole, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
 		
 		//rm_user_online_record
 		entity.put("DR","1");
		entityList.add(entity);
		whereMap.put("USER_ID", "USER_ID in ("+RmStringHelper.parseToString(ids)+")" );
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmUserOnlineRecord, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
 		
		//rm_user
 		for(int i=0;i<ids.length;i++){
 			SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL(RmPartyConstants.rmUser);
 			sqlEntity.appendWhereAnd(" ID = '"+ids[i]+"' ");
 			sqlEntity.appendWhereAnd(" DR = '0' ");
 			Map<String, Object> map = BaseDao.getBaseDao().find(sqlEntity);
 			
 			Map<String, Object> entityUser = new HashMap<String, Object>();
 			entityUser.put("USABLE_STATUS","0");
 			//此处可能会有问题
 			entityUser.put("LOGIN_ID",map.get("LOGIN_ID").toString()+"$"+ids[i]);
 			entityUser.put("DR","1");
 			Map<String,String> mWhere = new HashMap<String,String>();
 			mWhere.put("ID", "ID = '"+ids[i]+"' " );
 			SqlWhereEntity userWhere = new SqlWhereEntity();
 			userWhere.setWhereMap(mWhere);
 			dcmsDAO.update(RmPartyConstants.rmUser, entityUser, userWhere);
 		}
 		
 		//查询要删除的团体关系
 		SqlTableUtil sql = new SqlTableUtil(RmPartyConstants.rmPartyRelation,"");
 		sql.appendSelFiled("PARENT_PARTY_ID");
 		sql.appendSelFiled("PARENT_PARTY_CODE");
 		sql.appendSelFiled("CHILD_PARTY_CODE");
		sql.appendWhereAnd(" CHILD_PARTY_ID IN ("+RmStringHelper.parseToString(ids)+") ");
		List<Map<String, Object>> mapList = BaseDao.getBaseDao().query(sql);
 		for(Map<String,Object> map : mapList){
 			//查询当前节点的父节点下是否有子节点
 			SqlTableUtil childSql = new SqlTableUtil(RmPartyConstants.rmPartyRelation,"");
 			childSql.appendSelFiled("CHILD_PARTY_CODE");
 			childSql.appendWhereAnd(" CHILD_PARTY_CODE != '"+map.get("CHILD_PARTY_CODE")+"' ");
 			childSql.appendWhereAnd(" PARENT_PARTY_CODE = '"+map.get("PARENT_PARTY_CODE")+"' ");
 			List<Map<String, Object>> childList = BaseDao.getBaseDao().query(childSql);
 			if(childList.isEmpty()){
 				//更新父节点,rm_party_relation
 				entity.put("CHILD_IS_LEAF","1");
 				entityList.add(entity);
 				whereMap.put("CHILD_PARTY_CODE", "CHILD_PARTY_CODE = '"+map.get("PARENT_PARTY_CODE")+"' ");
 				sqlWhere.setWhereMap(whereMap);
 		 		dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
 		 		entity.clear();
 		 		entityList.clear();
 		 		whereMap.clear();
 		 		sqlWhere.clear();
 			}
 		}
		
 		//删除团体关系
 		entity.put("DR","1");
		entityList.add(entity);
		whereMap.put("CHILD_PARTY_ID", "CHILD_PARTY_ID in ("+RmStringHelper.parseToString(ids)+")" );
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmPartyRelation, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
 		
 		//删除团体
 		entity.put("DR","1");
		entityList.add(entity);
		whereMap.put("ID", "ID in ("+RmStringHelper.parseToString(ids)+")" );
		sqlWhere.setWhereMap(whereMap);
 		dcmsDAO.update(RmPartyConstants.rmParty, entityList, sqlWhere);
 		entity.clear();
 		entityList.clear();
 		whereMap.clear();
 		sqlWhere.clear();
		
		
 		jsonMessage = "{\"message\":\"删除成功\"}";
		
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}

}
