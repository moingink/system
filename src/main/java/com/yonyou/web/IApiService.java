package com.yonyou.web;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;

/** 
 * @author zzh
 * @version 创建时间：2017年5月25日
 * 类说明 
 */
public interface IApiService {

	public String doDeal(String jsonData);
	
	public String proxyDeal(String jsonData);
}
