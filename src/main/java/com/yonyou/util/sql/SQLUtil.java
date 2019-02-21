package com.yonyou.util.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.BussnissException;
/**
 * 新增、修改SQL工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class SQLUtil  extends DataTableUtil{
	 /** 主键标示 */
	 public static String ID ="ID";
	 /** 分割标示*/
	 public static String QUESTION_MARK ="?";
	 /** 新增语句构造器 */
	 private static BulidInsOrUp  bulidInsert =new BulidInsertSql();
	 /** 修改语句构造器 */
	 private static BulidInsOrUp  bulidUpdate =new BulidUpdateSql();
	 /** 删除语句构造器 */
	 private static BulidInsOrUp  bulidDelete =new BulidDeleteSql();
	 /** sql标示 */
	 public static String MAP_SQL_KEY ="sql";
	 /** 数组标示 */
	 public static String MAP_ARRAY_KEY="arrayObj";
	 /** 汇总长度标示 */
	 public static String COUNT_LENGTH ="COUNT_LENGTH";
	 /** 版本标示 */
	 public static String VERSION="VERSION";
	 /**
	  * 空MAP
	  */
	 public static Map<String,Object> nullMap =new HashMap<String,Object>();
	 
	 /**
	  * 获取数据长度语句
	  * @param tableEntity 表工具类
	  * @return SQL语句
	  */
	 public static String queryLengthSql(SqlTableUtil tableEntity){
		 StringBuffer queryLengthSql =new StringBuffer();
		 String selectSql =findSelectSql(tableEntity, tableEntity.findJoinType());
		 queryLengthSql.append("SELECT COUNT(*)  AS ")
		 			   .append(COUNT_LENGTH).append(" FROM (").append(selectSql).append(" ) tmp");
		 return queryLengthSql.toString();
	 }
	 
	 /**
	  * 获取SQL查询语句
	  * @param table 表名
	  * @param selFiled 查询列信息
	  * @param where 查询语句
	  * @param orderby 排序信息
	  * @param groupby 分组信息
	  * @return  SQL查询语句
	  */
	 public static String findSelectSql(String table,String selFiled,String where,String orderby,String groupby){
		 StringBuffer sql =new StringBuffer();
		 sql.append("SELECT ").append(selFiled).append(" FROM ").append(table);
		 if(!isEmpty(where)){
			 sql.append(" WHERE ").append(where);
		 }
		 if(!isEmpty(groupby)){
			 sql.append(" GROUP BY ").append(groupby);
		 }
		 if(!isEmpty(orderby)){
			 sql.append(" ORDER BY ").append(orderby);
		 }else{
			 sql.append(" ORDER BY ").append(" id desc");
			 
		 }
		 return sql.toString();
	 }
	 
	 /**
	  * 获取查询语句
	  * @param tableEntity  表工具类
	  * @return SQL查询语句
	  */
	 public static String findSelectSql(SqlTableUtil tableEntity){
		 return findSelectSql(tableEntity,tableEntity.findJoinType());
	 }
	 
	 /**
	  * 获取查询语句
	  * @param tableEntity 表工具类
	  * @param joinType 关联方向
	  * @return SQL查询语句
	  */
	 public static String findSelectSql(SqlTableUtil tableEntity,String joinType){
		
		 StringBuffer table =new StringBuffer();
		 String selFiled =mapToString(tableEntity.getTableMap().get(FILED_SEL_CODE));
		 String where =mapToStringForWhere(tableEntity.getTableMap().get(WHERE));
		 StringBuffer appendJoinWhere =new StringBuffer();
		 String groupby =mapToString(tableEntity.getTableMap().get(GROUP_BY));
		 String orderby =mapToStringForAPP(tableEntity.getTableMap().get(ORDER_BY));
		 table.append(tableEntity.getTable()).append(" ").append(tableEntity.getAlias_table_name());
		 if(tableEntity.getTableMap().containsKey(JOIN_TABLE)){
			 if(joinType!=null&&!joinType.equals("")){
				 for(String str:tableEntity.getTableMap().get(JOIN_TABLE).keySet()){
					 table.append(" ").append(joinType.toUpperCase()).append(" JOIN ").append(str).append(" ON ").append(tableEntity.getTableMap().get(JOIN_TABLE).get(str));
				 }
			 }else{
				 String joinTableStr =mapToString(tableEntity.getTableMap().get(JOIN_TABLE));
				 if(!joinTableStr.equals("")){
					 table.append(" , ").append(joinTableStr);
				 }
				 for(String str:tableEntity.getTableMap().get(JOIN_TABLE).keySet()){
					 appendJoinWhere.append(" ").append(tableEntity.getTableMap().get(JOIN_TABLE).get(str)).append(" AND");
				 }
				 if(appendJoinWhere.length()>3){
					 appendJoinWhere=appendJoinWhere.delete(appendJoinWhere.length()-3, appendJoinWhere.length());
					 if(where.length()>0){
						 where =where+" AND "+appendJoinWhere.toString();
					 }else{
						 where =appendJoinWhere.toString();
					 }
				 }
				 
			 }
		 }
		 
		 return findSelectSql(table.toString(), selFiled, where, orderby, groupby);
	 }
	 
	 
	 /**
	  * map 转字符串  keys
	  * @param sqlMap 数据信息
	  * @return 拼装后的信息
	  */
	 public static String mapToString (Map<String,String> sqlMap){
		 StringBuffer mapToString =new StringBuffer();
		 if(sqlMap!=null){
			 for(String str:sqlMap.keySet()){
				 mapToString.append(str).append(",");
			 }
			 if(mapToString.length()>=1){
				 mapToString=mapToString.delete(mapToString.length()-1, mapToString.length());
			 }
		 }
		 
		 return mapToString.toString();
	 }
	 
	 /**
	  * map 转字符串  key value s
	  * @param sqlMap 数据
	  * @return 拼装后的信息
	  */
	 public static String mapToStringForAPP(Map<String,String> sqlMap){
		 StringBuffer mapToString =new StringBuffer();
		 if(sqlMap!=null){
			 for(String str:sqlMap.keySet()){
				 mapToString.append(str).append(" ").append(sqlMap.get(str)).append(" ").append(",");
			 }
			 if(mapToString.length()>=1){
				 mapToString=mapToString.delete(mapToString.length()-1, mapToString.length());
			 }
		 }
		 
		 return mapToString.toString();
	 }
	 
	 /**
	  * map 字符串 用于where
	  * @param sqlMap 数据
	  * @return 拼装后的信息
	  */
	 public static  String mapToStringForWhere (Map<String,String> sqlMap){
		 StringBuffer mapToString =new StringBuffer("");
		 if(sqlMap!=null){
			 for(String str:sqlMap.keySet()){
				 if(!isEmpty(str)){
					 mapToString.append(" ").append(str).append(" ").append(sqlMap.get(str));
				 }
			 }
			 if(mapToString.length()>1){
				 int length =mapToString.length();
				 String and=mapToString.substring(length-3, length);
				 String or =mapToString.substring(length-2, length);
				 if(and.toLowerCase().endsWith("and")){
					 mapToString=mapToString.delete(length-3,length );
				 }
				 if(or.toLowerCase().endsWith("or")){
					 mapToString=mapToString.delete(length-2,length );
				 }
			 }
		 }
		 
		 return mapToString.toString();
	 }
	 
	 /**
	  * 获取修改信息  注:另一个map 需要上元数据中获取
	  * @param table 表名
	  * @param entity 数据信息
	  * @param whereEntity 查询条件工具类
	  * @return 修改信息
	  * @throws BussnissException 
	  */
	 public static InsUpSqlEntity findUpdate(String table ,Map<String , String> entity,SqlWhereEntity whereEntity) throws BussnissException{
		 
		 return findUpdateForObject(table,StringToObjUtil.stringToObjForMap(entity, null), whereEntity);
	 }
	 
	 /**
	  * 获取修改信息
	  * @param table 表名
	  * @param entity 数据信息
	  * @param whereEntity 查询条件工具类
	  * @return 修改信息
	  */
	 public static InsUpSqlEntity findUpdateForObject(String table ,Map<String , Object> entity,SqlWhereEntity whereEntity){
		 
		 return bulidInsOrUpSql(bulidUpdate, table, entity, whereEntity.getWhereMap());
	 }
	 
	 
	 /**
	  * 获取删除信息
	  * @param table 表名
	  * @param entity 数据信息
	  * @param whereEntity 查询条件工具类
	  * @return 删除信息
	  */
	 public static InsUpSqlEntity findDeleteForObject(String table ,Map<String , Object> entity,SqlWhereEntity whereEntity){
		 
		 return bulidInsOrUpSql(bulidDelete, table, entity, whereEntity.getWhereMap());
	 }
	 
	 /**
	  * 获取修改信息 用于list数据 并通过数据元转换数据类型
	  * @param table 表名
	  * @param entityList 数据集合
	  * @param whereEntity 查询条件工具类
	  * @return 修改信息
	  * @throws BussnissException
	  */
	 public static InsUpSqlEntity findUpdate(String table ,List<Map<String , String>> entityList,SqlWhereEntity whereEntity) throws BussnissException{
		 //Map<String,String > metaDataMap  =find
		 return bulidInsOrUpSql(bulidUpdate, table,StringToObjUtil.stringToObjForListMap(entityList, null) ,  whereEntity.getWhereMap());
	 }
	 
	 /**
	  * 获取修改信息
	  * @param table 表名
	  * @param entityList 数据集合
	  * @param whereEntity 查询条件工具类
	  * @return 修改信息
	  */
	 public static InsUpSqlEntity findUpdateObject(String table ,List<Map<String , Object>> entityList,SqlWhereEntity whereEntity){
		 if(whereEntity==null){
			 return bulidInsOrUpSql(bulidUpdate, table, entityList,  null);
		 }else{
			 return bulidInsOrUpSql(bulidUpdate, table, entityList,  whereEntity.getWhereMap());
		 }
		 
	 }
	 /**
	  * 获取新增数据
	  * @param table 表名
	  * @param entity 数据
	  * @return 新增数据
	  */
	 public static InsUpSqlEntity findInsert(String table,Map<String,Object> entity){
		 return bulidInsOrUpSql(bulidInsert, table, entity, new SqlWhereEntity().getWhereMap());
	 }
	 
	 /**
	  * 获取新增数据
	  * @param table 表名
	  * @param entityList 数据集合
	  * @param whereEntity 查询语句工具类
	  * @return 新增数据
	  */
	 public static InsUpSqlEntity findInsert(String table,List<Map<String,Object>> entityList,SqlWhereEntity whereEntity){
		 return bulidInsOrUpSql(bulidInsert, table, entityList, whereEntity.getWhereMap());
	 }
	 
	
	 /**
	  * 获取新增/修改数据信息
	  * @param bulidSQL 语句构造器
	  * @param table 表名
	  * @param entity 数据
	  * @param whereMap 查询语句工具类
	  * @return 新增/修改数据
	  */
	 private static InsUpSqlEntity bulidInsOrUpSql(BulidInsOrUp bulidSQL,String table,Map<String,Object> entity,Map<String,String> whereMap){
		 List<Map<String,Object>> entityList =new ArrayList<Map<String,Object>>();
		 if(entity==null){
			 entityList.add(nullMap);
		 }else{
			 entityList.add(entity);
		 }
		 
		 return bulidInsOrUpSql(bulidSQL, table, entityList, whereMap);
	 }
	 
	 /**
	  * 获取新增/修改数据信息
	  * @param bulidSQL 语句构造器
	  * @param table 表名
	  * @param entityList 数据集合
	  * @param whereMap 查询语句工具类
	  * @return 新增/修改数据
	  */
	 private static InsUpSqlEntity bulidInsOrUpSql(BulidInsOrUp bulidSQL,String table,List<Map<String,Object>> entityList,Map<String,String> whereMap ){
		 InsUpSqlEntity sqlEntity =new InsUpSqlEntity();
		 String sql ="";
		 Object [][] arrayObj =null;
		 List<String> rowList =new ArrayList<String>();
		 if(entityList!=null&&entityList.size()>=1){
			 int i =0;
			 arrayObj =new Object[entityList.size()][];
			 sql =bulidSQL.bulidSql(table, entityList.get(0), rowList, whereMap);
			 for(Map<String,Object> entity:entityList){
				 List<Object> listObj =new ArrayList<Object>();
				 for(String filed:rowList){
					 listObj.add(entity.get(filed));
				 }
				 arrayObj[i]=listObj.toArray();
				 i++;
				 listObj.clear();
			 }
		 }
		 sqlEntity.setSql(sql);
		 sqlEntity.setArrayObj(arrayObj);
		 return sqlEntity;
	 }
	 /**
	  * 获取where条件语句
	  * @param filed 字段
	  * @param value 字段值
	  * @param whereEnum 查询条件枚举
	  * @return where条件语句
	  */
	 public static String findWhereSql(String filed,String value,WhereEnum whereEnum){
		 StringBuffer where =new StringBuffer();
		 String mark="#";
		 value =findWhereSqlValueForIn(value,whereEnum);
		where.append(whereEnum.getFiledFormat().replaceAll(mark, filed)).append(" ")
			 .append(whereEnum.getVal()).append(" ")
			 .append(whereEnum.getValFormat().replaceAll(mark, value)).append(" ");
		return where.toString();
	 }
	 
	 /**
	  * 获取查询语句 对于IN类型进组特殊转换
	  * @param value 值信息
	  * @param whereEnum where语句类型
	  * @return 转换VALUE信息
	  */
	 private static String findWhereSqlValueForIn(String value,WhereEnum whereEnum){
		 StringBuffer in_value =new StringBuffer();
		 if(whereEnum.equals(WhereEnum.IN)){
			 if(value.indexOf(",")!=-1){
				 String [] values =value.split(",");
				 for(String val:values){
					 in_value.append("'").append(val).append("'").append(",");
				 }
			 }else{
				 in_value.append("'").append(value).append("'").append(",");
			 }
			 if(in_value.length()>1){
				 in_value.setLength(in_value.length()-1);
			 }
		 }else{
			 in_value.append(value);
		 }
		 return in_value.toString();
	 }
	 
	 /**
	  * 判断是否为空
	  * @param str 数据
	  * @return TRUE OR FALSE
	  */
	 public static boolean isEmpty(String str){
		 boolean isEmpty =true;
		 if(str!=null&&str.length()>=1&&!str.trim().equals("")){
			 isEmpty=false;
		 }
		 return isEmpty;
	 }
	 
	 /**
	  * WHERE 枚举信息
	  * @author moing_ink
	  * <p>创建时间 ： 2016年12月26日
	  * @version 1.0
	  */
	 public static enum WhereEnum{
		 
		 /** 小于 */
		 LESS_THAN("<","#","#"),
		 /** 等于 用于字符串*/
		 EQUAL_STRING("=","#","'#'"),
		 /** 等于 用于数值 */
		 EQUAL_INT("=","#","#"),
		 /** 不等于 用于字符串 */
		 NOT_EQUAL_STRING("!=","#","'#'"),
		 /** 不等于 用于数值 */
		 NOT_EQUAL_INT("!=","#","#"),
		 /** 大于 */
		 GREATER_THAN(">","#","#"),
		 /** 前后匹配 */
		 ALL_LIKE("LIKE","#","'%#%'"),
		 /** 前匹配 */
		 FRONT_LIKE("LIKE","#","'%#'"),
		 /** 后匹配*/
		 BACK_LIKE("LIKE","#","'#%'")
		 /** IN*/
		 ,IN("IN","#","(#)")
		 ,NOT_IN("NOT IN","#","(#)"),
		 /** IS NULL */
		 IS_NULL("IS","#","NULL"),
		 /** IS NOT NULL */
		 IS_NOT_NULL("IS NOT","#","NULL"),
		 /** 日期大于   */
		 TO_DATE_LESS("<","#","'#'"),
		 /** 日期等于*/
		 TO_DATE_EQUAL("=","#","'#'"),
		 /** 日期小于 */
		 TO_DATE_GREATER(">","#","'#'");
		 
		 /** 日期大于   *//*
		 TO_DATE_LESS("<","#","TO_DATE('#','YYYY-MM-DD')"),
		 *//** 日期等于*//*
		 TO_DATE_EQUAL("=","#","TO_DATE('#','YYYY-MM-DD')"),
		 *//** 日期小于 *//*
		 TO_DATE_GREATER(">","#","TO_DATE('#','YYYY-MM-DD')");*/
		 
		 /** 值*/
		 private String val;
		 /** 字段格式类型*/
		 private String field_format;
		 /** 值格式类型*/
		 private String val_format;
		 WhereEnum(String _val,String _filedformat,String _valformat){
			 val=_val;
			 field_format=_filedformat;
			 val_format=_valformat;
		 }
		 /**
		  * 获取值信息
		  * @return
		  */
		 public String getVal(){
			 return val;
		 }
		 /**
		  * 获取字段格式化信息
		  * @return
		  */
		 public String getFiledFormat(){
			 return field_format;
		 }
		 /**
		  * 获取值格式化信息
		  * @return
		  */
		 public String getValFormat(){
			 return val_format;
		 }
	 }
	 
	 
	 public static void main(String [] args){
		String abc ="SELECT * FROM AAA WHERE AA.A1='1232'";
		System.out.println("SELECT COUNT(*) FROM "+ abc.split("FROM")[1]);
		
		System.out.println(SQLUtil.findWhereSqlValueForIn("1,2,3,4", WhereEnum.IN));
	 }
	 
}
