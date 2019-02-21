package com.yonyou.util.news.mapping;

public enum NotityMappingEnum {
	
	//"result","withdrawMoney","withdrawTime","cardInfo","arrivedTime","remark"
	ACCOUNT("account","账号","a1","a2","a3","a4","a5"),
	BANK_MESSAGE("bank_message","银行信息","cardInfo"),
	Money("money","金额","withdrawMoney","m2","m3","m4"),
	TIME("time","时间","withdrawTime","arrivedTime"),
	CONTENT("content","内容","result");
	
	
	private String column;
	private String name;
	private String [] mappingColumn;
	
	NotityMappingEnum(String column,String name,String... mappingColumn){
		this.column=column;
		this.name=name;
		this.mappingColumn=mappingColumn;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getMappingColumn() {
		return mappingColumn;
	}

	public void setMappingColumn(String[] mappingColumn) {
		this.mappingColumn = mappingColumn;
	}
	
	
	
}
