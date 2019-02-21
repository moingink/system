package com.yonyou.util.page.proxy.bulid;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.page.PageUtil;

public class PageBulidHtmlByIndUpd extends PageBulidHtmlByMaintain{

	@Override
	protected String[] findFiledArray(Map<String, String> dataSourceMap) {
		// TODO 自动生成的方法存根
		return dataSourceMap.get(DataSourceUtil.DATASOURCE_UPDATE_FIELD).trim().split("\\s*,\\s*");
	}

	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_INDEPENDENT_UPDATE;
	}

	@Override
	protected String colNullMessage() {
		// TODO 自动生成的方法存根
		return "<label class='control-label'>此页面还未配置独立修改页面</label>";
	}

	@Override
	protected String findProxyType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_INDEPENDENT_UPDATE;
	}

}
