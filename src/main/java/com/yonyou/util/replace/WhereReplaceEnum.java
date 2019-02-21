package com.yonyou.util.replace;

import java.util.HashMap;
import java.util.Map;

public enum WhereReplaceEnum  {
	
	COMPANY("COMPANY","公司信息",ReplaceTypeEnum.REPLACE_OBJ),
	USER("USER","用户信息",ReplaceTypeEnum.REPLACE_OBJ),
	DATE("DATE","日期信息",ReplaceTypeEnum.REPLACE_DATE);
	
	
	String code;
	String name;
	ReplaceTypeEnum typeEnum;
	WhereReplaceEnum(String _code,String _name,ReplaceTypeEnum _typeEnum){
		code=_code;
		name=_name;
		typeEnum=_typeEnum;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	public ReplaceTypeEnum getTypeEnum() {
		return typeEnum;
	}


	public void setTypeEnum(ReplaceTypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}


	public Map<String,ReplaceTypeEnum> toMap(){
		
		Map<String,ReplaceTypeEnum> mapdate=new HashMap<String,ReplaceTypeEnum>();
		for(WhereReplaceEnum replaceEnum:WhereReplaceEnum.values()){
			mapdate.put(replaceEnum.getCode(), replaceEnum.getTypeEnum());
		}
		
		return mapdate;
		
	}
	
	
}
