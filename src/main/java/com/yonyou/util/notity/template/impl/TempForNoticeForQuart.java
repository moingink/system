package com.yonyou.util.notity.template.impl;

import java.util.Map;

import com.yonyou.util.notity.template.TemplateAbs;

public class TempForNoticeForQuart extends TemplateAbs {

	@Override
	protected String bulidHead(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "调度_#JOB_NAME#";
	}

	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "#DATE#  调度信息,#JOB_NAME#，服务名：#SYSTEM_NAME#,地址：#API_URL#";
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
