package com.yonyou.util.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.util.BussnissException;
import com.yonyou.util.notity.plan.NotifyPlanAbs;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public interface IBaseService {
	
	/**
	 * 查询数据
	 * @param tableEntity 表工具类
	 * @return 数据信息
	 */
	public Map<String,Object> find(SqlTableUtil tableEntity);
	/**
	 * 查询数据集合
	 * @param tableEntity 表工具类
	 * @return 数据集合
	 */
	public List<Map<String,Object>> query(SqlTableUtil tableEntity);
	
	/**
	 * 分页查询
	 * @param tableEntity 表工具类
	 * @param index 索引
	 * @param size 条数
	 * @return 数据集合
	 */
	public List<Map<String,Object>> query(SqlTableUtil tableEntity,int index,int size);
	
	/**
	 * 新增
	 * @param table 表名
	 * @param entity 数据信息
	 * @return
	 */
	public String insert(String table,Map<String,Object> entity);
	
	/**
	 * 批量信息
	 * @param table 表名
	 * @param entityList 数据集合
	 * @return
	 */
	public String[] insert(String table,List<Map<String,Object>> entityList);
	
	/**
	 * 修改
	 * @param table 表名
	 * @param entity 数据信息
	 * @param whereEntity 查询条件工具类
	 * @return 影响条数
	 */
	public int update(String table,Map<String,Object> entity,SqlWhereEntity whereEntity);
	
	/**
	 * 批量修改 
	 * @param table 表名
	 * @param entityList  数据集合
	 * @param whereEntity 查询条件工具类
	 * @return 影响条数
	 */
	public int update(String table,List<Map<String,Object>> entityList,SqlWhereEntity whereEntity);
	
	/**
	 * 查询总数
	 * @param tableEntity 表工具列
	 * @return 总条数
	 */
	public int queryLength(SqlTableUtil tableEntity);
	
	public int delete(String table,SqlWhereEntity whereEntity);
	
	
	public void executePlan(NotifyPlanAbs planAbs,String planCode,Map<String,Object> dataMap) throws IOException, BussnissException;
	
}
