package com.yonyou.util.validator.mark;

public interface IBulidValidMark {
	
	/* 默认信息 */
	public static String MESSAGE ="message";
	/* 容器信息 */
	public static String CONTAINER="container";
	/* 图表信息 */
	public static String FEEDBACKICONS="feedbackIcons";
	/* 隐藏判断 */
	public static String EXCLUDED ="excluded";
	
	
	/* 字段信息 */
	public static String FIELDS ="fields";
	/* 验证标示 */
	public static String VALIDATORS ="validators";
	/* 非空验证标示 */
	public static String NOTEMPTY ="notEmpty";
	/* 字符验证标示 【大写/小写】*/
	public static String STRINGCASE="stringCase";
	/* 正则表达式标示【大写/小写】*/
	public static String REGEXP="regexp";
	/* 长度验证标识 */
	public static String STRINGLENGTH ="stringLength";
	/* 两个字段不相同的判断标识 */
	public static String DIFFERENT ="different";
	/* Email地址验证标识 */
	public static String EMAILADDRESS ="emailAddress";
	/* 日期验证 */
	public static String DATE ="date";
	/* 数字验证 */
	public static String DIGITS="digits";
	
	public static String DECIMAL="DECIMAL";
	/* ajax验证  */
	public static String THRESHOLD="threshold";
	/* 复选框验证 */
	public static String CHOICE="choice";
	/* 密码确认验证 */
	public static String IDENTICAL="identical";
	/* 数值大于验证 */
	public static String GREATERTHAN="greaterThan";
	/* 数值小于验证 */
	public static String LESSTHAN="lessThan";
	/* 远程验证*/
	public static String REMOTE="remote";
	
	/* 金额（单位:元小数点保留2位，万元小数点保留6位）*/
	public static String MONEY="money";
	
	/* 校验格式*/
	public static String FORMAT="format";
	
	/* 最小值*/
	public static String MIN="min";
	
	/* 最大值*/
	public static String MAX="max";
	
	
	
	
}
