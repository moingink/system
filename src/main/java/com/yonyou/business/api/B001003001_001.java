package com.yonyou.business.api;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;

import net.sf.json.JSONObject;

/**
 * 接口实现类-根据登录名获取用户信息
* @ClassName B001003001_001 
* @author 博超
* @date 2017年3月10日
 */
public class B001003001_001 extends ApiBussAbs{

	@Override
	public JSONObject doDeal(JSONObject jsonData) {
		if (jsonData.containsKey("loginid")) {
			String loginName = jsonData.getString("loginid");
			SqlTableUtil tableEntity = null;
			try {
				tableEntity = DataSourceUtil.dataSourceToSQL("API_USER");
				tableEntity.appendWhereAnd("LOGIN_ID='" + loginName + "'");
				Map<String, Object> user = BaseDao.getBaseDao().find(tableEntity);

				if (user.isEmpty()) {
					retJsonObject = ApiUtil.ApiJsonPut("001004");
				} else {
					retJsonObject = ApiUtil.ApiJsonPut("000000");
					retJsonObject.put("data", user);
				}
			} catch (BussnissException e) {
				e.printStackTrace();
				retJsonObject = ApiUtil.ApiJsonPut("000007");
			}
		} else {
			retJsonObject = ApiUtil.ApiJsonPut("001003");
		}

		return retJsonObject;
	}

}
