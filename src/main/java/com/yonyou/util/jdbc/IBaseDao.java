package com.yonyou.util.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.yonyou.util.BussnissException;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public interface IBaseDao {
	
	/**
	 * 查询数据对象
	 * @param tableEntity 表工具
	 * @return 数据信息
	 */
	public Map<String,Object> find(SqlTableUtil tableEntity);
	
	/**
	 * 查询数据集合
	 * @param tableEntity 表工具
	 * @return 数据集合
	 */
	public List<Map<String,Object>> query(SqlTableUtil tableEntity);
	
	/**
	 * 分页查询数据集合
	 * @param tableEntity 表工具
	 * @param index 索引
	 * @param size 条数
	 * @return 数据集合
	 */
	public List<Map<String,Object>> query(SqlTableUtil tableEntity,int index,int size);
	
	/**
	 * 新增数据
	 * @param table 表名
	 * @param entity 数据信息
	 * @return 影响数据
	 */
	public String insert(String table,Map<String,Object> entity);
	
	/**
	 * 新增数据 并支持根据数据元进行类型转换
	 * @param table 表名
	 * @param entity 数据信息
	 * @return
	 * @throws BussnissException
	 */
	public String insertByTransfrom(String table,Map<String,String> entity)  throws BussnissException;
	
	/**
	 * 批量新增数据
	 * @param table 表名
	 * @param entityList 数据集合
	 * @return 影响条数信息
	 */
	public String[] insert(String table,List<Map<String,Object>> entityList);
	
	/**
	 * 批量新增数据 并支持根据数据元进行类型转换
	 * @param table 表名
	 * @param entityList 数据集合
	 * @return 影响条数
	 * @throws BussnissException
	 */
	public String[] insertByTransfrom(String table,List<Map<String,String>> entityList)  throws BussnissException;
	
	/**
	 * 修改数据
	 * @param table 表名
	 * @param entity 数据信息
	 * @param whereEntity where条件工具类
	 * @return 影响条数
	 */
	public int update(String table,Map<String,Object> entity,SqlWhereEntity whereEntity);
	
	/**
	 * 修改数据 并支持根据数据元进行类型转换
	 * @param table 表名
	 * @param entity 数据信息
	 * @param whereEntity where条件工具类
	 * @return 影响条数
	 * @throws BussnissException
	 */
	public int updateByTransfrom(String table,Map<String,String> entity,SqlWhereEntity whereEntity)  throws BussnissException;
	
	/**
	 * 批量修改数据 
	 * @param table 表名
	 * @param entityList 数据信息
	 * @param whereEntity where条件工具类
	 * @return 影响条数
	 */
	public int update(String table,List<Map<String,Object>> entityList,SqlWhereEntity whereEntity);
	
	/**
	 * 根据SQL进行数据修改 
	 * @param sql SQL语句
	 * @return 影响条数
	 */
	public int updateBySql(String sql);
	
	/**
	 * 批量修改数据 并支持根据数据元进行数据类型转换
	 * @param table 表名
	 * @param entityList 数据集合
	 * @param whereEntity where条件工具累
	 * @return 影响条数
	 * @throws BussnissException
	 */
	public int updateByTransfrom(String table,List<Map<String,String>> entityList,SqlWhereEntity whereEntity)  throws BussnissException;
	
	/**
	 * 获取总条数
	 * @param tableEntity 表工具类
	 * @return 总条数
	 */
	public int queryLength(SqlTableUtil tableEntity);
	
	/**
	 * 删除数据 逻辑删除
	 * @param table 表名
	 * @param whereEntity where条件工具类
	 * @return 影响条数
	 */
	public int delete(String table,SqlWhereEntity whereEntity);
	
	/**
	 * 删除数据  物理删除
	 * @param table 表名
	 * @param whereEntity where条件工具类
	 * @return 影响条数
	 * @throws BussnissException
	 */
	public int deleteByTransfrom(String table,SqlWhereEntity whereEntity)  throws BussnissException;
	
	/**
	 * 根据过滤类 进行数据查询
	 * @param strsql SQL查询语句
	 * @param filed_class 过滤类
	 * @return 数据集合
	 */
	public List<Map<String, Object>> query(String strsql, String filed_class);
	
}
