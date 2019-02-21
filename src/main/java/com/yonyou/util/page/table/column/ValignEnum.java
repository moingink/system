package com.yonyou.util.page.table.column;

/**
 * 垂直对齐方式 枚举
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public enum ValignEnum {
	
	TOP("top"),
	MIDDLE("middle"),
	BOTTOM("bottom");
	String val;
	
	ValignEnum(String _val){
		val=_val;
	}
	
	public String getVal(){
		return val;
	}
	
}
