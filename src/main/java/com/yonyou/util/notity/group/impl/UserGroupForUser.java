package com.yonyou.util.notity.group.impl;

import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupAbs;
import com.yonyou.util.sql.SqlTableUtil;

public class UserGroupForUser  extends UserGroupAbs{

	@Override
	public List<Map<String, Object>> findUserGroup(IBaseDao baseDao,Map<String,Object> dataMap,String... group_values) {
		// TODO 自动生成的方法存根
		SqlTableUtil user =new SqlTableUtil("RM_USER","");
		user.appendWhereAnd(" dr =0 ").appendSelFiled("*").appendSqlWhere(this.bulidWhereEntity(group_values));
		return baseDao.query(user);
	}


	
	
}
