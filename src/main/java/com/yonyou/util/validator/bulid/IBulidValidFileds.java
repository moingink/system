package com.yonyou.util.validator.bulid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.rale.IBulidFilesRule;

public interface IBulidValidFileds {
	
	public void appendJson(JSONObject obj,Map<String,ConcurrentHashMap<String, String>> valuesMaps,IBulidFilesRule rule,BulidValidMarkAbs bulidMark);
	
}
