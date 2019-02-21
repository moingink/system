package com.yonyou.util.sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Blob;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.business.MetaDataUtil;
import com.yonyou.util.BussnissException;
/**
 * 字符串转对象工具类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class StringToObjUtil {
	
	/**
	 * 标示
	 */
	public static String CHAR ="CHAR";
	public static String STRING ="VARCHAR2";
	public static String LONG="LONG";
	public static String NUMBER ="NUMBER";
	
	public static String DATE ="DATE";
	public static String TIMESTAMP="TIMESTAMP";
	public static String BLOB="BLOB";		
	public static String CLOB="CLOB";
			
	public static String ERROR_NUMBER ="001111";
	
	/** 日期格式信息 */
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	/** 时间格式信息 */
	private static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
	
	
	
	/**
	 * 根据元数据类型 转换成业务对象
	 * @param dataMap 数据信息
	 * @param metaDataMap 元数据信息
	 * @return 转换后数据信息
	 * @throws BussnissException
	 */
	public static Map<String,Object> stringToObjForMap(Map<String,String> dataMap ,Map<String,String> metaDataMap) throws BussnissException{
		Map<String,Object> entityMap =new HashMap<String,Object>();
		
		if(dataMap!=null&&metaDataMap!=null&&metaDataMap.size()>0&&dataMap.size()>0){
			for(String key:dataMap.keySet()){
				String type =STRING;
				if(metaDataMap.containsKey(key)){
					String val =dataMap.get(key);
					type=metaDataMap.get(key);
					entityMap.put(key, stringToObj(val,type));
				}
				
			}
		}
		
		return entityMap;
	}
	
	/**
	 * 根据元数据类型 转换成业务对象 方案2
	 * @param dataMap 数据信息
	 * @param metaDataMap 元数据信息
	 * @return 转换后数据信息
	 * @throws BussnissException
	 */
	public static Map<String,Object> stringToObjForMap2(Map<String,String> dataMap ,ConcurrentHashMap<String,ConcurrentHashMap<String,String>> metaDataMap) throws BussnissException{
		Map<String,Object> entityMap =new HashMap<String,Object>();
		
		if(dataMap!=null&&metaDataMap!=null&&metaDataMap.size()>1&&dataMap.size()>1){
			
			for(String key:metaDataMap.keySet()){
				
				if(dataMap.containsKey(key)){
					String val =dataMap.get(key);
					entityMap.put(key, stringToObj(val,metaDataMap.get(key).get(MetaDataUtil.FIELD_TYPE)));
				}
				
			}
			
		}
		
		
		return entityMap;
	}

	/**
	 * 根据元数据类型 转换成业务对象
	 * @param dataMap 数据信息
	 * @param metaDataMap 元数据信息
	 * @return 转换后数据信息
	 * @throws BussnissException
	 */
	public static List<Map<String,Object>> stringToObjForListMap(List<Map<String,String>> dataListMap,Map<String,String> metaDataMap) throws BussnissException {
		List<Map<String,Object>> entityList =new ArrayList<Map<String,Object>>();
		
		if(dataListMap!=null&&metaDataMap!=null&&dataListMap.size()>=1&&metaDataMap.size()>=1){
			for(Map<String,String> dataMap:dataListMap){
				Map<String,Object> map =stringToObjForMap(dataMap,metaDataMap);
				entityList.add(map);
			}
		}
		
		return entityList;
	}
	
	/**
	 * 根据元数据类型 转换成业务对象
	 * @param dataListMap 数据集合
	 * @param metaDataMap 元数据信息
	 * @return 转换后数据集合
	 * @throws BussnissException
	 */
	public static List<Map<String,Object>> stringToObjForListMap2(List<Map<String,String>> dataListMap,ConcurrentHashMap<String,ConcurrentHashMap<String,String>> metaDataMap) throws BussnissException {
		List<Map<String,Object>> entityList =new ArrayList<Map<String,Object>>();
		
		if(dataListMap!=null&&metaDataMap!=null&&dataListMap.size()>1&&metaDataMap.size()>1){
			for(Map<String,String> dataMap:dataListMap){
				Map<String,Object> map =stringToObjForMap2(dataMap,metaDataMap);
				entityList.add(map);
			}
		}
		
		return entityList;
	}
	
	/**
	 * 字符串转对象
	 * @param val 值
	 * @param type 类型
	 * @return 转换后对象信息
	 * @throws BussnissException
	 * @throws ParseException 
	 */
	public static Object stringToObj(String val,String type) throws BussnissException{
		type=MetaCodeMappingType.mappingType(type);
		Object obj = null ;
		
		if(SQLUtil.isEmpty(val)){
			return initValForEmpty(type);
		}
		
		try{
			if(type.equals(DATE)){
				val=val.replace("/", "-");
				if(val.length()>10){
					obj =Timestamp.valueOf(val);
				}else{
					obj =Date.valueOf(val);
				}
				
			}else if(type.equals(TIMESTAMP)){
				String hh24=HHmmss.format(new java.util.Date());
				int val_lenght =val.length();
				if(val_lenght>=19){
					obj =Timestamp.valueOf(val);
				}else if(val_lenght>10&&val_lenght<19){
					obj =Timestamp.valueOf(val.substring(0, 10)+" "+hh24);
				}else if(val_lenght==10){
					obj =Timestamp.valueOf(val+" "+hh24);
				}else{
					
				}
				
			}else if (type.equals(LONG)){
				val=val.replace(",", "");
				obj =new Long(val);
			}else if(type.equals(NUMBER)){
				val=val.replace(",", "");
				obj =new BigDecimal(val);
			}else if(type.equals(CHAR)){
				obj =val;
//			}else if(type.equals(BLOB)){
//				obj =new Blob(val);
//			}else if(type.equals(CLOB)){
//				obj =new BigDecimal(val);
			}else{
				obj =new String(val);
			}
		}catch(Exception e){
			String errorMessage =ERROR_NUMBER +":字符串转换对象出错！字符串【"+val+"】转换类型【"+type+"】";
			throw new BussnissException(errorMessage);
		}
		
		
		return obj;
	}
	
	/**
	 * 初始化空值对象信息
	 * @param type 类型
	 * @return 初始化后信息
	 */
	private static Object initValForEmpty(String type){
		Object obj =null;
		
		java.util.Date  newDate =new java.util.Date();
		if(type.equals(DATE)){
			//obj =new Date(newDate.getTime());
		}else if(type.equals(TIMESTAMP)){
			//obj =new Timestamp(newDate.getTime());
		}else if (type.equals(LONG)){
			obj =new Long("0");
		}else if(type.equals(NUMBER)){
			obj =new BigDecimal("0");
		}else if(type.equals(CHAR)){
			obj ="";
//		}else if(type.equals(BLOB)){
//			obj =new Blob(val);
//		}else if(type.equals(CLOB)){
//			obj =new BigDecimal(val);
		}else{
			obj =new String("");
		}
		
		return obj;
	}
	
	
	public static void main(String [] args) throws BussnissException{  
		String val="2018-01-11";
		String hh24=HHmmss.format(new java.util.Date());
		System.out.println(Timestamp.valueOf(val.substring(0, 10)+" "+hh24));
        System.out.println(Timestamp.valueOf("2017-07-08"+" "+HHmmss.format(new java.util.Date())));  
        
        Map<String,String> dataMap =new HashMap<String,String>();
        
        Map<String,String> metaDataMap =new HashMap<String,String>();
        
        dataMap.put("name", "zhangsan");
        dataMap.put("id", "11");
        dataMap.put("ssss", "1122");
        
        metaDataMap.put("name", STRING);
        metaDataMap.put("date", TIMESTAMP);
        
        Map<String,Object> obj =StringToObjUtil.stringToObjForMap(dataMap, metaDataMap);
        for(String o:obj.keySet()){
        	System.out.println(o+"\t"+obj.get(o)+"\t"+obj.get(o).getClass());
        }
} 
}
