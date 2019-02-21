package com.yonyou.util.page.table.column;

/**
 * 排列枚举类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public enum AlignEnum {
	/** 左排序*/
	LEFT("left"),
	/** 居中排序*/
	RIGTH("rigth"),
	/** 右排序*/
	CENTER("center");
	String val;
	
	AlignEnum(String _val){
		val=_val;
	}
	
	public String getVal(){
		return val;
	}
}
