package org.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 功能: 
 *
 * @author wushqd 
 * @version 创建时间: 2015年7月22日下午8:55:27
 *
 */
public class CustomObjectMapper extends ObjectMapper{
	private static final long serialVersionUID = -4888615119213991391L;

	public CustomObjectMapper() {
		super();
		//设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,true);
		//允许空字符串可以等价于JSON null
		this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		//允许单引号
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//字段和值都加引号
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 数字也加引号
		this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
		this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
		// 空值处理为空串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
				jg.writeString("");
			}
		});
		//设置日期转换yyyy-MM-dd HH:mm:ss  
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
}
