package com.yonyou.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * VO组装类
 */
public class VoUtils  {
	private static String setterPrefix = "set";
	private static Logger log = LoggerFactory.getLogger(VoUtils.class);
	
	/**
	 * 功能: 把Map<String,Object>转成Vo
	 * @author wushqd
	 * @version 创建时间: 2014年11月30日 下午4:31:48
	 *
	 * @param clazz
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T transMapToVo(Class<T> clazz, Map<String,Object> map) {
		T target;
		try {
			target = (T) clazz.newInstance();
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			Entry<String, Object> entry = null;
			Object val = null;
			String setterName = "";
			String fieldName = "";
			
			while(it.hasNext()) {
				entry = it.next();
				val = entry.getValue();
				if(val!=null) {
					setterName = transKeyToSetter(entry.getKey());
					fieldName = transKeyToField(entry.getKey());
					Map<String,Method> methodMap = getDeclaredMethod(clazz);
					Map<String,Field> fieldMap = getDeclaredField(clazz);
					Method method = methodMap.get(setterName);
					Field field = fieldMap.get(fieldName);
					if(method!=null && field!=null) {
						Class claxx = field.getType();
						
						if(claxx == String.class) {
							method.invoke(target, (String)val);
						} else if(claxx == Date.class) {
							method.invoke(target, new Date(((Timestamp)val).getTime()));
						} else if(claxx == BigDecimal.class) {
							method.invoke(target, val);
						} else if(claxx == Long.class) {
							if(val.getClass() == BigDecimal.class) {
								method.invoke(target,((BigDecimal)val).longValue());
							} else if(val.getClass() == Long.class) {
								method.invoke(target, val);
							} else {
								method.invoke(target, Long.parseLong((String)val));
							}
						}
					}
					
					//Field field = clazz.getDeclaredField(fieldName);
					//Method method = clazz.getDeclaredMethod(setterName, claxx);
					
				}
			}
			return target;
		} catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 功能: 把List<Map<String,Object>>转成List<Vo>
	 * @author wushqd
	 * @version 创建时间: 2014年11月30日 下午4:31:39
	 *
	 * @param clazz
	 * @param datas
	 * @return
	 */
	public static <T> List<T> transMapToVos(Class<T> clazz, List<Map<String,Object>> datas) {
		List<T> list = new ArrayList<T>();
		for(int i=0;i<datas.size();i++) {
			Map<String,Object> map = datas.get(i);
			list.add(transMapToVo(clazz, map));
		}
		return list;
	}
	
	/**
	 * 功能: 根据Map结果集中的key值(如"CUST_ORDER_ID")转化成vo中的属性值("custOrderId")
	 * @author wushqd
	 * @version 创建时间: 2014年11月30日 下午3:08:02
	 *
	 * @param key
	 * @return
	 */
	private static String transKeyToField(String key) {
		String field = "";
		if(key!=null && !"".equals(key)) {
			String[] ignoreCaseStr = key.toLowerCase().split("_");
			String tmp = "";
			for(int i=0;i<ignoreCaseStr.length;i++) {
				tmp = ignoreCaseStr[i].trim();
				//field += tmp.substring(0, 1).toUpperCase()+tmp.substring(1, tmp.length());
				if(i>0) {
					field += tmp.substring(0, 1).toUpperCase()+tmp.substring(1, tmp.length());
				} else {
					field += tmp;
				}
			}
		}
		return field;
	}
	
	/**
	 * 功能: 根据Map结果集中的key值(如"CUST_ORDER_ID")转化成vo中的属性值("setCustOrderId")
	 * @author wushqd
	 * @version 创建时间: 2014年11月30日 下午3:22:46
	 *
	 * @param key
	 * @return
	 */
	private static String transKeyToSetter(String key) {
		String setter = setterPrefix;
		if(key!=null && !"".equals(key)) {
			String[] ignoreCaseStr = key.toLowerCase().split("_");
			String tmp = "";
			for(int i=0;i<ignoreCaseStr.length;i++) {
				tmp = ignoreCaseStr[i].trim();
				setter += tmp.substring(0, 1).toUpperCase()+tmp.substring(1, tmp.length());
			}
		}
		return setter;
	}
	
	/**
	 * 功能: 把对象方法以方法名为Key放入map中存储
	 * @author wushqd
	 * @version 创建时间: 2014年12月11日 上午11:53:54
	 *
	 * @param clazz
	 * @return
	 */
	private static Map<String,Method> getDeclaredMethod(Class clazz){
		Method[] methods = clazz.getDeclaredMethods();
		Map<String,Method> map = new HashMap<String,Method>();
		for(int i=0;i<methods.length;i++) {
			map.put(methods[i].getName(), methods[i]);
		}
		return map;
	}
	
	/**
	 * 功能: 把对象属性以方法名为Key放入map中存储
	 * @author wushqd
	 * @version 创建时间: 2014年12月11日 上午11:53:54
	 *
	 * @param clazz
	 * @return
	 */
	private static Map<String,Field> getDeclaredField(Class clazz) {
		Map<String,Field> map = new HashMap<String,Field>();
		Field[] fields = clazz.getDeclaredFields();
		for(int i=0;i<fields.length;i++) {
			map.put(fields[i].getName(), fields[i]);
		}
		return map;
	}
}
