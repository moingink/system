/**
 * 版权所有：华信软件
 * 项目名称:公用模块
 * 创建者: Wangdf
 * 创建日期: 2011-1-22
 * 文件说明: 定时任务管理类
 * 最近修改者：Wangdf
 * 最近修改日期：2011-1-22
 */
package com.yonyou.util.quartz;

import java.text.ParseException;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.quartz.vo.JobVo;
import com.yonyou.util.sql.SqlTableUtil;

/**
 * 定时任务管理类
 *
 * @author 王德封
 */
public class QuartzManager  {
	public static String JOBDATAMAP_NOTICE_DATA="NOTICE_DATA";
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
	private static String NOTICE_CLASS ="com.yonyou.util.quartz.job.NoticeJob";
	@Autowired
	private static IBaseDao baseDao =(BaseDao)SpringContextUtil.getBean("dcmsDAO");
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 *
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobClass,String task_id, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, Class.forName(jobClass));// 任务名，任务组，任务执行类
				jobDetail.getJobDataMap().put("task_id", task_id);
				jobDetail.getJobDataMap().put("data", "112233445566");
				// 触发器
				CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
				trigger.setCronExpression(time);// 触发器时间设定
				sched.scheduleJob(jobDetail, trigger);
				sched.scheduleJob(trigger);
				// 启动
			if (!sched.isShutdown()){
				sched.start();
			}
					
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 *
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJobBySystem(JobVo vo) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if(vo!=null&&vo.getSystem_id().length()>0){
				Map<String,Object> mapData =findSystemData(vo.getSystem_id());
				mapData=vo.appendMap(mapData);				
				JobDetail jobDetail = new JobDetail(vo.getJob_name(), JOB_GROUP_NAME, Class.forName(findJobClass(mapData,vo.getJob_class())));// 任务名，任务组，任务执行类
				jobDetail.getJobDataMap().put(JOBDATAMAP_NOTICE_DATA, mapData);
				// 触发器
				CronTrigger trigger = new CronTrigger(vo.getJob_name(), TRIGGER_GROUP_NAME);// 触发器名,触发器组
				trigger.setCronExpression(vo.getCron_timer());// 触发器时间设定
				sched.scheduleJob(jobDetail, trigger);
				// 启动
				if (!sched.isShutdown()){
					sched.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static String findJobClass(Map<String,Object> systemData,String jobClass){
		String IS_LOCAL="0";
		if(systemData.containsKey("IS_LOCAL")){
			IS_LOCAL=(String)systemData.get("IS_LOCAL");
		}
        if(!IS_LOCAL.equals("1")){
        	return NOTICE_CLASS;
        }
        
        return jobClass;
	}

	/**
	 * 添加一个定时任务
	 *
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName, String jobClass, String time){
		Scheduler sched =null;
		try {
			sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, Class.forName(jobClass));// 任务名，任务组，任务执行类
			sched.getJobDetail(jobName, jobGroupName);
			// 触发器
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
			trigger.setCronExpression(time);// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 *
	 * @param jobName
	 * @param time
	 */
	public static void modifyJobTimeAndTaskId(String jobName,String task_id ,String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
			if(trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
				Class objJobClass = jobDetail.getJobClass();
				String jobClass = objJobClass.getName();
				removeJob(jobName);

				addJob(jobName, jobClass,task_id, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间
	 *
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	public static void modifyJobTime(String triggerName,
			String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
			if(trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				// 修改时间
				ct.setCronExpression(time);
				// 重启触发器
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 *
	 * @param jobName
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
			sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务
	 *
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
			sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
			sched.deleteJob(jobName, jobGroupName);// 删除任务
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if(!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static Map<String,Object> findSystemData(String systemId){
		SqlTableUtil tableEntity =new SqlTableUtil("QRTZ_SYSTEM_MESSAGE","");
		tableEntity.appendSelFiled("*").appendWhereAnd("ID="+systemId);
		Map<String,Object> systemMap =baseDao.find(tableEntity);
		return systemMap;
	}
	
	public static void main(String args){
		QuartzManager.addJob("11", "ut.ew.util.EarlyWarninJob.class", "123","*/10 * * * * ?");
	}
}
