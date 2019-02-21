package com.yonyou.util.notity.template.impl;

import java.util.Map;

import com.yonyou.util.notity.template.TemplateAbs;

public class TempForEmail extends TemplateAbs {

	@Override
	protected String bulidHead(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "你好 ";
	}

	@Override
	protected String bulidMain(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "这是一封测试邮件";
	}

	@Override
	protected String bulidEnd(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		return "祝好！";
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
