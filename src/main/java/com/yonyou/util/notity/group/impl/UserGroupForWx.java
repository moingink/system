package com.yonyou.util.notity.group.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.group.UserGroupAbs;

public class UserGroupForWx  extends UserGroupAbs{

	@Override
	public List<Map<String, Object>> findUserGroup(IBaseDao baseDao,Map<String,Object> dataMap,
			String... group_values) {
		// TODO 自动生成的方法存根
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		list.add(dataMap);
		return list;
	}

}
