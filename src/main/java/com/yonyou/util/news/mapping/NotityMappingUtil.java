package com.yonyou.util.news.mapping;

import java.util.HashMap;
import java.util.Map;

public class NotityMappingUtil {
	
	private static final Map<String,String> MAPPING_MAP =new HashMap<String,String>();
	
	
	static {
		
		for(NotityMappingEnum mappingEnum: NotityMappingEnum.values()){
			
			for(String column:mappingEnum.getMappingColumn()){
				MAPPING_MAP.put(column, mappingEnum.getColumn());
			}
			
		}
		
	}
	
	
	public static String mapping(String column){
		if(MAPPING_MAP.containsKey(column)){
			return MAPPING_MAP.get(column);
		}else{
			System.out.println("没有获取映射信息！");
			return column;
		}
	}
	
}
