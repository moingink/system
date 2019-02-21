package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.button.cache.ButForUpdateWithCache;
import com.yonyou.util.BussnissException;
import com.yonyou.util.DBInfoUtil;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

/**
 * 元数据字段物理修改
* @ClassName ButForMetadataPhyUpd 
* @author hubc
* @date 2018年5月7日
 */
public class ButForMetadataPhyUpd extends ButForUpdateWithCache {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		
		String returnMessage = "";
		try {
			// 获得初始列信息
			SqlTableUtil sqlTableUtil = new SqlTableUtil(tabName, "").appendSelFiled("*");
			SqlWhereEntity sqlWhereEntity = new SqlWhereEntity();
			sqlWhereEntity.putWhere("ID", json.getString("ID"), WhereEnum.EQUAL_STRING).putWhere("DR", "0", WhereEnum.EQUAL_STRING);
			Map<String, Object> origFieldInfo = dcmsDAO.find(sqlTableUtil.appendSqlWhere(sqlWhereEntity));
			
			//相关表表名
			String tableName = MetaDataUtil.getTableNameFromId(json.getString("METADATA_ID")).toUpperCase();
			String origCloumnName = (String) origFieldInfo.get(MetaDataUtil.FIELD_CODE);
			
			if(!DBInfoUtil.hasExistTable(tableName) || !DBInfoUtil.hasExistCloumn(tableName, origCloumnName)){
				returnMessage = "物理修改失败，物理表中不存在此表或此字段，请先删除字段后尝试物理新增";
				this.ajaxWrite( "{\"message\":\""+returnMessage+"\"}", request, response);
				return null;
			}else if(DBInfoUtil.isEmptyTable(tableName)){
				returnMessage = "物理修改失败，表中已存在数据记录,不允许进行物理修改";
				this.ajaxWrite( "{\"message\":\""+returnMessage+"\"}", request, response);
				return null;
			}else{
				//直接先删除原字段，再新增
				String dropColumnSql = "ALTER TABLE " + tableName + " DROP COLUMN " + origCloumnName;
				dcmsDAO.updateBySql(dropColumnSql);
				
				String columnName = json.getString("FIELD_CODE").toUpperCase();//待新增字段名
				String length = json.getString("FIELD_LENGTH");//字段长度
				String nullFlag = json.getString("NULL_FLAG");//空值标识——0：能为空；1：不能为空
				String keyFlag = json.getString("KEY_FLAG");//主键标志
				StringBuffer addColumnSql = new StringBuffer("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + json.get("FIELD_TYPE"));
				if(length != null && !length.equals("")){
					addColumnSql.append("(" + length + ")");
				}
				if(keyFlag.equals("1")){
					addColumnSql.append(" not null primary key");
				}else if(nullFlag.equals("1")){
					addColumnSql.append(" not null");
				}
				addColumnSql.append(" comment" + "'" + json.get("FIELD_NAME") + "'");
				dcmsDAO.updateBySql(addColumnSql.toString());
			}
			
			//修改元数据明细表
			dcmsDAO.updateByTransfrom(tabName, json, new SqlWhereEntity());
			returnMessage = "{\"message\":\"修改成功\"}";
		} catch (BussnissException e) {
			e.printStackTrace();
			returnMessage = "{\"message\":\"保存失败，请联系管理员\"}";
		}

		this.ajaxWrite(returnMessage, request, response);
		return null;
	}

}
