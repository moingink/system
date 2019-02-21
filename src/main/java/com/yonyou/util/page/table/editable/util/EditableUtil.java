package com.yonyou.util.page.table.editable.util;

import java.util.HashMap;
import java.util.Map;

public class EditableUtil {
	
	private static Map<String,String> VALIDATE_MAP =new HashMap<String,String>();
	
	public static String BIGINT ="BIGINT";
	
	static {
		VALIDATE_MAP.put(BIGINT, "function (v) \\{  if (isNaN(v)) return '必须是数字'; var age = parseInt(v);  if (age <= 0) return '必须是正整数'; \\} ");
	}
	
	public static String getValidate(String type){
		if(VALIDATE_MAP.containsKey(type)){
			return VALIDATE_MAP.get(type);
		}else{
			return "";
		}
	}
}
