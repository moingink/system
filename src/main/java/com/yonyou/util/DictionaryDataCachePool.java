package com.yonyou.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

//import platform.redis.service.PlatformCatchManager;
//import platform.sys.model.SysCodeData;
//import platform.sys.model.SysCodeType;
//import platform.sys.service.SysCodeDataManager;
//import platform.sys.service.SysCodeTypeManager;

/**
 * 数据字典获取工具类
 *
 */
public class DictionaryDataCachePool {

	
	private static String SQL_QUERY_BYKEYWORD="select codetype.id typeid,codetype.type_keyword,"
			+ " codetype.name,codetype.remark,codedata.data_key,codedata.enable_status,"
			+ " codedata.data_value,codedata.total_code,codedata.remark dataremark"
			+ " from sys_code_data codedata, sys_code_type codetype"
			+ " where codetype.id = codedata.code_type_id and enable_status='1' and type_keyword=";

    /**
     * 根据数据字典类型获取所有数字字典的数据
     * 
     * @param keyword  数据字典类型
     * @return  List   返回结果
     */
//	public static List<HashMap> getDictory(String keyword){
//		if(null==keyword || "".equals(keyword)){
//			return new ArrayList<HashMap>();
//		}
//		return TimesTenUtil.executeSQL(SQL_QUERY_BYKEYWORD+" '"+keyword+"'");
//    }
    
	/**
     * 根据数据字典类型获取所有数字字典的数据
     * 
     * @param keyword 数据字典类型
     * @param code    数据字典数字编码
     * @return  List  返回结果
     */
	
//    public static List<HashMap>  getDictory(String keyword,String code){
//    	if(null==keyword || "".equals(keyword)){
//			return new ArrayList<HashMap>();
//		}
//		return TimesTenUtil.executeSQL(SQL_QUERY_BYKEYWORD+" '"+keyword+"' and data_key='"+code+"'");
//    }
//
//    public static void main(String[] arg){
//    	
//    	List<HashMap>  list = DictionaryDataCachePool.getDictory("fileuploadPath","fileuploadPath");
//    	System.out.println(list.size());
//		for (int i = 0; i < list.size(); i++) {
//			HashMap map1 = list.get(i);
//			System.out.println("===="+map1.toString());
//
//		}
//    }
}
