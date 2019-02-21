package com.yonyou.util.validator.bulid.imp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.util.validator.bulid.IBulidValidFileds;
import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.mark.IBulidValidMark;
import com.yonyou.util.validator.rale.IBulidFilesRule;
import com.yonyou.util.validator.util.BulidValidUtil;

public class BulidValidFileds implements IBulidValidFileds{

	@Override
	public void appendJson(JSONObject obj,
			Map<String,ConcurrentHashMap<String, String>> valuesMaps, IBulidFilesRule rule,BulidValidMarkAbs bulidMark) {
		// TODO 自动生成的方法存根
		
		JSONObject fileds =new JSONObject();
		for(String key:valuesMaps.keySet()){
			Map<String,String> values =valuesMaps.get(key);
			JSONObject validators =new JSONObject();
			JSONObject fieldTypes =new JSONObject();
			Map<String,JSONObject> filedTypeMap =rule.queryFiledsJsons(bulidMark, values);
			for(String type:filedTypeMap.keySet()){
				fieldTypes.put(type, filedTypeMap.get(type));
			}
			validators.put(IBulidValidMark.VALIDATORS,fieldTypes);
			fileds.put(key, validators);
		}
		obj.put(IBulidValidMark.FIELDS, fileds);
	}

	
}
