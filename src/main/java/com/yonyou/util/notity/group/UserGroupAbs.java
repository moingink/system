package com.yonyou.util.notity.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public abstract class UserGroupAbs {
	
	
	protected  String defWhereKey ="ID";
	
	/**
	 * 获取用户信息
	 * @param group_values
	 * @return
	 */
	public abstract List<Map<String,Object>> findUserGroup(IBaseDao baseDao,Map<String,Object> dataMap,String ... group_values);
	
	/**
	 * 构建查询帮助类
	 * @param group_values
	 * @return
	 */
	protected SqlWhereEntity bulidWhereEntity(String ... group_values){
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		WhereEnum type =WhereEnum.EQUAL_STRING;
		if(group_values!=null&&group_values.length>0){
			int length =group_values.length;
			if(length>1){
				type=WhereEnum.IN;
			}
			whereEntity.putWhere(findWhereKey(), this.findValues(group_values), type);
		}
		return whereEntity ;
	}
	
	protected String findValues(String ... group_values){
		StringBuffer values =new StringBuffer();
		if(group_values!=null&&group_values.length>0){
			for(String value:group_values){
				values.append(value).append(",");
			}
			if(values.length()>0){
				values.setLength(values.length()-1);
			}
		}
		return values.toString();
	}
	
	protected  String findWhereKey(){
		return defWhereKey;
	}

	
	
}
