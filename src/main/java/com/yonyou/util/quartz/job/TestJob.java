package com.yonyou.util.quartz.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob extends QuartzJob {

	@Override
	protected boolean runJob(Map<String, Object> noticeData) {
		// TODO 自动生成的方法存根
		System.out.println("######任务运行！");
		return true;
	}


}
