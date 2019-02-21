package com.yonyou.util.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlLogUtil {
	
	private static InsUpSqlEntity insUpEntity =new InsUpSqlEntity();
	
	private final static Logger LOG = LoggerFactory.getLogger(SqlLogUtil.class);
	
	
	public static void writeSqlLog(String sql,Object[][] objs){
		if(objs!=null){
			insUpEntity.init(sql, objs);
			writeSqlLog(insUpEntity);
		}else{
			writeSqlLog(sql);
		}
		
		
	}
	
	public static void writeSqlLog(InsUpSqlEntity insUpSqlEntity){
		writeSqlLog(insUpSqlEntity.findSqls());
	}
	
	public static void writeSqlLog(String [] sqls){
		for(String sql:sqls){
			writeSqlLog(sql);
		}
	}
	
	public static void writeSqlLog(String sql){
		LOG.info("SQL:LOG:\t"+sql);
	}
	
	
}
