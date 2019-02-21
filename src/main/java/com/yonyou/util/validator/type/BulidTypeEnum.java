package com.yonyou.util.validator.type;

public enum BulidTypeEnum {
	
	HEAD("HEAD","HEAD"),
	FILED("FILED","FILED");
	
	private String type;
	private String name;
	
	BulidTypeEnum(String _type,String _name){
		type=_type;
		name=_name;
	}
	
	public String getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
}
