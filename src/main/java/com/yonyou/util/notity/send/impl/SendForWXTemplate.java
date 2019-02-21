package com.yonyou.util.notity.send.impl;

import java.util.List;
import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.send.SendTypeEnum;
import com.yonyou.util.notity.send.bulid.DataApi;
import com.yonyou.util.notity.send.bulid.impl.ArrayListData;
import com.yonyou.util.notity.send.entity.SendOperationEntity;

/**
 * 发送微信端用户
 * @author jack
 *
 */

public class SendForWXTemplate extends SendOperationAbs<ArrayListData<Map<String,Object>>,Map<String,Object>>{


	@Override
	public SendTypeEnum findType() {
		// TODO 自动生成的方法存根
		return SendTypeEnum.EMAIL;
	}

	
	
	@Override
	protected void insertLog(List logInsertList) {
		// TODO 自动生成的方法存根
	}


	
	
	@Override
	protected void execute(SendOperationEntity entity,
			ArrayListData<Map<String, Object>> gather) throws BusinessException {
		// TODO 自动生成的方法存根
		
		String token="";
		String sendJsonStr=entity.getSendMessage();
		//发送微信方法
		//WxCommonInterfaceUtil.sendMessage(sendJsonStr, token);
		System.out.println("###############"+entity.getSendMessage());
	}

	@Override
	protected Map<String, Object> bulidDataMessage(Map<String, Object> userMap,
			SendOperationEntity entity) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}



	@Override
	protected ArrayListData<Map<String, Object>> initGather() {
		// TODO 自动生成的方法存根
		return new ArrayListData<Map<String, Object>>();
	}


}
