package com.yonyou.util.page.proxy.bulid;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.page.PageUtil;

public class PageBulidHtmlBySel extends PageBulidHtmlByMaintain{

	@Override
	protected String[] findFiledArray(Map<String, String> dataSourceMap) {
		// TODO 自动生成的方法存根
		return dataSourceMap.get(DataSourceUtil.DATASOURCE_QUERY_FIELD).trim().split("\\s*,\\s*");
	}

	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_SELECT;
	}

	@Override
	protected String colNullMessage() {
		// TODO 自动生成的方法存根
		return "<label class='control-label'>此页面还未配置查询条件</label>";
	}

	@Override
	protected String findProxyType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_SELECT;
	}

}
