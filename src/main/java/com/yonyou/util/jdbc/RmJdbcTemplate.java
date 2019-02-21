package com.yonyou.util.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.yonyou.util.sql.SqlLogUtil;

/**
 * JDBC工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class RmJdbcTemplate extends JdbcTemplate {
	
	/** 数据标示  ORACLE */
	public static String dataBase = IGlobalConstants.DATABASE_PRODUCT_NAME_MYSQL;
	
	
	public int update(final String sql) throws DataAccessException {
		//添加SQLLOG
    	SqlLogUtil.writeSqlLog(sql);
		return super.update(sql);
	}
    
    public int update(String sql, Object[][] args) throws DataAccessException {
    	//添加SQLLOG
    	SqlLogUtil.writeSqlLog(sql,args);
        return super.update(sql, args[0], getSqlTypeFromArgs(args[0]));
    }
    
    public int[] batchUpdate(final String[] sql) throws DataAccessException {
    	for(int i=0; i<sql.length; i++) {
    		sql[i] = (sql[i]);
    	}
    	//添加SQLLOG
    	SqlLogUtil.writeSqlLog(sql);
    	return super.batchUpdate(sql);
    }
    
    public int[] batchUpdate(String sql, final Object[][] aObj) throws DataAccessException {
    	if(aObj.length == 0) {
    		return new int[0];
    	}
    	int[] aCount = super.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] args = aObj[i];
				ArgTypePreparedStatementSetter atpss = new ArgTypePreparedStatementSetter(args, getSqlTypeFromArgs(args));
				atpss.setValues(ps);
			}

			public int getBatchSize() {
				return aObj.length;
			}
		});
    	//添加SQLLOG
    	SqlLogUtil.writeSqlLog(sql,aObj);
    	return aCount;
    }
    
    
    /**
     * @param args
     * @return
     */
    public static int[] getSqlTypeFromArgs(Object[] args) {
        int types[] = new int[args.length];
        for (int i = 0; i < args.length; i++) {
        	if(args[i] == null) {
        		types[i] = Types.VARCHAR; 
        	} else if(args[i] instanceof java.sql.Timestamp) {
                types[i] = Types.TIMESTAMP;
            } else if(args[i] instanceof java.sql.Date) {
                types[i] = Types.DATE;
            } else if(args[i] instanceof java.sql.Time) {
            	types[i] = Types.TIME;
            } else if(args[i] instanceof java.math.BigDecimal) {
                types[i] = Types.DECIMAL;
            } else if(args[i] instanceof Integer) {
                types[i] = Types.INTEGER;
            } else if(args[i] instanceof Long) {
                types[i] = Types.BIGINT;
            } else if(args[i] instanceof Short) {
                types[i] = Types.SMALLINT;
            } else if(args[i] instanceof Float) {
                types[i] = Types.FLOAT;
            } else if(args[i] instanceof Double) {
                types[i] = Types.DOUBLE;
            } else {
                types[i] = Types.VARCHAR;
            }
        }
        return types;
    }

    
    public List<Map<String,Object>> query(String strsql,String filed_class,int startIndex,int size){
    	
    	if(IGlobalConstants.DATABASE_PRODUCT_NAME_ORACLE.equalsIgnoreCase(dataBase)) {
    		return (List<Map<String,Object>>) query(getSqlPage4Oracle(strsql, startIndex, size),filed_class,startIndex);
    	} else if(IGlobalConstants.DATABASE_PRODUCT_NAME_MYSQL.equalsIgnoreCase(dataBase)) {
    		return (List<Map<String,Object>>) query(getSqlPage4Mysql(strsql, startIndex, size),filed_class,startIndex);
    	}  else {
    		return (List<Map<String,Object>>) query(strsql, new RmRowMapperResultset<List<Map<String,Object>>>(filed_class,startIndex));
    	}
    }
    
    public List<Map<String,Object>> query(String strsql,ResultSetExtractor ext){
    	//添加SQLLOG
    	SqlLogUtil.writeSqlLog(strsql);
    	return (List<Map<String,Object>>) super.query(strsql, ext);
    }
    
    public List<Map<String,Object>> query(String strsql,RowMapper rowMapper){
    	//添加SQLLOG
    	SqlLogUtil.writeSqlLog(strsql);
    	return (List<Map<String,Object>>) super.query(strsql, rowMapper);
    }
    
    public List<Map<String,Object>> query(String strsql,String filed_class){
    	return (List<Map<String,Object>>) query(strsql, new RmRowMapperResultset<List<Map<String,Object>>>(filed_class));
    }
    
    public List<Map<String,Object>> query(String strsql,String filed_class,int startIndex){
    	return (List<Map<String,Object>>) query(strsql, new RmRowMapperResultset<List<Map<String,Object>>>(filed_class,startIndex));
    }
    
    
    private static String getSqlPage4Mysql(String strsql, int startIndex, int size) {
    	if(size==-1){
    		return strsql;
    	}
        return strsql + " limit " + (startIndex) + "," + size;
    }
    
    private static String getSqlPage4Oracle(String strsql, int startIndex, int size) {
        return "select * from (select rownum as rmrn, a.* from(" + strsql + ") a where rownum<=" + (startIndex + size ) + ")where rmrn >=" + (startIndex+1);
    }

}
