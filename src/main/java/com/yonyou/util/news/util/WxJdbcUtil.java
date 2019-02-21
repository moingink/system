package com.yonyou.util.news.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.util.DateUtil;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.service.IBaseService;

public class WxJdbcUtil {
	
	@Autowired
	private  static IBaseService baseService =(IBaseService)SpringContextUtil.getBean("baseService");
	
	public static String insertWxNotice(Map<String,Object> dataMap ){
		dataMap.put("TIME", new Timestamp(System.currentTimeMillis()));
		dataMap.put("TS", DateUtil.findSystemDateString());
		return baseService.insert("WX_NOTICE", dataMap);
	}
	
	
	public static String insertWxNotice(String serial_number,String channel_code,String type,String tel,String content){
		
		Map<String,Object> insertMap =new HashMap<String,Object>();
		insertMap.put("SERIAL_NUMBER", serial_number);
		insertMap.put("CHANNEL_CODE", channel_code);
		insertMap.put("TYPE", type);
		insertMap.put("TEL", tel);
		insertMap.put("CONTENT", content);
		
		return insertWxNotice(insertMap);
	}
	
	public static String insertWxRecord(String tel,String type,String content){
		
		Map<String,Object> insertMap =new HashMap<String,Object>();
		insertMap.put("TEL", tel);
		insertMap.put("TYPE", type);
		insertMap.put("CONTENT", content);
		insertMap.put("TIME", new Timestamp(System.currentTimeMillis()));
		insertMap.put("TS", DateUtil.findSystemDateString());
		
		return baseService.insert("WX_RECORD", insertMap);
		
	}
	/**
	 * 添加微信流水
	 * @param serial_number
	 * @param type
	 * @param content
	 * @return
	 */
	public static String insertWxSerial(String serial_number,String type,String content){
		
		Map<String,Object> dataMap =new HashMap<String,Object>();
		
		dataMap.put("serial_number", serial_number);
		dataMap.put("type", type);
		dataMap.put("content", content);
		dataMap.put("time", new Timestamp(System.currentTimeMillis()));
		dataMap.put("ts", DateUtil.findSystemDateString());
		
		return baseService.insert("WX_SERIAL", dataMap);
		
		
	}
}
