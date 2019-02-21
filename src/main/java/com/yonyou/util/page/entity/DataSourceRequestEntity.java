package com.yonyou.util.page.entity;

public class DataSourceRequestEntity {
	
	private String [] colArrays;
	
	private String pageCode;
	private String metaCode;
	private String type;
	
	public DataSourceRequestEntity(String [] _colArrays,String _pageCode,String _metaCode,String _type){
		colArrays=_colArrays;
		pageCode=_pageCode;
		metaCode=_metaCode;
		type=_type;
	}
	
	public String[] getColArrays() {
		return colArrays;
	}
	public String getPageCode() {
		return pageCode;
	}
	public String getMetaCode() {
		return metaCode;
	}

	public String getType() {
		return type;
	}
	
}
