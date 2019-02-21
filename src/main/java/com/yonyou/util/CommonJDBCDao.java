package com.yonyou.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.yonyou.iuap.persistence.bs.dao.DAOException;
import com.yonyou.iuap.persistence.jdbc.framework.SQLParameter;

/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * 通用数据库访问类
 * 通过该类能查询访问不同表的数据
 */

@Repository
public class CommonJDBCDao {

	@Autowired
	private JdbcTemplate dao;

	public int save(String metaDataCode,HashMap filedsmap) throws DAOException {
		//SQLUtil(String table_name,HashMap fields_values,String FLAG)
		return 1;
	}

	public int update(String metaDataCode,HashMap filedsMap,HashMap searchParams) throws DAOException {
		//SQLUtil(String table_name,HashMap fields_values,String FLAG)
		return 0;
	}

	public int delete(String metaDataCode,HashMap searchParams) throws DAOException {
		//SQLUtil(String table_name,HashMap fields_values,String FLAG)
		return 0;
	}
	
	public List<HashMap> queryPage(String metaDataCode,Map<String, Object> searchParams, PageRequest pageRequest) throws DAOException {
		//SQLUtil(String table_name,HashMap fields_values,String FLAG)
		StringBuffer sqlBuffer = new StringBuffer("select * from good_demo where 1=1 ");
		SQLParameter sqlParameter = new SQLParameter();
		buildSql(searchParams, sqlBuffer, sqlParameter);
		String sql = sqlBuffer.toString();
		//return dao.queryPage(sql, sqlParameter, pageRequest, GoodJdbcDemo.class);
		return null;
	}

	
	//业务开发根据自己的需求，修改查询条件的拼接方式
	private void buildSql(Map<String, Object> searchParams, StringBuffer sqlBuffer, SQLParameter sqlParameter) {
		
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
			String[] keySplit = entry.getKey().split("_");
			if (keySplit.length == 2) {
				String columnName = keySplit[1];
				String compartor = keySplit[0];
				Object value = entry.getValue();
				if (value != null && StringUtils.isNotBlank(value.toString())) {
					
					sb.append(columnName).append(" ").append(compartor).append(" ? ");
					// 处理模糊查询
					value = "like".equalsIgnoreCase(compartor) ? "%" + value + "%" : value;
					sqlParameter.addParam(value);
					index ++;
					
					if(index != searchParams.keySet().size()){
						sb.append(" or ");
					}
				}
			}
		}
		
		String conditionSql = sb.toString();
		if(StringUtils.isNoneBlank(conditionSql)){
			sqlBuffer.append(" and (" + conditionSql.toString() + ");");
		}
		
	}
}
