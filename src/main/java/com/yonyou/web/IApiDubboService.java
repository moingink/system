package com.yonyou.web;
/** 
 * @author zzh
 * @version 创建时间：2017年5月26日
 * 类说明 
 */
public class IApiDubboService implements IApiService{

	@Override
	public String doDeal(String jsonData) {
		// TODO 自动生成的方法存根
		return "hello world!! IApiDubboService";
	}

	@Override
	public String proxyDeal(String jsonData) {
		// TODO 自动生成的方法存根
		return doDeal(jsonData);
	}

}
