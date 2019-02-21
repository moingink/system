package com.yonyou.business.button.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.button.cache.ButForInsertWithCache;
import com.yonyou.util.BussnissException;
import com.yonyou.util.DBInfoUtil;
import com.yonyou.util.jdbc.IBaseDao;

/**
 * 元数据字段物理新增
* @ClassName ButForMetadataPhyIns 
* @author hubc
* @date 2018年5月7日
 */
public class ButForMetadataPhyIns extends ButForInsertWithCache{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		
		String dataSourceCode = request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String columnName = json.getString("FIELD_CODE").toUpperCase();//待新增字段名
		String length = json.getString("FIELD_LENGTH");//字段长度
		String nullFlag = json.getString("NULL_FLAG");//空值标识——0：能为空；1：不能为空
		String keyFlag = json.getString("KEY_FLAG");//主键标志
		String fieldType = json.getString(MetaDataUtil.FIELD_TYPE);//字段类型
		//待新增字段语句-id varchar(19) not null primary key comment '主键'
		StringBuffer tempColumnSql = new StringBuffer(columnName + " " + fieldType);
		if(null != length && !"".equals(length.trim())){
			tempColumnSql.append("(" + length + ")");
		}
		if(keyFlag.equals("1")){
			tempColumnSql.append(" not null primary key");
		}else if(nullFlag.equals("1")){
			tempColumnSql.append(" not null");
		}else if("TIMESTAMP".equals(fieldType)){
			tempColumnSql.append(" null");//mysql中TIMESTAMP类型默认为非空
		}
		tempColumnSql.append(" comment" + "'" + json.get(MetaDataUtil.FIELD_NAME) + "'");
		
		//相关表表名
		String tableName = MetaDataUtil.getTableNameFromId(json.getString(MetaDataUtil.FIELD_METADATA_ID)).toUpperCase();
		String returnMessage = "";
		try {
			//如果当前系统中没有这张表，需要建立
			if(DBInfoUtil.hasExistTable(tableName)){
				//已存在物理表时，还需判断字段是否已存在
				if(DBInfoUtil.hasExistCloumn(tableName, columnName)){
					returnMessage = "物理新增失败，物理表中已存在该字段，无需物理新增";
					this.ajaxWrite( "{\"message\":\""+returnMessage+"\"}", request, response);
					return null;
				}else{
					if(keyFlag.equals("1") && DBInfoUtil.hasExistPrimaryKey(tableName)){
						returnMessage = "物理新增失败，物理表中已存在主键字段";
						this.ajaxWrite( "{\"message\":\""+returnMessage+"\"}", request, response);
						return null;
					}
					if((keyFlag.equals("1") || nullFlag.equals("1")) && !DBInfoUtil.isEmptyTable(tableName)){
						returnMessage = "物理新增失败，表中已存在数据记录，无法新增非空字段(包括主键字段)";
						this.ajaxWrite( "{\"message\":\""+returnMessage+"\"}", request, response);
						return null;
					}
					String addColumnSql = "ALTER TABLE " + tableName + " ADD COLUMN " + tempColumnSql;
					dcmsDAO.updateBySql(addColumnSql);
				}
			}else {
				String createTabSql="CREATE TABLE " + tableName + "(" + tempColumnSql + ")";
				dcmsDAO.updateBySql(createTabSql);
			}
			//插入元数据明细表
			dcmsDAO.insertByTransfrom(tabName, json);
			returnMessage = "{\"message\":\"保存成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			returnMessage = "{\"message\":\"保存失败，请联系管理员\"}";
		}

		this.ajaxWrite(returnMessage, request, response);
		return null;
	}

}
