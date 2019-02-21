package com.yonyou.util.sql;

import com.yonyou.util.jdbc.IGlobalConstants;
import com.yonyou.util.jdbc.RmJdbcTemplate;

public class SqlStringupUpperToLowerTrans {
	
	public static String transByRmJdbc(String sql){
		if(sql!=null){
			if(RmJdbcTemplate.dataBase.equals(IGlobalConstants.DATABASE_PRODUCT_NAME_MYSQL)){
				return sql.toLowerCase();
			}
		}
		return sql;
	}
}
