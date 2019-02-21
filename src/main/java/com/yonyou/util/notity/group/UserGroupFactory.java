package com.yonyou.util.notity.group;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.notity.group.impl.UserGroupForCompany;
import com.yonyou.util.notity.group.impl.UserGroupForRole;
import com.yonyou.util.notity.group.impl.UserGroupForUser;
import com.yonyou.util.notity.group.impl.UserGroupForWx;

public class UserGroupFactory {
	
	private static Map<String,UserGroupAbs> GROUP_MAP =new HashMap<String,UserGroupAbs>();
	
	static {
		GROUP_MAP.put("0", new UserGroupForUser());
		GROUP_MAP.put("1", new UserGroupForCompany());
		GROUP_MAP.put("2", new UserGroupForRole());
		GROUP_MAP.put("3", new UserGroupForWx());
	}
	
	
	public static UserGroupAbs findGroup(String type) throws BusinessException{
		if(GROUP_MAP.containsKey(type)){
			return GROUP_MAP.get(type);
		}else{
			throw new BusinessException("没有找到相应的类型：type="+type);
		}
	}
}
