package com.yonyou.util.notity.send;

public enum SendTypeEnum {
	
	 NOTICE("0","公告"),
	 SMS("1","短信"),
	 EMAIL("2","邮件"),
	 WX("3","微信");
	 String type;
	 String name;
	 
	 SendTypeEnum(String _type,String _name){
		 type=_type;
		 name=_name;
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
