package com.yonyou.util;

import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.wsystem.api.HttpRequestAbs;
import com.yonyou.util.wsystem.impl.HttpRequestByData;

public class HttpRequestUtil {
	
	private  static HttpRequestAbs  HTTP =new HttpRequest();
	
	public static JSONObject httpRespJSON(String url, String jsonString){
		return HTTP.httpRespJSON(url, jsonString);
	}
	
	public static Map httpRespMap(String url, String jsonString){
		return HTTP.httpRespJSON(url, jsonString);
	}
	
}
