package com.yonyou.util.notity.group.impl;

import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupAbs;
import com.yonyou.util.sql.SqlTableUtil;

public class UserGroupForCompany  extends UserGroupAbs{


	@Override
	public List<Map<String, Object>> findUserGroup(IBaseDao baseDao,Map<String,Object> dataMap,
			String... group_values) {
		// TODO 自动生成的方法存根
		StringBuffer selectSql =new StringBuffer();
		
//		selectSql.append("select u.* from rm_user u where u.id in (")
//			     .append("select party.child_party_id from rm_party_relation party where party.parent_party_id in ( ")
//			     .append(this.findValues(group_values)).append(" ) ")
//			     .append(" ) ");
		
		
		selectSql.append("select u.* from rm_user u where u.id in (")
	     .append("select party.child_party_id from rm_party_relation party")
	     .append(" where  party.parent_party_id !=party.child_party_id and  party.child_party_type_id ='1000200800000000013' and party_view_id ='1000200700000000001' ")
	     //.append(" connect by nocycle  PRIOR    party.child_party_id= party.parent_party_id ")
	     //.append(" start with ")
	     .append(" and ")
	     .append(" party.parent_party_id in ( ")
	     .append(this.findValues(group_values)).append(" ) ")
	     .append(" ) ");
		
		return baseDao.query(selectSql.toString(),"");
	}

}
