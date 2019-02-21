package com.yonyou.util.validator.mark;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.validator.type.BulidTypeEnum;
import com.yonyou.util.validator.util.BulidValidUtil;

public abstract class BulidValidMarkAbs implements IBulidValidMark {
	
	
	protected Map<String,String> markMap =new HashMap<String,String>();
	
	
	public void init(){
		if(markMap.size()==0){
			this.initMarkMap();
		}
	}
	
	
	public  JSONObject findJson(BulidTypeEnum bulidTypeEnum,String filed_type,Map<String,String> valuesMap){
		JSONObject obj =new JSONObject();
		String typeMessgae =this.findTypeMessage(bulidTypeEnum,filed_type);
		//替换信息
		typeMessgae=BulidValidUtil.replaceMarkString(typeMessgae, valuesMap);
		String [] types =typeMessgae.split(",");
		for(String type:types){
			String [] messages =type.split(":");
			if(messages.length==2){
				obj.put(messages[0], messages[1]);
			}
		}
		return obj;
	}
	
	protected abstract void initMarkMap();
	
	protected abstract String findTypeMessage(BulidTypeEnum bulidTypeEnum,String filed_type);
	
	public abstract Map<String,String []> findAnalysisType(String filed_type,Map<String,String> dataMap);
	
	protected String findKey(BulidTypeEnum bulidTypeEnum,String filed_type){
		return bulidTypeEnum.getType()+"_"+filed_type;
	}
}
