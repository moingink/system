package com.yonyou.business.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.util.RmPartyConstants;
import org.util.tools.helper.RmStringHelper;

import com.yonyou.util.jdbc.BaseDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 接口实现类-根据用户ID获取用户所有权限公司
* @ClassName B001002001_001 
* @author 博超
* @date 2017年4月6日
 */
public class B001002001_001 extends ApiBussAbs {

	//团体视图对应业务关键字
	public static final String BsKeyWord = "A";
	
	@Override
	public JSONObject doDeal(JSONObject jsonData) {
		if (jsonData.containsKey("userid")) {
			String userId = jsonData.getString("userid");
			//取得父团体信息
			String ParentSql = "select PR.PARENT_PARTY_ID, PR.PARENT_PARTY_CODE from RM_PARTY_RELATION PR WHERE PR.CHILD_PARTY_ID=" + userId + " and PR.PARTY_VIEW_ID=" + RmPartyConstants.viewId +" and PR.DR = 0";
			List<Map<String, Object>> lParent = BaseDao.getBaseDao().query(ParentSql, "");
			
			List lParentId = new ArrayList<>();
			for(Map<String, Object> mParent : lParent){
				String partyCode = (String) mParent.get("PARENT_PARTY_CODE");
				
				//拼装祖先团体编码（包含父）-顺序为由外到里，后面为更上级团体
				List<String> lAncestorPartyCode = new ArrayList<String>();
				if(partyCode != null && partyCode.length() > 0) {
					String remainStr = partyCode;
					String bk = BsKeyWord;
					while(remainStr.length() > 0 && !bk.equals(remainStr)) {
						lAncestorPartyCode.add(remainStr);
						remainStr = remainStr.substring(0, remainStr.length()-3);
					}
				}
				//获取所有祖先公司团体信息
				String ancestorCoSql = "select PR.CHILD_PARTY_CODE,PR.CHILD_PARTY_ID FROM RM_PARTY_RELATION PR JOIN RM_PARTY_TYPE PT ON PR.CHILD_PARTY_TYPE_ID = PT.id WHERE PT.BS_KEYWORD = '"+ RmPartyConstants.PartyType.TM_COMPANY.name() +"' AND PR.CHILD_PARTY_CODE in(" + RmStringHelper.parseToSQLStringApos(lAncestorPartyCode.toArray()) + ") and PR.PARTY_VIEW_ID=" + RmPartyConstants.viewId + " AND PR.DR = 0 AND PT.DR = 0";
				List<Map<String, Object>> lAncestorCo = BaseDao.getBaseDao().query(ancestorCoSql, "");
				Map<String,Map<String, Object>> mAncestorCo = new HashMap<String,Map<String, Object>>();
				for (Map<String, Object> ancestorCo : lAncestorCo) {
					mAncestorCo.put(ancestorCo.get("CHILD_PARTY_CODE").toString(), ancestorCo);
				}
				
				for (String ancestorPartyCode : lAncestorPartyCode) {
					if (mAncestorCo.containsKey(ancestorPartyCode)) {
						// 拿到上级父公司后终止循环-不拿祖先公司
						lParentId.add(mAncestorCo.get(ancestorPartyCode).get("CHILD_PARTY_ID"));
						break;
					}
				}
			}
			
			String parentCoSql = "select * from TM_COMPANY CO where CO.ID in ("+RmStringHelper.parseToSQLStringApos(lParentId.toArray())+")";
			List<Map<String, Object>> lParentCo = BaseDao.getBaseDao().query(parentCoSql, "");
			
			retJsonObject = ApiUtil.ApiJsonPut("000000");
			retJsonObject.put("data", JSONArray.fromObject(lParentCo));
		} else {
			retJsonObject = ApiUtil.ApiJsonPut("001008");
		}

		return retJsonObject;
	}
	
}
