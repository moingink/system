package com.yonyou.util.translate.entity;

/**
 * 翻译实体类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class TranslateEntity {
	
	/**
	 * 翻译对象信息
	 */
	private Object translate =null;
	
	public TranslateEntity(Object val){
		translate =val;
	}
	/**
	 * 设置值信息
	 * @param val 对象
 	 */
	public void setValue(Object val){
		translate =val;
	}
	/**
	 * 获取值信息
	 * @return 对象
	 */
	public Object getValue(){
		return translate;
	}
}
