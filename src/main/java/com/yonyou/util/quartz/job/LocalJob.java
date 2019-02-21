package com.yonyou.util.quartz.job;

import java.util.Map;

public abstract class LocalJob  extends QuartzJob{
	
	@Override
	protected void after(Map<String, Object> noticeData) {
		// TODO 自动生成的方法存根
		//记录公告信息
		//super.after(noticeData);
	}
}
