package com.yonyou.business.api;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.util.tools.helper.RmStringHelper;

import com.yonyou.util.jdbc.BaseDao;

/**
 * 接口实现类-根据用户ID及所在公司ID获取菜单信息
* @ClassName B001005001_001 
* @author 博超
* @date 2017年3月10日
 */
public class B001005001_001 extends ApiBussAbs{

	@Override
	public JSONObject doDeal(JSONObject jsonData) {
		if (jsonData.containsKey("userid") && jsonData.containsKey("companyid")) {
			String userId = jsonData.getString("userid");
			String companyId = jsonData.getString("companyid");
			String userRoleId = jsonData.getString("roleid");
			//根据用户ID及所在公司ID，从RM_ROLE和RM_PARTY_ROLE表同时过滤后的 ROLE_ID列表
//			String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"+userId+"' and ((R.IS_SYSTEM_LEVEL = '1' and (PR.OWNER_ORG_ID IS NULL or PR.OWNER_ORG_ID ='"+companyId+"' )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"+companyId+"')) and r.Enable_Status='1'";
			//全局角色挂组织
			//String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"+userId+"' and ((R.IS_SYSTEM_LEVEL = '1' and ((R.OWNER_ORG_ID IS NULL or r.owner_org_id ='')or((R.OWNER_ORG_ID IS NOT NULL or r.owner_org_id ='') AND EXISTS (SELECT id FROM rm_party_relation rr WHERE rr.party_view_id = '"+RmPartyConstants.viewId+"' AND rr.parent_party_id = R.OWNER_ORG_ID "
					//+ RmPartyConstants.recurPartySql(companyId) +" )) )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"+companyId+"')) and r.Enable_Status='1'";
			//List<Map<String, Object>> roleCommonList = BaseDao.getBaseDao().query(sql, "");
			//if (roleCommonList != null && roleCommonList.size() > 0) {
				String[] roleIds = userRoleId.split(",");
				//roleIds[0]=userRoleId;
				String menuSql = sqlJoinAuthorize_resource(roleIds, "RM_FUNCTION_NODE", "RM_FUNCTION_NODE", "TOTAL_CODE", "SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE,1,LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3) AS PARENT_TOTAL_CODE,RM_FUNCTION_NODE.ICON,RM_FUNCTION_NODE.TOTAL_CODE,RM_FUNCTION_NODE.NAME,RM_FUNCTION_NODE.ID,RM_FUNCTION_NODE.IS_LEAF,RM_FUNCTION_NODE.NODE_TYPE,RM_FUNCTION_NODE.FUNCTION_PROPERTY,RM_FUNCTION_NODE.ORDER_CODE,RM_FUNCTION_NODE.DATA_VALUE,RM_FUNCTION_NODE.PATTERN_VALUE,RM_FUNCTION_NODE.ICON HELP_NAME", "and RM_FUNCTION_NODE.DR='0' and RM_FUNCTION_NODE.ENABLE_STATUS='1' and RM_FUNCTION_NODE.NODE_TYPE!='3' OR RM_FUNCTION_NODE.ID = '1000201400000000106' order by SUBSTR(RM_FUNCTION_NODE.TOTAL_CODE, 1, LENGTH(RM_FUNCTION_NODE.TOTAL_CODE)-3),RM_FUNCTION_NODE.ORDER_CODE");
				List<Map<String, Object>> menuList = BaseDao.getBaseDao().query(menuSql, "");
				if(menuList == null || menuList.isEmpty()){
					retJsonObject = ApiUtil.ApiJsonPut("001007");
				}else{
					retJsonObject = ApiUtil.ApiJsonPut("000000");
					retJsonObject.put("data", JSONArray.fromObject(menuList));
				}
			//}
		} /*else {
			retJsonObject = ApiUtil.ApiJsonPut("001006");
		}*/
		return retJsonObject;
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
