package com.yonyou.util.sql;

import java.util.List;
import java.util.Map;

/**
 * 拼装修改语句类
 * @author moing_ink
 *
 */
public abstract class BulidInsOrUp {
	
	/**
	 * 获取头信息
	 * @param sql 语句
	 * @param table 表名
	 */
	protected abstract void findHead(StringBuffer sql,String table);
	/**
	 * 获取内容信息
	 * @param sql 语句
	 * @param rowList 数据
	 * @param filed 拼装字段
	 * @param whereMap 查询条件集合
	 */
	protected abstract void findMain(StringBuffer sql,List<String> rowList,String filed,Map<String,String> whereMap);
	/**
	 * 获取结尾信息
	 * @param sql 语句
	 * @param rowList 数据
	 * @param whereMap 查询条件集合
	 */
	protected abstract void findEnd(StringBuffer sql,List<String> rowList,Map<String,String> whereMap);
	
	/**
	 * 拼装语句
	 * @param table 表名
	 * @param entity  拼装列实体
	 * @param rowList 数据
	 * @param whereMap 查询条件集合
	 * @return
	 */
	public String bulidSql(String table,Map<String,Object> entity,List<String> rowList,Map<String,String> whereMap){
		StringBuffer sql =new StringBuffer();
		this.findHead(sql, table);
		if(entity!=null){
			for(String filed:entity.keySet()){
				this.findMain(sql, rowList, filed, whereMap);
			}
		}
		this.findEnd(sql, rowList, whereMap);
		return sql.toString();
	}
	
	/**
	 * SQL删除一位长度
	 * @param sql
	 * @return
	 */
	public StringBuffer deleteLengthEndOne(StringBuffer sql){
		return sql.delete(sql.length()-1, sql.length());
	}
	
}
