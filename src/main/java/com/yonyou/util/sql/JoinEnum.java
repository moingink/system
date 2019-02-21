package com.yonyou.util.sql;

/**
 * json关联枚举类
 * @author moing_ink
 *
 */
public enum JoinEnum {
	
	/**
	 * 内关联
	 */
	INNER("INNER","内连接"),
	/**
	 * 左关联
	 */
	LEFT("LEFT","左连接"),
	/**
	 * 右关联
	 */
	RIGTH("RIGTH","右连接");
	
	/**
	 * 关联类型
	 */
	String type;
	/**
	 * 关联名称
	 */
	String name;
	
	JoinEnum(String _type,String _name){
		type=_type;
		name=_name;
	}
	
	/**
	 * 获取类型
	 * @return josn 关联类型
	 */
	public String getType(){
		return type;
	}
	/**
	 * 获取名称
	 * @return json 关联名称
	 */
	public String getName(){
		return name;
	}
}
