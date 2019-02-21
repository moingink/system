package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class ButForResetCuMenu extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		 System.out.println("-------------ButForResetCuMenu--------------------------------");
		// TODO 自动生成的方法存根
		
		
		String companyId = TokenUtil.initTokenEntity(request).COMPANY.getCompany_id();
		String userId = TokenUtil.initTokenEntity(request).USER.getId();
		
		String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"+userId+"' and ((R.IS_SYSTEM_LEVEL = '1' and ((R.OWNER_ORG_ID IS NULL)or(R.OWNER_ORG_ID IS NOT NULL AND EXISTS (SELECT id FROM rm_party_relation rr WHERE rr.party_view_id = '"+RmPartyConstants.viewId+"' AND rr.parent_party_id = R.OWNER_ORG_ID '"+RmPartyConstants.recurPartySql(companyId)+")) )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"+companyId+"')) and r.Enable_Status='1'";
		List<Map<String, Object>> roleCommonList = BaseDao.getBaseDao().query(sql, "");
		List<Map<String, Object>> menuList=null;
		//List<Map<String, Object>> menuList2=null;
		if (roleCommonList != null && roleCommonList.size() > 0) {
			String[] roleIds = new String[roleCommonList.size()];
			for (int i = 0; i < roleCommonList.size(); i++) {
				Map<String, Object> map = roleCommonList.get(i);
				roleIds[i] = map.get("ID").toString();
			}
			String menuSql = sqlJoinAuthorize_resource(roleIds, "RM_FUNCTION_NODE", "RM_FUNCTION_NODE", "TOTAL_CODE", "SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE,1,LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3) AS PARENT_TOTAL_CODE,RM_FUNCTION_NODE.ICON,RM_FUNCTION_NODE.TOTAL_CODE,RM_FUNCTION_NODE.NAME,RM_FUNCTION_NODE.ID,RM_FUNCTION_NODE.IS_LEAF,RM_FUNCTION_NODE.NODE_TYPE,RM_FUNCTION_NODE.STATUS,RM_FUNCTION_NODE.FUNCTION_PROPERTY,RM_FUNCTION_NODE.ORDER_CODE,RM_FUNCTION_NODE.DATA_VALUE,RM_FUNCTION_NODE.PATTERN_VALUE,RM_FUNCTION_NODE.ICON HELP_NAME", "and RM_FUNCTION_NODE.DATA_VALUE is not null and RM_FUNCTION_NODE.ENABLE_STATUS='1'and RM_FUNCTION_NODE.STATUS='1' and RM_FUNCTION_NODE.NODE_TYPE!='3' order by SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE, 1, LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3),RM_FUNCTION_NODE.ORDER_CODE");
			menuList = BaseDao.getBaseDao().query(menuSql, "");
			System.out.println("menuList  "+menuList);
			for (Map<String, Object> map : menuList) {
				String id = map.get("ID").toString();
				String sql2="UPDATE  RM_FUNCTION_NODE SET STATUS='0' WHERE ID="+id;
				dcmsDAO.updateBySql(sql2);
			}
			
			SqlTableUtil stu=new SqlTableUtil("TM_CUSTOM_MENU","");
			SqlWhereEntity swe=new SqlWhereEntity();
			swe.putWhere("DR", "0", WhereEnum.EQUAL_STRING);
			stu.appendSelFiled("*").appendSqlWhere(swe);
			List<Map<String, Object>> query = dcmsDAO.query(stu);
			for (Map<String, Object> map : query) {
				String sql3="UPDATE  TM_CUSTOM_MENU SET DR='1' WHERE ID="+map.get("ID") +"AND COMPANY_ID="+companyId+" AND USER_ID="+userId;
				dcmsDAO.updateBySql(sql3);
			}
			
		}
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
