package com.yonyou.business.button.notify;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yonyou.business.button.util.ButForUpdate;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;

public class NotifyButForUpdate extends ButForUpdate{
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		TokenEntity tokenEntity =TokenUtil.initTokenEntity(request);
		if(tokenEntity!=null&&tokenEntity.USER!=null){
			dataMap.put("MODIFIER", tokenEntity.USER.getId());
		}
		dataMap.put("TS", TokenUtil.findSystemDateString());
		dataMap.put("MODIFY_DATE", TokenUtil.findSystemDateString());
	}
}
