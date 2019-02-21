package com.yonyou.util.page.table.column;

/**
 * 是否展示枚举
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public enum IsShowEnum {
	
	TRUE(true),
	FALSE(false);
	
	boolean val;
	
	IsShowEnum(boolean _val){
		val=_val;
	}
	
	public boolean getVal(){
		return val;
	}
}
