package com.yonyou.util.sql;

import java.util.List;
import java.util.Map;

/**
 * 删除语句拼装类
 * @author moing_ink
 *
 */
public class BulidDeleteSql extends BulidUpdateSql{

	@Override
	protected void findHead(StringBuffer sql, String table) {
		// TODO 自动生成的方法存根
		sql.append("DELETE FROM ").append(table).append(" ");
	}

	@Override
	protected void findMain(StringBuffer sql, List<String> rowList,
			String filed, Map<String, String> whereMap) {
		// TODO 自动生成的方法存根
	}

}
