package com.yonyou.util.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.util.BussnissException;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.plan.NotifyPlanAbs;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * Service实现类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class BaseService implements IBaseService{
	
	@Autowired
	private IBaseDao baseDao =(BaseDao)SpringContextUtil.getBean("dcmsDAO");
	
	@Override
	public Map<String, Object> find(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		return baseDao.find(tableEntity);
	}

	@Override
	public List<Map<String, Object>> query(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		return baseDao.query(tableEntity);
	}

	@Override
	public List<Map<String, Object>> query(SqlTableUtil tableEntity, int index,
			int size) {
		// TODO 自动生成的方法存根
		return baseDao.query(tableEntity,index,size);
	}

	@Override
	public String insert(String table, Map<String, Object> entity) {
		// TODO 自动生成的方法存根
		return baseDao.insert(table, entity);
	}

	@Override
	public String[] insert(String table, List<Map<String, Object>> entityList) {
		// TODO 自动生成的方法存根
		return baseDao.insert(table, entityList);
	}

	@Override
	public int update(String table, Map<String, Object> entity,
			SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		return baseDao.update(table, entity, whereEntity);
	}

	@Override
	public int update(String table, List<Map<String, Object>> entityList,
			SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		return baseDao.update(table, entityList, whereEntity);
	}

	@Override
	public int queryLength(SqlTableUtil tableEntity) {
		// TODO 自动生成的方法存根
		return baseDao.queryLength(tableEntity);
	}

	@Override
	public int delete(String table,
			SqlWhereEntity whereEntity) {
		// TODO 自动生成的方法存根
		return baseDao.delete(table, whereEntity);
	}

	@Override
	public void executePlan(NotifyPlanAbs planAbs, String planCode,
			Map<String, Object> dataMap) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		planAbs.Notify(planCode, dataMap, baseDao);
	}

	
	
}
