package com.yonyou.util.notity.template.impl;

import java.util.Map;

import com.yonyou.util.notity.template.TemplateAbs;

public class TempForNotice extends TemplateAbs {

	@Override
	protected String bulidHead(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "系统公告";
	}

	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "将于今天12：00---5：00进行系统更新";
	}

	@Override
	protected String bulidEnd(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "---";
	}

	@Override
	protected String lineBreak() {
		// TODO 自动生成的方法存根
		return "";
	}
	
	@Override
	protected boolean isReplace() {
		// TODO 自动生成的方法存根
		return false;
	}

}
