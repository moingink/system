package com.yonyou.util;

import java.util.List;
import java.util.Map;

import com.yonyou.iuap.utils.PropertyUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

/**
 * 工具类-用于获取数据库各类信息
* @ClassName DBInfoUtil 
* @author hubc
* @date 2018年5月8日
 */
public class DBInfoUtil {

	//MySQL数据库方案名
	public static String schemaName = PropertyUtil.getPropertyByKey("schemaName");
	private static IBaseDao dcmsDAO = BaseDao.getBaseDao();

	/**
	 * 数据中是否存在此表
	* @param tableName
	* @return
	 */
	public static boolean hasExistTable(String tableName){
		SqlTableUtil sqlTableUtil1= new SqlTableUtil("information_schema.TABLES", "").appendSelFiled("TABLE_NAME");
		SqlWhereEntity sqlWhereEntity1 = new SqlWhereEntity()
				.putWhere("TABLE_SCHEMA", DBInfoUtil.schemaName, WhereEnum.EQUAL_STRING)
				.putWhere("TABLE_NAME", tableName, WhereEnum.EQUAL_STRING);
		sqlTableUtil1.appendOrderBy("table_name");
		List<Map<String, Object>> tableInfo = dcmsDAO.query(sqlTableUtil1.appendSqlWhere(sqlWhereEntity1));
		if(tableInfo.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 数据库表中是否存在此字段
	* @param tableName
	* @param columnName
	* @return
	 */
	public static boolean hasExistCloumn(String tableName, String columnName){
		SqlTableUtil sqlTableUtil2 = new SqlTableUtil("information_schema.COLUMNS", "").appendSelFiled("COLUMN_NAME");
		SqlWhereEntity sqlWhereEntity2 = new SqlWhereEntity()
				.putWhere("TABLE_SCHEMA", DBInfoUtil.schemaName,WhereEnum.EQUAL_STRING)
				.putWhere("TABLE_NAME", tableName,WhereEnum.EQUAL_STRING)
				.putWhere("COLUMN_NAME", columnName, WhereEnum.EQUAL_STRING);
		sqlTableUtil2.appendOrderBy("COLUMN_NAME");
		List<Map<String, Object>> columnInfo = dcmsDAO.query(sqlTableUtil2.appendSqlWhere(sqlWhereEntity2));
		if(columnInfo.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查看要原表是否为空表
	 * @param tableName
	 * @return
	 */
	public static boolean isEmptyTable(String tableName){
		SqlTableUtil stu = new SqlTableUtil(tableName, "").appendSelFiled("*");
		List<Map<String,Object>> result =  dcmsDAO.query(stu);
		if(result.size() > 0){
			return false;
		} else{
			return true;
		}
	}
	
	/**
	 * 判断表中是否已有主键
	 * @param tableName
	 * @return
	 */
	public static boolean hasExistPrimaryKey(String tableName){
		SqlTableUtil stu4 = new SqlTableUtil("information_schema.KEY_COLUMN_USAGE", "");
		SqlWhereEntity swe3 = new SqlWhereEntity();
		swe3.putWhere("CONSTRAINT_SCHEMA", DBInfoUtil.schemaName, WhereEnum.EQUAL_STRING).putWhere("TABLE_NAME", tableName, WhereEnum.EQUAL_STRING).putWhere("CONSTRAINT_NAME", "PRIMARY", WhereEnum.EQUAL_STRING);
		stu4.appendSelFiled("*").appendSqlWhere(swe3);
		 List<Map<String,Object>> existPrimaryKey = dcmsDAO.query(stu4);
		if(existPrimaryKey.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
	
}
