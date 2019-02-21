package com.yonyou.util.theme.path.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.theme.path.ThemePathAbs;
import com.yonyou.util.theme.vo.ThemePathEntity;

public class ThemePathForData extends ThemePathAbs {

	ThemePathForData() {
		super();
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected void init() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public Map<String, ThemePathEntity> queryData(String themeCode) {
		Map<String, ThemePathEntity> returnMap =new HashMap<String,ThemePathEntity>();
		// TODO 自动生成的方法存根
		for(Map<String,Object> data:this.queryDataByThemeCode(themeCode)){
			ThemePathEntity entity =new ThemePathEntity(data);
			String key =this.getKey(entity.getPosition_code(), entity.getSystem_code());
			returnMap.put(key, entity);
		}
		return returnMap;
	}
	
	@Override
	public ThemePathEntity findData(String themeCode, String position_code,
			String system_code) {
		// TODO 自动生成的方法存根
		try {
			SqlTableUtil sqlEntity=this.bulidSqlTable(themeCode);
			sqlEntity.appendWhereAnd(" position_code='"+position_code+"' ");
			Map<String,Object> data =BaseDao.getBaseDao().find(sqlEntity);
			if(data!=null&&data.size()>0){
				return new ThemePathEntity(data);
			}else{
				return null;
			}
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
		
	}
	
	protected List<Map<String,Object>> queryDataByThemeCode(String themeCode){
		List<Map<String,Object>> listData=new ArrayList<Map<String,Object>>();
		try {
			listData=BaseDao.getBaseDao().query(this.bulidSqlTable(themeCode));
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return listData;
	}
	
	protected SqlTableUtil bulidSqlTable(String themeCode) throws BussnissException{
		SqlTableUtil sqlEntity = DataSourceUtil.dataSourceToSQL("PAGE_DEFAULT_PATH");
		return sqlEntity;
	}
	
}
