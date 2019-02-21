package com.yonyou.util.page.proxy.bulid;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.page.PageUtil;

public class PageBulidHtmlByInsOrUpText extends PageBulidHtmlByInsOrUp {
	
	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_INSERT_UPDATE_TEXT;
	}
	
	@Override
	protected String[] findFiledArray(Map<String, String> dataSourceMap) {
		// TODO 自动生成的方法存根
		return dataSourceMap.get(DataSourceUtil.DATASOURCE_UPDATE_FIELD).trim().split("\\s*,\\s*");
	}
}
