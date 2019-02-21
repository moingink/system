package com.yonyou.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Types;

/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * 数据类型转换类
 */
public class TransMetaTypeUtil {

	/**
	 * 根据SQL查询数据字典
	 * @param sqlStr  查询SQL
	 * @return     返回数字字典的数据集
	 */
	public static void transFormat(String metaData,HashMap fields) { 
		
//					switch (rs.getMetaData().getColumnType(i)) {
//					case Types.ARRAY:
//						temp.put(rs.getMetaData().getColumnName(i), new String(rs.getBytes(i)));
//						break;
//					case Types.BIGINT:
//						temp.put(rs.getMetaData().getColumnName(i), String.valueOf(rs.getLong(i)));
//						break;
//					case Types.BOOLEAN:
//						temp.put(rs.getMetaData().getColumnName(i), String.valueOf(rs.getBoolean(i)));
//						break;
//					case Types.DATE:
//						temp.put(rs.getMetaData().getColumnName(i), DateUtil.getDate(rs.getDate(i)));
//						break;
//					case Types.DECIMAL:
//						temp.put(rs.getMetaData().getColumnName(i),rs.getBigDecimal(i).toString());
//						break;
//					case Types.DOUBLE:
//						temp.put(rs.getMetaData().getColumnName(i),Double.valueOf(rs.getDouble(i)).toString());
//						break;
//					case Types.FLOAT:
//						temp.put(rs.getMetaData().getColumnName(i),Float.valueOf(rs.getFloat(i)).toString());
//						break;
//					case Types.INTEGER:
//						temp.put(rs.getMetaData().getColumnName(i),Integer.valueOf(rs.getInt(i)).toString());
//						break;
//					case Types.VARCHAR:
//						temp.put(rs.getMetaData().getColumnName(i),rs.getString(i));
//						break;
//					default:
//						break;
//					}
					
			
	}
}
