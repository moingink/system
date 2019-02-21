package com.yonyou.util.sql;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.yonyou.util.sql.SQLUtil.WhereEnum;

/**
 * WHERE语句工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class SqlWhereEntity implements Serializable{
	
	/**
	 * where条件map
	 */
	private  Map<String,String> whereMap =new LinkedHashMap<String,String>();
	/**
	 * 表别名
	 */
	private String alis_table;
	/**
	 * 获取 where语句 MAP
	 * @return 数据集合
	 */
	public Map<String, String> getWhereMap() {
		return whereMap;
	}
	/**
	 * 设置where语句map
	 * @param whereMap 添加条件集合
	 */
	public void setWhereMap(Map<String, String> whereMap) {
		this.whereMap = whereMap;
	}
	/**
	 * 添加where语句map信息
	 * @param key 类型
	 * @param value 值
	 * @param whereEnum WHERE枚举信息
	 * @return 工具类
	 */
	public SqlWhereEntity putWhere(String key,String value,WhereEnum whereEnum){
		
		StringBuffer whereBuffer =new StringBuffer();
		if(whereMap.containsKey(key)){
			whereBuffer.append(whereMap.get(key)).append(" AND ");
		}
		whereBuffer.append(SQLUtil.findWhereSql(key, value, whereEnum));
		whereMap.put(key,whereBuffer.toString());
		return this;
	}
    /**
     * 获取表别名
     * @return 表别名
     */
	public String getAlis_table() {
		return alis_table;
	}
	/**
	 * 设置表别名
	 * @param alis_table 表别名
	 */
	public void setAlis_table(String alis_table) {
		this.alis_table = alis_table;
	}
	
	/**
	 * 清除数据
	 */
	public void clear(){
		whereMap.clear();
		alis_table="";
	}
	
	
}
