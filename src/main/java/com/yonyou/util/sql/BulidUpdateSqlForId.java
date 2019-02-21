package com.yonyou.util.sql;

import java.util.List;
import java.util.Map;
/**
 * 修改拼装语句类  
 * · 用于ID
 * @author moing_ink
 *
 */
public class BulidUpdateSqlForId extends BulidInsOrUp {

	@Override
	protected void findHead(StringBuffer sql, String table) {
		// TODO 自动生成的方法存根
		sql.append("UPDATE ").append(table).append(" SET ");
	}

	@Override
	protected void findMain(StringBuffer sql, List<String> rowList,
			String filed, Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		if(!filed.toLowerCase().equals("id".toLowerCase())){
			sql.append(filed).append(" = ").append("?").append(",");
			rowList.add(filed);
		}
	}

	@Override
	protected void findEnd(StringBuffer sql, List<String> rowList, Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		sql=this.deleteLengthEndOne(sql);
		sql.append(" WHERE ").append("id").append(" = ").append("?");
		rowList.add("id");
		
	}

}
