package com.yonyou.business.api;

import java.util.List;
import java.util.Map;

import org.util.RmPartyConstants;

import net.sf.json.JSONObject;

import com.yonyou.util.jdbc.BaseDao;

/**根据当前用户ID、公司ID、菜单编码获取授权的菜单按钮
 * @author XIANGJIAN
 * @date 创建时间：2017年5月24日
 * @version 1.0
 */
public class B001006001_001 extends ApiBussAbs {

	
	@Override
	public JSONObject doDeal(JSONObject jsonData) {
		// TODO 自动生成的方法存根
		if (jsonData.containsKey("userid")&&jsonData.containsKey("companyid")&&jsonData.containsKey("menucode")) {
			String userId = jsonData.get("userid").toString() ;
			String menuCode = jsonData.get("menucode").toString() ;
			String companyId = jsonData.get("companyid").toString() ;
			StringBuffer sb = new StringBuffer() ; 
			sb.append("SELECT * FROM RM_BUTTON RB WHERE ID IN ( ");
			sb.append("SELECT  RBRM.BUTTON_ID FROM RM_BUTTON_RELATION_MENU RBRM WHERE RBRM.BUTTON_CODE IN ( ");
			sb.append("SELECT DISTINCT A.TOTAL_CODE FROM RM_AUTHORIZE_RESOURCE A JOIN RM_AUTHORIZE_RESOURCE_RECORD B ");
			sb.append("ON A.ID = B.AUTHORIZE_RESOURCE_ID ");
			sb.append("WHERE SUBSTR(A.TOTAL_CODE, 0, LENGTH(A.TOTAL_CODE) - 3) ='"+menuCode+"' ");
			sb.append("AND B.PARTY_ID IN (SELECT R.ID FROM RM_ROLE R JOIN RM_PARTY_ROLE PR ON R.ID = PR.ROLE_ID WHERE PR.OWNER_PARTY_ID = '" + userId + "' AND ((R.IS_SYSTEM_LEVEL = '1' AND ((R.OWNER_ORG_ID IS NULL or r.owner_org_id ='')OR((R.OWNER_ORG_ID IS NOT NULL or r.owner_org_id ='') AND EXISTS (SELECT ID FROM RM_PARTY_RELATION RR WHERE RR.PARTY_VIEW_ID = '" + RmPartyConstants.viewId + "' AND RR.PARENT_PARTY_ID = R.OWNER_ORG_ID " + RmPartyConstants.recurPartySql(companyId) + ")) )) OR (R.IS_SYSTEM_LEVEL = '0' AND R.OWNER_ORG_ID ='" + companyId + "')) AND R.ENABLE_STATUS='1')");
			sb.append(" AND B.IS_BUTTON_AUTHORITY = '1' AND A.DR = '0' AND B.DR = '0')  AND RBRM.DR = '0' ) AND RB.DR = '0'");
			sb.append(" ORDER BY RB.BUTTON_DESC ");
			List<Map<String, Object>> list = BaseDao.getBaseDao().query(sb.toString(), "");
			if (list == null || list.isEmpty()) {
				retJsonObject = ApiUtil.ApiJsonPut("001004");
			} else {
				retJsonObject = ApiUtil.ApiJsonPut("000000");
				retJsonObject.put("data", list);
			}
		} else {
			retJsonObject = ApiUtil.ApiJsonPut("001005");
		}
		
		return retJsonObject;
	}
}
