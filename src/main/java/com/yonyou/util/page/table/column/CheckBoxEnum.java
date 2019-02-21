package com.yonyou.util.page.table.column;

/**
 * 多选框枚举
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public enum CheckBoxEnum {
	/** 多选框*/
	CHECK("checkbox"),
	/** 单选框*/
	RADIO("radio"),
	/** 无*/
	NOCHECK("");
	String val;
	
	CheckBoxEnum(String _val){
		val=_val;
	}
	
	public String getVal(){
		return val;
	}
}
