package com.yonyou.util.notity.template.impl;

import java.util.Map;

import com.yonyou.util.notity.template.TemplateAbs;

public class TempForNoticeManage extends TemplateAbs {

	@Override
	protected String bulidHead(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "#TITLE#";
	}

	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "#NOTICE_MESSAGE#";
	}

	@Override
	protected String bulidEnd(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "";
	}

	@Override
	protected String lineBreak() {
		// TODO 自动生成的方法存根
		return "";
	}
	
	@Override
	protected boolean isReplace() {
		// TODO 自动生成的方法存根
		return true;
	}

}
