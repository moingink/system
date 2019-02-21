package com.yonyou.util.notity.send.impl;

import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.send.SendTypeEnum;
import com.yonyou.util.notity.send.bulid.DataApi;
import com.yonyou.util.notity.send.entity.SendOperationEntity;

public class SendForSms extends SendOperationAbs {


	@Override
	public SendTypeEnum findType() {
		// TODO 自动生成的方法存根
		return SendTypeEnum.SMS;
	}

	@Override
	protected void execute(SendOperationEntity entity, DataApi gather)
			throws BusinessException {
		// TODO 自动生成的方法存根
		this.testPrint("短信", entity);
	}

	@Override
	protected DataApi initGather() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected Object bulidDataMessage(Map userMap, SendOperationEntity entity)
			throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	

}
