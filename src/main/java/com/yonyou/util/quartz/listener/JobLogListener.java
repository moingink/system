package com.yonyou.util.quartz.listener;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.quartz.vo.JobLogVo;
import com.yonyou.util.quartz.vo.JobVo;



/**
 * 任务日志监听器 记录任务执行历史
 * 
 * @author 刘佳
 * @since 2010-10-9
 */
public class JobLogListener implements JobListener {
	public JobLogListener() {
		name = this.getClass().getSimpleName();
	}
	
	/**
	 * jobLogVo与线程绑定，用于记录开始和结束
	 */
	private ThreadLocal<JobLogVo> tlJobLogVo = new ThreadLocal<JobLogVo>();
	
	private final static Logger log = Logger.getLogger(JobLogListener.class);
	
	
	private String name;
	
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void jobExecutionVetoed(JobExecutionContext context) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 记录任务开始执行
	 */
	public void jobToBeExecuted(JobExecutionContext context) {
		log.debug(context.getJobDetail().getName() + "-任务执行");
	}

	/**
	 * 记录任务执行结束
	 */
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		log.debug(context.getJobDetail().getName() + "-任务执行完毕");
	}
}

