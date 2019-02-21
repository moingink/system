package org.util;

import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;

public class JacksonJsonUtil {
	//private static Logger log = RmLogHelper.getLogger(JacksonJsonUtil.class);
	private static CustomObjectMapper mapper = new CustomObjectMapper();
	
	public static CustomObjectMapper getMapperInstance(boolean createNew) {
		if (createNew) {   
			return new CustomObjectMapper();   
		} else {
			return mapper;
		}
	}
	
	/**
	 * 功能: bean序列化JSON字符串
	 * @author wushqd
	 * @version 创建时间: 2015年7月23日 上午11:03:28
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJson(Object bean) {
		try {
			return getMapperInstance(false).writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("对象序列化成JSON时异常："+e.getMessage());
		}
	}
	
	/**
	 * 功能: JSON反序列化
	 * @author wushqd
	 * @version 创建时间: 2015年7月23日 上午11:03:47
	 *
	 * @param json
	 * @param t
	 * @return
	 */
	public static <T> T jsonToBean(String json,Class<T> t) {
		try {
			return  getMapperInstance(false).readValue(json, t);
		} catch (Exception e) {
			throw new RuntimeException("JSON数据反序列化时异常："+e.getMessage());
		}
	}
	/**
	 * 功能: json反序列化 List<bean> 类型的数据
	 * @author wushqd
	 * @version 创建时间: 2015年8月31日 下午5:16:55
	 *
	 * @param json
	 * @param t
	 * @param collectionsClass
	 * @return
	 */
	public static <T> List<T> jsonToListBean(String json,Class<T> t) {
		JavaType tp = getMapperInstance(false).getTypeFactory().constructParametricType(List.class, t);
		try {
			return getMapperInstance(false).readValue(json, tp);
		} catch (Exception e) {
			throw new RuntimeException("对象序列化成JSON时异常："+e.getMessage());
		} 
	}
	/**
	 * 功能: json反序列化Map<String,bean> 类型的数据
	 * @author wushqd
	 * @version 创建时间: 2015年8月31日 下午5:32:44
	 *
	 * @param json
	 * @param t
	 * @return
	 */
	public static <T> Map<String,T> jsonToMapBean(String json,Class<T> t) {
		JavaType tp = getMapperInstance(false).getTypeFactory().constructParametricType(Map.class, String.class,t);
		try {
			return getMapperInstance(false).readValue(json, tp);
		} catch (Exception e) {
			throw new RuntimeException("对象序列化成JSON时异常："+e.getMessage());
		} 
	}
	
	public static void main(String[] args) {
	}
}