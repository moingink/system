package com.yonyou.business.orangefinancial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

@Service
public class MatchService {
	@Autowired
	private IBaseDao baseDao;

	public String initH(String defid) {

		try {
			List<String> tableList = new ArrayList<>();
//			获取 对照映射 表信息
			SqlTableUtil sqlEntity;
			sqlEntity = DataSourceUtil.dataSourceToSQL("BASE_MAINDATA_DEF");
			Map<String, String> hparam = new HashMap<>();
			hparam.put("ID=", defid);
			sqlEntity.appendWhereMap(hparam);
			List<Map<String, Object>> mmap = baseDao.query(sqlEntity);
//			对照表名
			String source = (String) mmap.get(0).get("SOURCE");
			System.out.println(mmap);
//			获取被对照表信息
			Map<String, String> bparam = new HashMap<>();
			bparam.put("PID=", defid);
			SqlTableUtil sqlEntityB = DataSourceUtil.dataSourceToSQL("BS_MD_DEF_B");
			sqlEntityB.appendWhereMap(bparam);
			List<Map<String, Object>> bmap = baseDao.query(sqlEntityB);
			for (Map<String, Object> b : bmap) {
				tableList.add((String) b.get("DEST"));
			}

		} catch (BussnissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
