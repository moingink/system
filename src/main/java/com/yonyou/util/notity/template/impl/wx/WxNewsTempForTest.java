package com.yonyou.util.notity.template.impl.wx;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.util.notity.template.impl.TempForWxNews;

public class WxNewsTempForTest extends TempForWxNews{
	
	
	@Override
	protected String findTemplate_id() {
		// TODO 自动生成的方法存根
		return "111122";
	}
	@Override
	protected String findUrl() {
		// TODO 自动生成的方法存根
		return "BAIDU,COM";
	}
	
	@Override
	protected String[] findDataColumn() {
		// TODO 自动生成的方法存根
		return new String[]{"result","withdrawMoney","withdrawTime","cardInfo","arrivedTime","remark"};
	}
	
	
	

}
