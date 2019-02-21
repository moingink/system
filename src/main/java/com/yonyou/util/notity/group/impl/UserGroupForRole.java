package com.yonyou.util.notity.group.impl;

import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupAbs;

public class UserGroupForRole  extends UserGroupAbs{

	@Override
	public List<Map<String, Object>> findUserGroup(IBaseDao baseDao,Map<String,Object> dataMap,
			String... group_values) {
		// TODO 自动生成的方法存根
		StringBuffer sql =new StringBuffer();
		sql.append(" SELECT U.*   FROM RM_PARTY_ROLE PR, RM_USER U ")
		   .append("  WHERE PR.DR = '0' AND U.ID = PR.OWNER_PARTY_ID ")
		   .append("  AND PR.ROLE_ID IN (").append(this.findValues(group_values)).append(")");
		   
		return baseDao.query(sql.toString(),"");
	}



}
