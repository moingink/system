package com.yonyou.util.validator.rale;

import java.util.Map;

import net.sf.json.JSONObject;

import com.yonyou.util.validator.mark.BulidValidMarkAbs;

public interface IBulidFilesRule {
	
	/**
	 * 追加JSON对象信息
	 * @param obj
	 * @param values
	 */
	public Map<String,JSONObject> queryFiledsJsons(BulidValidMarkAbs bulidMark,Map<String,String> values);
}
