package com.yonyou.util.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 表工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class SqlTableUtil extends DataTableUtil {
	
	/**
	 * 表名
	 */
	private String table="";
	/**
	 * 别名
	 */
	private String alias_table_name ="";
	
	/**
	 * 
	 */
	private String filed_class="";
	
	
	private String where_class="";
	
	
	private String joinType="";
	/**
	 * 表信息
	 */
	public Map<String,Map<String,String>> tableMap =new HashMap<String,Map<String,String>>();
	
	/**
	 * 初始化
	 * @param _table 表名
	 * @param _alias_table_name 别名
	 */
	public SqlTableUtil(String _table,String _alias_table_name){
		table=_table;
		alias_table_name=_alias_table_name;
	}
	/**
	 * 初始化
	 * @param _table 表名
	 * @param _alias_table_name 别名
	 * @param _filed_class 过滤类
	 */
	public SqlTableUtil(String _table,String _alias_table_name,String _filed_class){
		table=_table;
		alias_table_name=_alias_table_name;
		filed_class=_filed_class;
	}
	
	/**
	 * 初始化方法
	 * @param selFileds 查询列
	 * @param where 查询条件
	 */
	public void init(String selFileds,String where){
		init(selFileds,selFileds,where);
	}
	
	/**
	 * 根据查询项，显示项 以及查询条件 进行初始化数据
	 * @param selFileds 查询列
	 * @param showFileds 展示列
	 * @param where 查询条件
	 */
	public void init(String selFileds,String showFileds,String where){
		
		String [] selFileArray =stringToArray(selFileds,",");
//		FIXME 展示字段中可能包括TNAME函数（表示待翻译字段），不能使用','分割字段，暂定'&'
		String [] showFiledArray =stringToArray(showFileds,"&");
		String [] whereArray =new String[]{where};
		
		this.copyArrayForMark(FILED_SEL_CODE, selFileArray);
		this.copyArrayForMark(FILED_SHOW_CODE, showFiledArray);
		this.copyArrayForMark(WHERE, whereArray);
		
	}
	
	
	/**
	 * 添加关联表
	 * @param tableName 表名
	 * @return
	 */
	public SqlTableUtil appendJoinTable(String tableName){
		
		this.appendJoinTable(tableName, "");
		return this;
	}
	/**
	 * 添加表关联
	 * @param tableName 表名
	 * @param alias_table_name 别名
	 * @param join_on_str 关联条件
	 * @return
	 */
	public SqlTableUtil appendJoinTable(String tableName,String alias_table_name,String join_on_str){
		String table =tableName+" "+alias_table_name;
		this.append(JOIN_TABLE, table, join_on_str);
		return this;
	}
	
	/**
	 * 添加关联表
	 * @param tableName 表名
	 * @param join_on_str 关联条件
	 * @return
	 */
	public SqlTableUtil appendJoinTable(String tableName,String join_on_str){
		
		this.append(JOIN_TABLE, tableName, join_on_str);
		return this;
	}
	
	/**
	 * 添加表关联
	 * @param join 关联工具类
	 * @param join_on_str 关联条件
	 * @return
	 */
	public SqlTableUtil joinTable(SqlTableUtil join,String join_on_str){
		
		String table =join.getTable();
		String alias_table=join.getAlias_table_name();
		if(alias_table!=null&&!alias_table.equals("")){
			table=table+" "+alias_table;
		}
		this.append(JOIN_TABLE, table, join_on_str);
		this.copyJoin(join);
		return this;
	}
	
	/**
	 * 复制数据
	 * @param join 工具类
	 */
	private void copyJoin(SqlTableUtil join){
		
		copyMapForMark(FILED_SEL_CODE,join.getTableMap().get(FILED_SEL_CODE));
		copyMapForMark(FILED_SHOW_CODE,join.getTableMap().get(FILED_SHOW_CODE));
		copyMapForMark(WHERE,join.getTableMap().get(WHERE));
		copyMapForMark(GROUP_BY,join.getTableMap().get(GROUP_BY));
		copyMapForMark(ORDER_BY,join.getTableMap().get(ORDER_BY));
	}
	
	/**
	 * 根据类型复制数据
	 * @param mark 分割标示
	 * @param map copy信息
	 */
	private void copyMapForMark(String mark,Map<String,String> map){
		
		if(map!=null&&map.size()>0){
			for(String str:map.keySet()){
				if(mark.equals(FILED_SEL_CODE)){
					if(str.indexOf(".")!=-1){
						this.appendSelFiled("", str);
					}else{
						this.appendSelFiled(alias_table_name, str);
					}
					
				}else if(mark.equals(FILED_CODE_TO_NAME)){
					this.appendFiledName(str, map.get(str));
				}else if(mark.equals(FILED_SHOW_CODE)){
					this.appendShowFiled(str);
				}
				else if(mark.equals(GROUP_BY)){
					this.appendGroup(str);
				}else if(mark.equals(ORDER_BY)){
					this.appendOrderBy(str, map.get(str));
				}
			}
			
			if(mark.equals(WHERE)){
				String where =SQLUtil.mapToStringForWhere(map);
				if(!SQLUtil.isEmpty(where)){
					where =" ("+where+") ";
					this.appendWhereAnd(where);
				}
				
			}
		}
	}
	
	/**
	 * 添加where条件 根据MAP
	 * @param map 写入数据
	 */
	public void appendWhereMap(Map<String,String> map){
		
		this.appendMarkMap(WHERE,map);
	}
	
	/**
	 * 添加where条件 根据MAP
	 * @param map 写入数据
	 */
	public SqlTableUtil appendSqlWhere(SqlWhereEntity sqlWhere){
		
		for(String key:sqlWhere.getWhereMap().keySet()){
			appendWhereAnd(sqlWhere.getWhereMap().get(key));
		}
		return this;
	}
	
	/**
	 * 添加查询字段信息     根据MAP
	 * @param map 写入数据
	 */
	public void appendSelFiledMap(Map<String,String> map){
		
		this.appendMarkMap(FILED_SEL_CODE,map);
	}
	
	/**
	 * 添加字段名称信息  根据MAP
	 * @param map 写入数据
	 */
	public void appendFileNameMap(Map<String,String> map){
		this.appendMarkMap(FILED_CODE_TO_NAME, map);
	}
	
	/**
	 * 添加MAP信息
	 * @param mark 分割标示
	 * @param map 写入数据
	 */
	public void appendMarkMap(String mark,Map<String,String> map){
		this.copyMapForMark(mark, map);
	}
	/**
	 * 复制数据信息到MAP	
	 * @param mark 分割标示
	 * @param arrays 复制信息
	 */
	private void copyArrayForMark(String mark,String [] arrays){
		StringBuffer selStrBuf =new StringBuffer();
		boolean isFunction =false;
		if(arrays!=null&&arrays.length>0){
			for(String str:arrays){
				
				if(mark.equals(FILED_SEL_CODE)){
					
					if(str.indexOf("(")!=-1){
						isFunction=true;
					}
					if(str.indexOf(")")!=-1){
						isFunction=false;
					}
					
					if(!isFunction){
						selStrBuf.append(str);
						if(str.indexOf(".")!=-1||str.indexOf("(")!=-1||str.indexOf(")")!=-1){
							this.appendSelFiled("", selStrBuf.toString());
						}else{
							this.appendSelFiled(alias_table_name, selStrBuf.toString());
						}
						selStrBuf.setLength(0);
					}else{
						selStrBuf.append(str).append(",");
					}
					
					
				}
				if(mark.equals(FILED_SHOW_CODE)){
					this.appendShowFiled(str);
				}
			}
			
			if(mark.equals(WHERE)){
					this.appendWhereAnd(arrays[0]);
			}
		}
	}
	
	/**
	 * 字符串转换成数据
	 * @param strs 字符串信息
	 * @param splitMark 分组标示
	 * @return 转换后的数组信息
	 */
	private String[] stringToArray(String strs,String splitMark){
		String [] strArray =null;
		if(strs.indexOf(splitMark)!=-1){
			strArray =strs.split(splitMark);
		}else{
			strArray =new String[]{strs};
		}
		return strArray;
	}
			
	/**
	 * 添加字段名称
	 * @param filed 字段
	 * @param filedName 字段名称
	 * @return 工具类
	 */
	public SqlTableUtil appendFiledName(String filed,String filedName){
		
		this.append(FILED_CODE_TO_NAME, filed, filedName);
		return this;
	}
	
	/**
	 * 添加查询字段信息
	 * @param filed 字段
	 * @return 工具类
	 */
	public SqlTableUtil appendSelFiled(String filed){
		return this.appendSelFiled(this.alias_table_name, filed);
	}
	/**
	 * 根据别名添加查询字段信息 
	 * @param alias_table_name 别名
	 * @param filed 字段
	 * @return 工具类
	 */
	public SqlTableUtil appendSelFiled(String alias_table_name,String filed){
		if(alias_table_name!=null&&!alias_table_name.equals("")&&filed.indexOf("(")==-1){
			if(filed.indexOf(".")==-1){
				filed=alias_table_name+"."+filed;
			}
		}
		this.append(FILED_SEL_CODE, filed, filed);
		return this;
	}
	/**
	 * 添加展示字段信息
	 * @param filed 字段
	 * @return 工具类
	 */
	public SqlTableUtil appendShowFiled(String filed){
		this.append(FILED_SHOW_CODE, filed, filed);
		return this;
	}
	
	/**
	 * 添加排序信息
	 * @param order 排序字段
	 * @param sort 排序方式
	 * @return 工具类
	 */
	
	public SqlTableUtil appendOrderBy(String order,String sort){
		return appendOrderByForAliasTableName(order,alias_table_name,sort);
	}
	
	/**
	 * 根据别名 添加排序信息
	 * @param order 排序字段
	 * @param alias_table_name 表别名
	 * @param sort 排序方式
	 * @return 工具类
	 */
	public SqlTableUtil appendOrderByForAliasTableName(String order,String alias_table_name,String sort){
		if(alias_table_name!=null&&!alias_table_name.equals("")){
			order=alias_table_name+"."+order;
		}
		this.append(ORDER_BY, order, sort);
		return this;
	}
	/**
	 * 添加排序信息  默认倒序
	 * @param order 排序字段
	 * @return 工具类
	 */
	public SqlTableUtil appendOrderBy(String order){
		
		return this.appendOrderBy(order, DESC);
	}
	/**
	 * 根据别名添加排序信息 默认倒序
	 * @param order 排序字段
	 * @param alias_table_name 表别名
	 * @return 工具类
	 */
	public SqlTableUtil appendOrderByForAliasTableName(String order,String alias_table_name){
		return this.appendOrderByForAliasTableName(order,alias_table_name, DESC);
	}
	
	/**
	 * 添加分组信息
	 * @param group 分组字段
	 * @return 工具类
	 */
	public SqlTableUtil appendGroup(String group){
		return this.appendGroup(group, alias_table_name);
	}
	/**
	 * 根据别名 添加分组信息  
	 * @param group 分组字段
	 * @param alias_table_name 别名
	 * @return 工具类  
	 */
	public SqlTableUtil appendGroup(String group,String alias_table_name){
		if(alias_table_name!=null&&!alias_table_name.equals("")&&group.indexOf("(")==-1){
			group=alias_table_name+"."+group;
		}
		this.append(GROUP_BY, group, group);
		return this;
	}
	
	
	/**
	 * 添加  and where语句
	 * @param whereStr 查询条件
	 * @return 添加分组信息  
	 */
	public SqlTableUtil appendWhereAnd(String whereStr){
		
		this.appendAndOr(whereStr, "AND");
		return this;
	}
	
	/**
	 * 添加 or where语句
	 * @param whereStr 查询条件
	 * @return 添加分组信息  
	 */
	public SqlTableUtil appendWhereOr(String whereStr){
		
		this.appendAndOr(whereStr, "OR");
		return this;
	}
	
	/**
	 * 添加 where语句 基础方法 and/or 
	 * @param whereStr 查询语句
	 * @param andOr 标示 and / or
	 */
	private void appendAndOr(String whereStr,String andOr){
		
		//TODO MOING 根据过滤类，来进行查询条件翻译
		if(!SQLUtil.isEmpty(this.getWhere_class())){
			
		}
		String endKey =this.findEndKey(WHERE);
		if(endKey!=null&&!endKey.equals("")){
			this.append(WHERE, endKey,andOr);
		}
		this.append(WHERE, whereStr,"");
	}
	/**
	 * 添加where语句
	 * @param whereStr 查询语句
	 * @param andOr 标示
	 * @return 工具类
	 */
	private SqlTableUtil appendWhere(String whereStr,String andOr){
		
		this.append(WHERE, whereStr, andOr);
		return this;
	}
	
	/**
	 * 添加信息基础方法
	 * @param mark 标示
	 * @param key 类型
	 * @param value 值
	 * @return 工具类
	 */
	private SqlTableUtil append(String mark,String key,String value){
		
		Map<String,String> map =null;
		if(tableMap.containsKey(mark)){
			map =tableMap.get(mark);
			this.remove(mark, key);
		}else{
			map =new LinkedHashMap<String,String>();
			tableMap.put(mark,map);
		}
		map.put(key, value);
		
		return this;
	}
	
	/**
	 * 获取最后类型
	 * @param mark 标示
	 * @return 工具类
	 */
	private String findEndKey(String mark){
		String endKey ="";
		if(tableMap.containsKey(mark)){
			for(String key:tableMap.get(mark).keySet()){
				endKey =key;
			}
		}
		return endKey;
	}
	
	/**
	 * 移除数据信息
	 * @param mark 标示
	 * @param key 类型
	 * @return 工具类
	 */
	public SqlTableUtil remove(String mark,String key){
		
		if(tableMap.containsKey(mark)){
			if(tableMap.get(mark).containsKey(key)){
				tableMap.get(mark).remove(key);
			}
		}
		return this;
	}
	
	/**
	 * 根据标示 移除当前标示下全部数据
	 * @param mark 标示
	 * @return 工具类
	 */
	public SqlTableUtil remove(String mark){
		
		if(tableMap.containsKey(mark)){
			tableMap.remove(mark);
		}
		return this;
	}
	
	/**
	 * 设置字段名称
	 * @param filedNameMap 字段名称对应数据
	 * @return 工具类
	 */
	public SqlTableUtil setFiledName(Map<String,String> filedNameMap){
		this.copyMapForMark(FILED_SHOW_CODE, filedNameMap);
		return this;
	}
	
	/**
	 * 获取表名
	 * @return 表名
	 */
	public String getTable() {
		return table;
	}
	/**
	 * 修改表名 
	 * @param table 表名
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * 获取表别名
	 * @return 表别名
	 */
	public String getAlias_table_name() {
		return alias_table_name;
	}
	/**
	 * 修改表别名
	 * @param alias_table_name 表别名
	 */
	public void setAlias_table_name(String alias_table_name) {
		this.alias_table_name = alias_table_name;
	}
	/**
	 * 获取工具类，全部数据
	 * @return 数据
	 */
	public Map<String, Map<String, String>> getTableMap() {
		return tableMap;
	}
	
	/**
	 * 获取展示列数据
	 * @return 数据集合
	 */
	public Map<String,String> getShowFiledMap(){
		Map<String,String> showMap =new HashMap<String,String>();
		if(tableMap.containsKey(FILED_SHOW_CODE)){
			showMap=tableMap.get(FILED_SHOW_CODE);
		}
		return showMap;
	}
	
	/**
	 * 获取查询条件数据
	 * @return 数据集合
	 */
	public Map<String,String> getWhereMap(){
		Map<String,String> whereMap =new HashMap<String,String>();
		if(tableMap.containsKey(WHERE)){
			whereMap=tableMap.get(WHERE);
		}
		return whereMap;
	}
	
	/**
	 * 获取分组数据 
	 * @return 数据集合
	 */
	public Map<String,String> getGroupMap(){
		Map<String,String> groupMap =new HashMap<String,String>();
		if(tableMap.containsKey(GROUP_BY)){
			groupMap=tableMap.get(GROUP_BY);
		}
		return groupMap;
	}
	/**
	 * 获取字段名称数据
	 * @return 数据集合
	 */
	public Map<String,String> getFiledNameMap(){
		Map<String,String> showMap =new HashMap<String,String>();
		if(tableMap.containsKey(FILED_CODE_TO_NAME)){
			showMap=tableMap.get(FILED_CODE_TO_NAME);
		}
		return showMap;
	}
	/**
	 * 设置 工具类 全部数据
	 * @param tableMap 工具类数据
	 */
	public void setTableMap(Map<String, Map<String, String>> tableMap) {
		this.tableMap = tableMap;
	}
	
	/**
	 * 清除对象
	 */
	public void clearTableMap(){
		if(this.getTableMap()!=null){
			for(String key:this.getTableMap().keySet()){
				Map<String,String> map=getTableMap().get(key);
				map.clear();
				map=null;
			}
			this.getTableMap().clear();
			tableMap=null;
		}
	}
	/**
	 * 复制对象
	 * @return
	 */
	public  SqlTableUtil copyTable(){
		SqlTableUtil sqlTableEntity =new SqlTableUtil(this.table,this.alias_table_name);
		Map<String,Map<String,String>> tableEntity =new HashMap<String,Map<String,String>>();
		sqlTableEntity.setTableMap(tableEntity);
		if(this.getTableMap()!=null&&this.getTableMap().size()>0){
			for(String key:this.getTableMap().keySet()){
				Map<String,String> linkMap =new LinkedHashMap<String, String>();
				tableEntity.put(key, linkMap);
				for(String keyL:this.getTableMap().get(key).keySet()){
					linkMap.put(keyL, getTableMap().get(key).get(keyL));
				}
			}
		}
		return sqlTableEntity;
	}
	/**
	 * 获取过滤类
	 * @return
	 */
	public String getFiled_class() {
		return filed_class;
	}
	/**
	 * 设置过滤类
	 * @param filed_class
	 */
	public void setFiled_class(String filed_class) {
		this.filed_class = filed_class;
	}
	/**
	 * 获取查询条件过滤类
	 * @return
	 */
	public String getWhere_class() {
		return where_class;
	}
	/**
	 * 设置查询条件过滤类
	 * @param where_class
	 */
	public void setWhere_class(String where_class) {
		this.where_class = where_class;
	}
	/**
	 * 设置 json 关联类型
	 * @param joinEnum json管理枚举
	 * @see JoinEnum 关联枚举类
	 */
	public void setJoinType(JoinEnum joinEnum){
		if(joinEnum!=null){
			joinType=joinEnum.getType();
		}
	}
	/**
	 * 获取关联类型
	 * @return
	 */
	public String findJoinType(){
		return joinType;
	}
	
	
}
