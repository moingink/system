package com.yonyou.util.notity.send.impl;

import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.mail.MailHelper;
import com.yonyou.util.notity.send.SendOperationAbs;
import com.yonyou.util.notity.send.SendTypeEnum;
import com.yonyou.util.notity.send.bulid.DataApi;
import com.yonyou.util.notity.send.entity.SendOperationEntity;

public class SendForEmail extends SendOperationAbs{


	@Override
	public SendTypeEnum findType() {
		// TODO 自动生成的方法存根
		return SendTypeEnum.EMAIL;
	}

	@Override
	protected void execute(SendOperationEntity entity, DataApi gather)
			throws BusinessException {
		// TODO 自动生成的方法存根
		MailHelper test = new MailHelper();
		test.init();
		String to ="1005543444@qq.com";
		test.sendMail(entity.getType(), entity.getTitle(), entity.getSendMessage(),to);
		this.testPrint("邮件", entity);
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
	
	public static void main(String[] args) {
		SendForEmail t=new SendForEmail();
		SendOperationEntity entity=new SendOperationEntity();
		DataApi a=t.initGather();
		try {
			t.execute(entity, a );
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}


}
