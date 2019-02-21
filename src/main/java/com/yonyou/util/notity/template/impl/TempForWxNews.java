package com.yonyou.util.notity.template.impl;

import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.news.mapping.NotityMappingUtil;
import com.yonyou.util.notity.template.TemplateAbs;

public abstract  class TempForWxNews extends TemplateAbs{

	
	protected String template_id="";
	protected String url ="http://weixin.qq.com/download ";
	protected String appid="xiaochengxuappid12345";
	protected String pagepath ="index?foo=bar";
	
	
	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		System.out.println("##########"+this.findTempDataByColumnForString("PARAM_MESSAGE"));
		return bulidWxMessage(dataMap);
	}

	@Override
	protected boolean isReplace() {
		// TODO 自动生成的方法存根
		return false;
	}
	
	protected abstract String [] findDataColumn();
	
	protected String findTemplate_id(){
		return template_id;
	}
	protected String findUrl(){
		return url;
	}
	protected String findAppid(){
		return appid;
	}
	protected String findPagepath(){
		return pagepath;
	}
	
	
	
	private String bulidWxMessage(Map<String, Object> dataMap){
		
		JSONObject wxObj =new JSONObject();
		
		wxObj.put("touser", "OPENID");
		wxObj.put("template_id", findTemplate_id());
		wxObj.put("url", findUrl());
		JSONObject miniprogram =new JSONObject();
		miniprogram.put("appid", findAppid());
		miniprogram.put("pagepath", findPagepath());
		wxObj.put("miniprogram", miniprogram);
		JSONObject data =new JSONObject();
		
		for(String column:findDataColumn()){
			JSONObject obj =new JSONObject();
			String mappingColumn=NotityMappingUtil.mapping(column);
			String value="";
			if(dataMap.containsKey(mappingColumn)){
				value =(String)dataMap.get(mappingColumn);
			}
			this.bulidJsonData(obj, column, value, dataMap);
			data.put(column, obj);
		}	
		wxObj.put("data", data);
	    return wxObj.toString();   
	}
	
	protected void appendDateMap(Map<String, Object> dataMap){
		
	}
	
	protected void bulidJsonData(JSONObject obj,String column,String value,Map<String,Object> dataMap){
		obj.put("value", value);
	}
	
	

}
