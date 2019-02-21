package com.yonyou.util.sql;

import java.util.List;
import java.util.Map;
/**
 * 修改拼装语句类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class BulidUpdateSql extends BulidInsOrUp {

	@Override
	protected void findHead(StringBuffer sql, String table) {
		// TODO 自动生成的方法存根
		sql.append("UPDATE ").append(table).append(" SET ");
	}

	@Override
	protected void findMain(StringBuffer sql, List<String> rowList,
			String filed, Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		if(!filed.toUpperCase().equals(SQLUtil.ID.toUpperCase())){
			
			if(!filed.toUpperCase().equals(SQLUtil.VERSION.toUpperCase())){
				sql.append(filed).append(" = ").append("?").append(",");
				rowList.add(filed);
			}else{
				sql.append(filed).append(" = ").append(" ").append(filed).append("+1 ").append(",");
			}
			
		}
		
	}

	@Override
	protected void findEnd(StringBuffer sql, List<String> rowList,Map<String,String> whereMap) {
		// TODO 自动生成的方法存根
		sql=this.deleteLengthEndOne(sql);
		
		if(whereMap!=null&&whereMap.size()>0){
			sql.append(" WHERE ");
			for(String filed:whereMap.keySet()){
				if(whereMap.get(filed).indexOf("?")!=-1){
					rowList.add(filed);
				}
				sql.append(" ").append(whereMap.get(filed)).append(" and");
			}
		}else{
			sql.append(" WHERE ");
			sql.append(" ID = ? ");
			rowList.add("ID");
		}
		if(sql.length()>3){
			String end =sql.substring(sql.length()-3, sql.length());
			if(end.toLowerCase().equals("and")){
				sql=sql.delete(sql.length()-3, sql.length());
			}
		}
	}

}
