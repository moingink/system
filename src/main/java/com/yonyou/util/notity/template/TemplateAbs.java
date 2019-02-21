package com.yonyou.util.notity.template;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.notity.template.entity.TempEntity;
import com.yonyou.util.replace.WhereReplaceUtil;

public abstract class TemplateAbs {
	
	protected String tempString="";
	
	protected String repalceMark ="#";
	
	protected Map<String,Object> tempData =new HashMap<String,Object>();
	
	public void initTempString(String temp){
		tempString =temp;
	}
	
	public void initTempData(Map<String,Object> tempData){
		this.tempData =tempData;
	}
	
	protected String findTempDataByColumnForString(String column){
		
		String returnString ="";
		if(tempData!=null&&tempData.containsKey(column)){
			return (String)tempData.get(column);
		}
		return returnString;
	}
	
	public TempEntity bulidTemp(Map<String,Object> dataMap){
		
		String head =this.bulidHead(dataMap);
		String main =this.bulidMain(dataMap);
		String end =this.bulidEnd(dataMap);
		String lineBreak =this.lineBreak();
		String message=this.bulidMessage(head, main, end, lineBreak);
		if(isReplace()){
			head=this.replaceTemp(head, dataMap);
			message =this.replaceTemp(message.toString(), dataMap);
		}else{
			message =message.toString();
		}
		TempEntity tempEntity =new TempEntity(head, main, end, message);
		return tempEntity;
	}
	
	protected  String bulidHead(Map<String,Object> dataMap){
		return "";
	}
	
	protected abstract String bulidMain(Map<String,Object> dataMap);
	
	protected  String bulidEnd(Map<String,Object> dataMap){
		return "";
	}
	
	protected  String lineBreak(){
		return "";
	}
	
	protected abstract boolean isReplace();
	
	protected String replaceTemp(String tempString,Map<String,Object> dataMap){
		
		return WhereReplaceUtil.replaceWhere(tempString, dataMap);
	}
	
	private String bulidMessage(String head,String main,String end,String lineBreak){
		StringBuffer messageBuff =new StringBuffer();
		messageBuff
		   //.append(head).append(lineBreak)
		   .append(main).append(lineBreak)
		   .append(end);
		return messageBuff.toString();
	}
	
	
}
