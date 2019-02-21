package com.yonyou.util.quartz.job;

import java.util.Map;

import com.yonyou.util.quartz.util.NoticeEnum;
import com.yonyou.util.wsystem.service.NoticeService;

public class NoticeJob extends QuartzJob{

	@Override
	protected boolean runJob(Map<String, Object> noticeData) {
		// TODO 自动生成的方法存根
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+(String)noticeData.get(NoticeEnum.API_URL.getCode())
				+"\t\t"+(String)(noticeData.get(NoticeEnum.API_MESSAGE.getCode())));
		NoticeService noticeService =new NoticeService((String)noticeData.get(NoticeEnum.API_URL.getCode()));
		noticeService.sendHTTP((String)noticeData.get(NoticeEnum.API_MESSAGE.getCode()));
		return true;
	}

	
}
