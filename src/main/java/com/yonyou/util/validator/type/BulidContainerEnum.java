package com.yonyou.util.validator.type;

public enum BulidContainerEnum {
	
	TOOLTIP("tooltip","气泡提示"),
	DIV("#errors","DIV层提示"),
	DEF("","默认提示");
	
	private String type;
	private String name;
	
	BulidContainerEnum(String _type,String _name){
		this.type=_type;
		this.name=_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
