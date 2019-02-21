package com.yonyou.business.button.cache;

import com.yonyou.business.DataSourceUtil;
import com.yonyou.business.MetaDataUtil;
import com.yonyou.business.ProxyPageUtil;
import com.yonyou.business.RmDictReferenceUtil;

/**
 * 缓存清理工具类
* @ClassName ClearCacheUtil 
* @author hubc
* @date 2018年5月12日
 */
public class ClearCacheUtil {

	public static void clearCache(String dataSourceCode) {

		if ("CD_DATASOURCE".equals(dataSourceCode)) {
			//数据源缓存
			DataSourceUtil.clear();
		} else if ("CD_METADATA".equals(dataSourceCode)
				|| "CD_METADATA_DETAIL".equals(dataSourceCode)) {
			//元数据缓存
			MetaDataUtil.clear();
		} else if ("RM_CODE_DATA".equals(dataSourceCode)
				|| "RM_CODE_TYPE".equals(dataSourceCode)) {
			//数据字典缓存
			RmDictReferenceUtil.clear();
		} else if ("CD_PROXY_PAGE".equals(dataSourceCode)
				|| "CD_PROXY_LIST_PAGE".equals(dataSourceCode)
				|| "CD_PROXY_MAIN_PAGE".equals(dataSourceCode)
				|| "CD_PROXY_SELECT_PAGE".equals(dataSourceCode)
				|| "CD_PROXY_UTIL_MESSAGE".equals(dataSourceCode)) {
			//代理页面缓存
			ProxyPageUtil.clear();
		}else{
			System.err.println("缓存处理类中没有找到有关-"+dataSourceCode+"-缓存的相关清理操作");
		}

	}

}
