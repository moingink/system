package com.yonyou.util.page.table.editable.util;

public interface  IEditable {
	
	//编辑框的类型。支持text|textarea|select|date|checklist等
	public String TYPE="type"; 
	//编辑框的标题
	public String TITLE="title";
	//是否禁用编辑
	public String DISABLED="disabled";
	//空值的默认文本
	public String EMPTY_TEXT="emptytext";
	 //编辑框的模式：支持popup和inline两种模式，默认是popup
	public String MODE="mode";
	 //验证函数
	public String VALIDATE="validate";
	//展示数据
	public String SOURCE="source";
}
