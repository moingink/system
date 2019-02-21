package com.yonyou.util.page.table.editable.util;

public enum EditableTypeEnum {
	
	//编辑框的类型。支持text|textarea|select|date|checklist等
	
	TEXT("text","文本"),
	TEXTAREA("textarea","文本域"),
	SELECT("select","下拉框"),
	DATE("date","日期"),
	CHECKLIST("checklist","多复选框");
	
	private String type;
	private String name;
	
	EditableTypeEnum(String _type,String _name){
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
