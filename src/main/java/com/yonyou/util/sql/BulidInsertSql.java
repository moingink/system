package com.yonyou.util.sql;

import java.util.List;
import java.util.Map;
/**
 * 新增拼装语句类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class BulidInsertSql extends BulidInsOrUp {

	@Override
	protected void findHead(StringBuffer sql, String table) {
		// TODO 自动生成的方法存根
		sql.append("INSERT INTO ").append(table).append(" (");
	}

	@Override
	protected void findMain(StringBuffer sql, List<String> rowList,
			String filed, Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		sql.append(filed).append(",");
		rowList.add(filed);
	}

	@Override
	protected void findEnd(StringBuffer sql, List<String> rowList, Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		sql=this.deleteLengthEndOne(sql);
		sql.append(")").append(" VALUES (");
		for(int i=0;i<rowList.size();i++){
			sql.append("?").append(",");
		}
		sql=this.deleteLengthEndOne(sql);
		sql.append(")");
	}

}
