package com.yonyou.business.api;

import java.util.List;
import java.util.Map;

import org.util.RmPartyConstants;

import com.yonyou.util.jdbc.BaseDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 接口实现类-根据用户ID获取角色信息
* @ClassName B001004001_001 
* @author 博超
* @date 2017年5月15日
 */
public class B001004001_001 extends ApiBussAbs {

	@Override
	public JSONObject doDeal(JSONObject jsonData) {
		if (jsonData.containsKey("userid") && jsonData.containsKey("companyid")) {
			String userId = jsonData.getString("userid");
			String companyId = jsonData.getString("companyid");

			// 根据用户ID及所在公司ID，从RM_ROLE和RM_PARTY_ROLE表同时过滤后的 ROLE_ID列表
			// String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '"+userId+"' and ((R.IS_SYSTEM_LEVEL = '1' and (PR.OWNER_ORG_ID IS NULL or PR.OWNER_ORG_ID ='"+companyId+"' )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='"+companyId+"')) and r.Enable_Status='1'";
			// 全局角色挂组织后新sql
			String sql = "select R.ID,R.ROLE_CODE,R.NAME from RM_ROLE R join RM_PARTY_ROLE PR on R.ID = PR.ROLE_ID where PR.OWNER_PARTY_ID = '" + userId + "' and ((R.IS_SYSTEM_LEVEL = '1' and ((R.OWNER_ORG_ID IS NULL or r.owner_org_id ='' )or((R.OWNER_ORG_ID IS NOT NULL  or r.owner_org_id ='') AND EXISTS (SELECT id FROM rm_party_relation rr WHERE rr.party_view_id = '" + RmPartyConstants.viewId + "' AND rr.parent_party_id = R.OWNER_ORG_ID "
					+ RmPartyConstants.recurPartySql(companyId) + "' )) )) or (r.is_system_level = '0' and R.OWNER_ORG_ID ='" + companyId + "')) and r.Enable_Status='1'";
			List<Map<String, Object>> roleCommonList = BaseDao.getBaseDao().query(sql, "");
			retJsonObject = ApiUtil.ApiJsonPut("000000");
			retJsonObject.put("data", JSONArray.fromObject(roleCommonList));
		} else {
			retJsonObject = ApiUtil.ApiJsonPut("001006");
		}

		return retJsonObject;
	}

}
