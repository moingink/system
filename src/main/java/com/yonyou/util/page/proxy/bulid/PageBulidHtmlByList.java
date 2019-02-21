package com.yonyou.util.page.proxy.bulid;

import java.util.Map;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.page.PageUtil;
import com.yonyou.util.page.entity.DataSourceRequestEntity;
import com.yonyou.util.page.proxy.PageBulidApi;
import com.yonyou.util.page.proxy.PageBulidHtmlAbs;

public class PageBulidHtmlByList extends PageBulidHtmlAbs{

	@Override
	public String bulidPageHtmlString(DataSourceRequestEntity entity, Map<String, Object> dataMap)
			throws BussnissException {
		// TODO 自动生成的方法存根
		PageBulidApi pageBulid =this.findBulidClass(entity.getPageCode());
		return pageBulid.findTableColumn(entity);
	}

	@Override
	protected String[] findFiledArray(Map<String, String> dataSourceMap) {
		// TODO 自动生成的方法存根
		String fileds =dataSourceMap.get(DataSourceUtil.DATASOURCE_DISPLAY_FIELD);
		return fileds.split("&");
	}

	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_TABLE;
	}

	@Override
	protected String findProxyType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_TABLE;
	}
	
}
