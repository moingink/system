package com.yonyou.util.sql;

/**
 * 数据元类型映射
 * @author moing_ink
 * <p>创建时间 ： 2016年12月26日
 * @version 1.0
 */
public class MetaCodeMappingType {
	
	/** VARCHAR */
	private static String VARCHAR="VARCHAR";
	/** VARCHAR2 */
	private static String VARCHAR2="VARCHAR2";
	/** 日期 */
	private static String DATE="DATE";
	/** 时间 */
	private static String TIMESTAMP="TIME";
	/** 数值 */
	private static String NUMBER="NUMBER";
	
	private static String BIGINT="BIGINT";
	
	private static String DECIMAL="DECIMAL";
	/**
	 * 映射类型 
	 * @param filed_code 字段编码
	 * @return 映射后字段编码
	 */
	public static String mappingType(String filed_code){
		String returnType ="";
		filed_code=filed_code.toUpperCase();
		if(filed_code.equals(DATE)){
			returnType=StringToObjUtil.DATE;
		}else if(filed_code.endsWith(TIMESTAMP)){
			returnType=StringToObjUtil.TIMESTAMP;
		}else if(filed_code.endsWith(NUMBER)){
			returnType=StringToObjUtil.NUMBER;
		}else if(filed_code.endsWith(BIGINT)){
			returnType=StringToObjUtil.NUMBER;
		}else if(filed_code.endsWith(DECIMAL)){
			returnType=StringToObjUtil.NUMBER;
		}
		else if(filed_code.endsWith(VARCHAR)){
			returnType=StringToObjUtil.STRING;
		}else if(filed_code.endsWith(VARCHAR2)){
			returnType=StringToObjUtil.STRING;
		}else{
			returnType=filed_code;
		}
		return returnType;
	}
	
}
