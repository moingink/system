package com.yonyou.util.quartz.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJobOne extends QuartzJob {

	@Override
	protected boolean runJob(Map<String, Object> noticeData) {
		// TODO 自动生成的方法存根
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@"+noticeData.get("JOB_NAME"));
		return true;
	}

	

}
