package com.yonyou.util.notity.template.impl;

import java.util.Map;

import com.yonyou.util.notity.template.TemplateAbs;

public class TempForSms extends TemplateAbs{

	@Override
	protected String bulidHead(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "尊敬的用户，您好:";
	}

	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "您的话费已与余额不足，请及时充值!";
	}

	@Override
	protected String bulidEnd(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "用友广信";
	}

	@Override
	protected String lineBreak() {
		// TODO 自动生成的方法存根
		return "\n";
	}

	@Override
	protected boolean isReplace() {
		// TODO 自动生成的方法存根
		return false;
	}

}
