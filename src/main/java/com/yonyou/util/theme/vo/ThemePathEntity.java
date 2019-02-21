package com.yonyou.util.theme.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ThemePathEntity implements Serializable {
	
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5466501667990289583L;
	
	private Map<String,String> initData =new HashMap<String,String>();
	
	public ThemePathEntity(Map<String,Object> data){
		
		for(String key:data.keySet()){
			initData.put(key, String.valueOf(data.get(key)));
		}
	}
	 
	public String getId() {
		return initData.get("ID");
	}
	public String getDef_name() {
		return initData.get("DEF_NAME");
	}
	public String getPosition_code() {
		return initData.get("POSITION_CODE");
	}
	public String getSystem_code() {
		return initData.get("SYSTEM_CODE");
	}
	public String getVisit_path() {
		return initData.get("VISIT_PATH");
	}
	public String getVisit_file_name() {
		return initData.get("VISIT_FILE_NAME");
	}
	public String getMessage() {
		return initData.get("MESSAGE");
	}
	public String getOption_type() {
		return initData.get("OPTION_TYPE");
	}
	public String getIs_dr() {
		return initData.get("IS_DR");
	}
	 
	 
	 
	 
}
