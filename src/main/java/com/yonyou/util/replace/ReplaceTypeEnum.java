package com.yonyou.util.replace;

public enum ReplaceTypeEnum {
	
	
	REPLACE_OBJ("obj",0,"根据对象值进行替换"),
	REPLACE_DATE("date",1,"日期替换"),
	REPLACE_STRING("string",2,"字符串进行替换");
	
	
	String code;
	Integer value;
	String name;
	ReplaceTypeEnum(String _code,Integer _value,String _name){
		code=_code;
		value=_value;
		name=_name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return String.valueOf(value);
	}
	
}
