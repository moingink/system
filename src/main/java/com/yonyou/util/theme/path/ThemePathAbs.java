package com.yonyou.util.theme.path;

import java.util.Map;

import com.yonyou.util.theme.vo.ThemePathEntity;

public abstract class ThemePathAbs  {
	
	protected ThemePathAbs(){
		init();
	}
	
	protected  abstract void init();
	
	public ThemePathEntity findData(String themeCode,String position_code,String system_code){
		
		return this.queryData(themeCode).get(this.getKey(position_code, system_code));
		
	}
	
	public  abstract Map<String,ThemePathEntity> queryData(String themeCode);
	
	
	public String getKey(String position_code,String systemCode){
		String key=position_code;
		if(position_code!=null&&position_code.length()>0){
			position_code=position_code.trim();
			key=position_code;
		}
		if(systemCode!=null&&systemCode.length()>0){
			key+="."+systemCode;
		}
		return key;
	}
	
}
