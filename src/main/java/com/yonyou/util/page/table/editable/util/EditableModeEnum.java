package com.yonyou.util.page.table.editable.util;

public enum EditableModeEnum {
	
	//编辑框的模式：支持popup和inline两种模式，默认是popup
	
	POPUP("popup","弹出编辑"),
	INLINE("inline","内嵌编辑");
	
	private String type;
	private String name;
	
	EditableModeEnum(String _type,String _name){
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
