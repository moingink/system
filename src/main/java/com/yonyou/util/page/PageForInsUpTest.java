package com.yonyou.util.page;

import java.util.Map;

import com.yonyou.util.page.util.SingleQueryFrameUtil;

public class PageForInsUpTest extends PageForInsUp{
	
	
	@Override
	public String appendHtml(Map<String, Object> dataMap,int row,int col) {
		// TODO 自动生成的方法存根
		return "";
	}
	
	@Override
	protected String findInputType(String inputType) {
		// TODO 自动生成的方法存根
		return SingleQueryFrameUtil.TYPE_TEXT_VAL;
	}
	
	@Override
	protected boolean isPutPageHtml(String id, String input_type, String value) {
		// TODO 自动生成的方法存根
		return true;
	}
	
	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE_TEXT;
	}
	
}
