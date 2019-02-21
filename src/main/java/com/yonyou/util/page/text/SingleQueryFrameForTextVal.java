package com.yonyou.util.page.text;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

public class SingleQueryFrameForTextVal extends SingleQueryFrameAbs {

	@Override
	protected String findSingleFrame(String id, String name, String value,
			Map<String, String> message) {
		// TODO 自动生成的方法存根
		return "<label class='control-label'><font color='888888'>"+value+"</font></label>";
	}
	
}
