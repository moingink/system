package com.yonyou.util.validator.bulid;

import com.yonyou.util.validator.mark.BulidValidMarkAbs;

import net.sf.json.JSONObject;

public interface IBulidValidHead {
	
	public void appendJson(JSONObject obj,BulidValidMarkAbs bulidMark);
	
}
