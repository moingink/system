package com.yonyou.business.button.notify;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;

public class NotifyButForInsert extends ButForInsert{
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		TokenEntity tokenEntity =TokenUtil.initTokenEntity(request);
		if(tokenEntity!=null&&tokenEntity.USER!=null){
			dataMap.put("CREATOR", tokenEntity.USER.getId());
		}
		dataMap.put("TS", TokenUtil.findSystemDateString());
		dataMap.put("CREATION_DATE", TokenUtil.findSystemDateString());
	}
}
