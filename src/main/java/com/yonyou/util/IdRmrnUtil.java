package com.yonyou.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**验证list界面所选中的数据是否满足某个按钮的业务功能的工具类
 * @author XIANGJIAN
 * @date 创建时间：2016年12月26日
 * @version 1.0
 */
public class IdRmrnUtil {
	
	/**主键标识串**/
	private static String IDS = "IDS" ; 	
	/** 主键标识 */
	private static String ID = "ID" ; 		
	/** 序列号 */
	private static String RMRN = "RMRN" ; 
	/** YES常量 */
	private static String YES = "YES" ; 
	/** NO常量 */
	private static String NO = "NO" ; 
	
	/**通过所选中的json对象，返回每个json对象的序号，以及所选中的IDS
	 * @param jsonArray 前端选中的json数组
	 * @return 每个ID对应的序号,以及所有ID，以‘,’分隔;Map{ID1=RMRN1,ID2=RMRN2,ID3=RMRN3...IDS=ID1,ID2,ID3...}
	 */
	public static Map<String,Object> getIdRmrnMap(JSONArray jsonArray){
		Map<String,Object> idRmrnMap = new HashMap<String, Object>(); 
		int len = jsonArray.size();
		String ids = "" ; 
		for (int i = 0 ; i < len ; i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			idRmrnMap.put(jb.getString(ID), jb.getString(RMRN));
			if(i==len-1){
				ids += jb.getString(ID); 
			}else{
				ids += jb.getString(ID) + ","; 
			}
		}
		idRmrnMap.put(IDS , ids);
		return idRmrnMap;
	}
	
	/**获取持久层通过IDS查询的List<Map<String, Object>>集合 
	 * 	如果Map的key中含有形参key，并且key相应的value与形参的value相同，则返回Map{ID=YES,ID=YES....ID=YES};
	 * 	如果Map的key中含有形参key，并且key相应的value与形参的value不同，则返回Map{ID=NO,ID=NO...ID=NO};
	 * @param key	验证的字段
	 * @param value	验证字段的值
	 * @param entityList	获取持久层通过IDS查询返回的集合
	 * @return 如果持久层对象含有key，并且value相同，返回YES；不同返回NO。如：{ID1=YES,ID2=YES,ID3=NO......IDS=YES}
	 */
	public static Map<String,Object> lists(String key , String value ,List<Map<String, Object>> entityList){
		Map<String,Object> daoMap  = new HashMap<String, Object>(); 
		for(Map<String, Object> map : entityList){
			if(map.containsKey(key)){
				if(value.equals(map.get(key))){
					daoMap.put(map.get(ID).toString(), (String)YES);
				}else{
					daoMap.put(map.get(ID).toString(), (String)NO);
				}
			}
		}
		return daoMap;
	}
	
	/**	获取持久层通过IDS查询的List<Map<String, Object>>集合 
	 * 	如果Map的key中含有形参key，并且key相应的value与形参的value[i]相同，则返回Map{ID=YES,ID=YES....ID=YES};
	 * 	如果Map的key中含有形参key，并且key相应的value与形参的value[i]不同，则返回Map{ID=NO,ID=NO...ID=NO};
	  * @param key	验证的字段
	 * @param value	验证字段的值，可为多个值，用数组表示
	 * @param entityList	获取持久层通过IDS查询返回的集合
	 * @return	如果持久层对象含有key，并且value[i]相同，返回YES；不同返回NO。如：{ID1=YES,ID2=YES,ID3=NO......IDS=YES}
	 */
	public static Map<String,Object> lists(String key , String value[] ,List<Map<String, Object>> entityList){
		Map<String,Object> daoMap  = new HashMap<String, Object>(); 
		if(value.length == 1){
			return lists(key,value[0],entityList);
		}
		if(value.length > 1){
			for(Map<String,Object> map : entityList){
				if(map.containsKey(key.trim())){
					for(int i = 0; i < value.length ; i++){
						if(value[i].trim().equals(map.get(key))){
							daoMap.put(map.get(ID).toString(), (String)YES);
							break;
						}else{
							daoMap.put(map.get(ID).toString(), (String)NO);
						}
					}
				}
			}
		}
		return daoMap;
	}
	
	/** 及时验证前端选择的数据是否满足业务需求
	 * @param key	验证字段
	 * @param value	验证字段的值
	 * @param entityList	获取持久层通过IDS查询返回的集合
	 * @param jsonArray		前端选中的json数组
	 * @return 获取不满足条件的序号，以“,”分隔
	 */
	public static String getNoPassRmrn(String key , String value,List<Map<String, Object>> entityList , JSONArray jsonArray){
		int listsLen = entityList.size() ;
		int jsonArrayLen = jsonArray.size() ;
		Map<String, Object> jsonMap = null ;
		Map<String, Object> daoMap = null ; 
		String result = "" ;
		if(listsLen > 0 && jsonArrayLen > 0){
			jsonMap = getIdRmrnMap(jsonArray);
			daoMap = lists(key.trim(),value.trim(),entityList);
			for(String listKey : daoMap.keySet()){
				if(daoMap.get(listKey).toString().equals(YES)&&jsonMap.containsKey(listKey)){
					result += (String) jsonMap.get(listKey)+",";
				}
			}
		}
		return result;
	}
	
	/**Map<String,Object> valueMap  多个字段（String）的值（Object）判断
	 * 只针对多个值的反向操作，比如CHECK_STATUS不能为0，但是DATA_STATUS必须为1的情况
	 * @param valueMap 验证的字段和值的集合
	 * @param entityList 获取持久层通过IDS查询返回的集合
	 * @param jsonArray 前端选中的json数组
	 * @return 获取不满足条件的序号，以“,”分隔
	 */
	public static String getNoPassRmrn(Map<String,Object> valueMap,List<Map<String, Object>> entityList , JSONArray jsonArray){
		int listsLen = entityList.size() ;
		int jsonArrayLen = jsonArray.size() ;
		int valueLen = valueMap.size();
		Map<String, Object> jsonMap = null ;
		Map<String, Object> daoMap1 = null ; 
		Map<String, Object> daoMap2 = null ; 
		String result = "" ;
		if(listsLen > 0 && jsonArrayLen > 0 && valueLen > 0){
			jsonMap = getIdRmrnMap(jsonArray);
			daoMap1 = lists("REC_STATUS",valueMap.get("REC_STATUS").toString(),entityList);
			daoMap2 = lists("ITEM_STATUS",valueMap.get("ITEM_STATUS").toString(),entityList);
			for(String listKey : daoMap1.keySet()){
				if(daoMap1.get(listKey).toString().equals(YES)&&jsonMap.containsKey(listKey)&&daoMap2.get(listKey).toString().equals(YES)){
					return result;
				}else if(daoMap1.get(listKey).toString().equals(YES)&&jsonMap.containsKey(listKey)&&daoMap2.get(listKey).toString().equals(NO)){
					return result += (String) jsonMap.get(listKey)+",";
				}
			}
		}
		return result;
	}
	
	/** 及时验证前端选择的数据是否满足业务需求
	 * @param k	验证字段
	 * @param value	验证字段的值，可以为多个值，用数组表示
	 * @param entityList	获取持久层通过IDS查询返回的集合
	 * @param jsonArray 前端选中的json数组
	 * @return 获取不满足条件的序号，以“,”分隔
	 */
	public static String getNoPassRmrn(String k , String value[] ,List<Map<String, Object>> entityList , JSONArray jsonArray){
		int listsLen = entityList.size() ;
		int jsonArrayLen = jsonArray.size() ;
		Map<String, Object> jsonMap = null ;
		Map<String, Object> daoMap = null ; 
		String result = "" ;
		if(listsLen > 0 && jsonArrayLen > 0){
			jsonMap = getIdRmrnMap(jsonArray);
			daoMap = lists(k.trim(),value,entityList);
			for(String listKey : daoMap.keySet()){
				if(daoMap.get(listKey).toString().equals(YES)&&jsonMap.containsKey(listKey)){
					result += (String) jsonMap.get(listKey)+",";
				}
			}
		}
		return result;
	}
	
	/**回写页面错误信息，带序号
	 * @param result 不满足条件的序号
	 * @param reason 不满足条件原因，可自定义
	 * @return 不满足条件的序号以及原因
	 */
	public static StringBuffer writeErrorInfo(String result , String reason){
		StringBuffer sb = new StringBuffer();
		String[] temp = result.split(",");
		int len = temp.length;
		if(len > 0){
			int[] numTemp = new int[len];
			for(int i = 0 ; i<len ; i++){
				numTemp[i] = Integer.parseInt(temp[i]);
			}
			Arrays.sort(numTemp);
			for(int i = 0 ; i <len ; i++){
				result +="第"+numTemp[i]+"行不满足条件！</br>";
			}
			sb.append(result.substring(result.indexOf("第"))).append("<font color='red' size='4'>").append(reason).append("</font>");
			return sb;
		}
		return new StringBuffer("数据有误");
	}
	
	/**回写页面错误信息，不带序号
	 * @param reason 不满足条件原因，可自定义
	 * @return 不满足条件的原因
	 */
	public static StringBuffer writeErrorInfo(String reason){
		StringBuffer sb = new StringBuffer();
		sb.append("<font color='red' size='4'>").append(reason).append("</font>");
		return sb;
	}
	
}
