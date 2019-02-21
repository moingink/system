package com.yonyou.util.validator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yonyou.util.validator.entity.MatcherEntity;

public class BulidValidUtil {
	
	private static String MARK ="\\{\\w*\\}";
	private static String REPALCE_MARK="\\{|\\}";
	
	private static String PAGE_FUN_MARK ="\\(\\w*\\)";
	private static String PAGE_FUN_REPALCE_MARK="\\(|\\)";
	private static String PAGE_FUN_SPLIT=";";
	private static String PAGE_MESSAGE_SPLIT=",";
	//是否验证大小写
	private static boolean isCase =false;
	
	/**
	 * 获取替换后数据
	 * @param formar
	 * @param values
	 * @return
	 */
	public static String replaceMarkString(String format,Map<String,String> values){
		if(format!=null&&format.length()>0){
		    List<MatcherEntity> list =analysisMark(format, values, MARK);
			return bulidReplaceMessage(list, format, values);
		}
		return "";
	}
	
	public static List<MatcherEntity> analysisMark(String format,Map<String,String> values,String mark){
		List<MatcherEntity> list =new ArrayList<MatcherEntity>();
		if(format!=null&&format.length()>0){
			Pattern r = Pattern.compile(mark);
		    Matcher m = r.matcher(format);
		      while(m.find())
	          {  
		    	 MatcherEntity entity =new MatcherEntity(m);
		    	 list.add(entity);
	          }
		}
		return list;
	}
	
	/**
	 * 构建替换数据
	 * @param list
	 * @param formar
	 * @param values
	 * @return
	 */
	private static String bulidReplaceMessage(List<MatcherEntity> list,String formar,Map<String,String> values){
		StringBuilder repalce =new StringBuilder(formar);
		StringBuilder returnString =new StringBuilder();
		int start=0;
		int length =repalce.length();
		for(MatcherEntity entity:list){
			int end =entity.getStart();
			returnString.append(repalce.substring(start, end));
			returnString.append(findValue(findKey(entity.getMark()),values));
			start =entity.getEnd();
		}
		if(start<length){
			returnString.append(repalce.substring(start, length));
		}
		return returnString.toString();
	}
	/**
	 * 获取数据KEY
	 * @param mark
	 * @return
	 */
	private static String findKey(String mark){
		return replaceMark(mark,REPALCE_MARK);
	}
	
	/**
	 * 去除标示
	 * @param message
	 * @param replaceMark
	 * @return
	 */
	private static String replaceMark(String message,String replaceMark){
		if(message!=null&&message.length()>0){
			return message.replaceAll(replaceMark, "");
		}else{
			return "";
		}
	}
	
	/**
	 * 获取数据值
	 * @param key
	 * @param values
	 * @return
	 */
	public static String findValue(String key,Map<String,String> values){
		String returnObj ="";
		if(key!=null&&key.length()>0&&values!=null){
			if(!isCase){
				key=key.toUpperCase();
			}
			if(values.containsKey(key)){
				Object value =values.get(key);
				if(value!=null){
					returnObj=value.toString();
				}
				
			}
		}
		return returnObj;
	}
	
	
	public static Map<String,String[]>  analysisPageFun(String pageFun,Map<String,String> values){
		Map<String,String []> analysisMap =new LinkedHashMap<String,String[]>();
		
		if(pageFun!=null&&pageFun.length()>0){
			String [] funs =pageFun.split(PAGE_FUN_SPLIT);
			for(String fun:funs){
				String[] codes=fun.split("\\(");
				if(codes.length==2){
					analysisMap.put(codes[0], replaceMark(codes[1], PAGE_FUN_REPALCE_MARK).split(PAGE_MESSAGE_SPLIT));
				}
			}
		}
		
		return analysisMap;
	}
	
	
	public static void main(String []args){
		Map<String,String> values =new HashMap<String,String>();
		values.put("NAME", "ZHANGSAN");
		values.put("VALUE", "23");
		System.out.println(BulidValidUtil.replaceMarkString("  nihaoahoahao ", null));;
	}
}
