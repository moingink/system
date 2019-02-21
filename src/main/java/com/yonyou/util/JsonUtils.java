package com.yonyou.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * @author zzh
 * @version 创建时间：2016年9月14日
 * JSON工具类
 */

public class JsonUtils {
    /**页面传至后台时，json数据在request的参数名称*/  
    public final static String JSON_ATTRIBUTE = "json";  
    public final static String JSON_ATTRIBUTE1 = "json1";  
    public final static String JSON_ATTRIBUTE2 = "json2";  
    public final static String JSON_ATTRIBUTE3 = "json3";  
    public final static String JSON_ATTRIBUTE4 = "json4";  
	
	private static Log log = LogFactory.getLog(JsonUtils.class);

	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		}else if(obj instanceof Timestamp){
			 String datePattern = "yyyy-MM-dd HH:mm:ss";
	            SimpleDateFormat sdf = new SimpleDateFormat(datePattern,
	                    Locale.CHINESE);
	            json.append("\"").append(string2json(sdf.format((Date) obj))).append("\"");
		}
		else if(obj instanceof Date){
				 String datePattern = "yyyy-MM-dd";
		            SimpleDateFormat sdf = new SimpleDateFormat(datePattern,
		                    Locale.CHINESE);
		            json.append("\"").append(string2json(sdf.format((Date) obj))).append("\"");
		}
		else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		return json.toString();

	}

	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			int count = 0;  
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					
					if(value == null || value.trim().equals("\"\"")){
						continue;
					}
					
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
					
					count++;
					
				} catch (Exception e) {
					
				}
			}
			if(count == 0){
				json.append("}");
			}else{
				json.setCharAt(json.length() - 1, '}');
			}

		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	/*
	 * zhou lichen json-lib
	 * */
    /** 
     * 从一个JSON 对象字符格式中得到一个java对象，形如： 
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
     * @param <T>
     * @param object 
     * @param clazz 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	public static <T> T getDTO(String jsonString, Class<T> clazz){  
        JSONObject jsonObject = null;  
        try{  
            setDataFormat2JAVA();   
            jsonObject = JSONObject.fromObject(jsonString);  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return (T)JSONObject.toBean(jsonObject, clazz);  
    }  
      
    /** 
     * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如： 
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}, 
     * beansList:[{}, {}, ...]} 
     * @param jsonString 
     * @param clazz 
     * @param map 集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" : Bean.class) 
     * @return 
     */  
    public static Object getDTO(String jsonString, Class clazz, Map map){  
        JSONObject jsonObject = null;  
        try{  
            setDataFormat2JAVA();   
            jsonObject = JSONObject.fromObject(jsonString);  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return JSONObject.toBean(jsonObject, clazz, map);
    }
      
    /** 
     * 从一个JSON数组得到一个java对象数组，形如： 
     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...] 
     * @param object 
     * @param clazz 
     * @return 
     */  
    public static Object[] getDTOArray(String jsonString, Class clazz){  
        setDataFormat2JAVA();  
        JSONArray array = JSONArray.fromObject(jsonString);  
        Object[] obj = new Object[array.size()];  
        for(int i = 0; i < array.size(); i++){  
            JSONObject jsonObject = array.getJSONObject(i);  
            obj[i] = JSONObject.toBean(jsonObject, clazz);  
        }  
        return obj;  
    }  
      
    /** 
     * 从一个JSON数组得到一个java对象数组，形如： 
     * [{"id" : idValue, "name" : nameValue}, {"id" : idValue, "name" : nameValue}, ...] 
     * @param object 
     * @param clazz 
     * @param map 
     * @return 
     */  
    public static Object[] getDTOArray(String jsonString, Class clazz, Map map){  
        setDataFormat2JAVA();  
        JSONArray array = JSONArray.fromObject(jsonString);  
        Object[] obj = new Object[array.size()];  
        for(int i = 0; i < array.size(); i++){  
            JSONObject jsonObject = array.getJSONObject(i);  
            obj[i] = JSONObject.toBean(jsonObject, clazz, map);  
        }  
        return obj;  
    }  
      
    /** 
     * 从一个JSON数组得到一个java对象集合 
     * @param <T>
     * @param object 
     * @param clazz 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	public static <T> List<T> getDTOList(String jsonString, Class<T> clazz){  
        setDataFormat2JAVA();  
        JSONArray array = JSONArray.fromObject(jsonString);  
        List<T> list = new ArrayList<T>(); 
        for(Iterator<JSONObject> iter = array.iterator(); iter.hasNext();){  
            JSONObject jsonObject = iter.next();  
            list.add((T)JSONObject.toBean(jsonObject, clazz));  
        }
        return list;
    }  
      
    /** 
     * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性 
     * @param object 
     * @param clazz 
     * @param map 集合属性的类型 
     * @return 
     */  
    public static List getDTOList(String jsonString, Class clazz, Map map){  
        setDataFormat2JAVA();  
        JSONArray array = JSONArray.fromObject(jsonString);  
        List list = new ArrayList();  
        for(Iterator iter = array.iterator(); iter.hasNext();){  
            JSONObject jsonObject = (JSONObject)iter.next();  
            list.add(JSONObject.toBean(jsonObject, clazz, map));  
        }  
        return list;  
    }  
      
    /** 
     * 从json HASH表达式中获取一个map，该map支持嵌套功能 
     * 形如：{"id" : "johncon", "name" : "小强"} 
     * 注意commons-collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap 
     * @param object 
     * @return 
     */  
    public static Map<String, Object> getMapFromJson(String jsonString) {  
        setDataFormat2JAVA();  
        JSONObject jsonObject = JSONObject.fromObject(jsonString);  
        Map<String, Object> map = new HashMap<String, Object>();  
        for(Iterator<?> iter = jsonObject.keys(); iter.hasNext();){  
            String key = (String)iter.next();  
            map.put(key, jsonObject.get(key));
        }  
        return map;  
    }  
      
    /** 
     * 从json数组中得到相应java数组 
     * json形如：["123", "456"] 
     * @param jsonString 
     * @return 
     */  
    public static Object[] getObjectArrayFromJson(String jsonString) {  
        JSONArray jsonArray = JSONArray.fromObject(jsonString);  
        return jsonArray.toArray();  
    }  
  
  
    /** 
     * 把数据对象转换成json字符串 
     * DTO对象形如：{"id" : idValue, "name" : nameValue, ...} 
     * 数组对象形如：[{}, {}, {}, ...] 
     * map对象形如：{key1 : {"id" : idValue, "name" : nameValue, ...}, key2 : {}, ...} 
     * @param object 
     * @return 
     */  
    public static String getJSONString(Object object) throws Exception{  
        String jsonString = null;  
        //日期值处理器  
        JsonConfig jsonConfig = new JsonConfig();  
        //jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());  
        if(object != null){  
            if(object instanceof Collection || object instanceof Object[]){  
                jsonString = JSONArray.fromObject(object, jsonConfig).toString();  
            }else{  
                jsonString = JSONObject.fromObject(object, jsonConfig).toString();  
            }  
        }  
        return jsonString == null ? "{}" : jsonString;  
    }
      
    private static void setDataFormat2JAVA(){  
        //设定日期转换格式  
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));  
    }  
	
	public static void main(String[] args) {

		String str = "{deal1:{oper: 'insert',entity:'ma_product', data:{a1@V@*F*:123,a2@V@*F*:'@123'}},deal2:{oper: 'insert',entity:'ma_product', data:{a1@V@*F*:456,a2@V@*F*:'@123'}}}";
		
		Map map = JsonUtils.getMapFromJson(str);
		System.out.println(map.toString());
		System.out.println(((Map)(((Map)map.get("deal1")).get("data"))).get("a1@V@*F*"));
	}
}
