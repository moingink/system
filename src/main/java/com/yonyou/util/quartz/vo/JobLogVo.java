//代码生成时,文件路径: E:/QbRmWebDemo/src/main/java/ut/ap/scheduler/joblog/vo/JobLogVo.java
//代码生成时,系统时间: 2010-12-03 21:04:32
//代码生成时,操作系统用户: Administrator

/*
 * 系统名称:单表模板 --> QbRmWebDemo
 * 
 * 文件名称: ut.ap.scheduler.joblog.vo --> JobLogVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2010-12-03 21:04:32 创建1.0.0版 (白小勇)
 *  
 */

package com.yonyou.util.quartz.vo;


import java.sql.Timestamp;
import java.math.BigDecimal;



/**
 * 功能、用途、现存BUG:
 * 
 * @author 白小勇
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class JobLogVo {
    
    //开始rm_code_type的属性
    
	/**
     * id 表示: 主键
	 * 数据库注释: 
     */
    private String id;
	/**
     * job_type_id 表示: 任务类型编号
	 * 数据库注释: 
     */
    private String job_type_id;
	/**
     * job_type_name 表示: 任务类型
	 * 数据库注释: 
     */
    private String job_type_name;
	/**
     * job_deploy_id 表示: 任务条目编号
	 * 数据库注释: 
     */
    private String job_deploy_id;
	/**
     * job_deploy_name 表示: 任务条目
	 * 数据库注释: 
     */
    private String job_deploy_name;
	/**
     * start_time 表示: 开始时间
	 * 数据库注释: 
     */
    private Timestamp start_time;
	/**
     * end_time 表示: 结束时间
	 * 数据库注释: 
     */
    private Timestamp end_time;
	/**
     * run_time 表示: 运行时间
	 * 数据库注释: 
     */
    private BigDecimal run_time;
	/**
     * run_ip 表示: 运行IP
	 * 数据库注释: 
     */
    private String run_ip;
	/**
     * run_result 表示: 结果
	 * 数据库注释: 
     */
    private String run_result;
	/**
     * usable_status 表示: 数据可用状态
	 * 数据库注释: 
     */
    private String usable_status;
	/**
     * modify_date 表示: 修改日期
	 * 数据库注释: 
     */
    private Timestamp modify_date;
	/**
     * modify_ip 表示: 修改IP
	 * 数据库注释: 
     */
    private String modify_ip;
	/**
     * modify_user_id 表示: 修改用户ID
	 * 数据库注释: 
     */
    private String modify_user_id;

    /**
     * 非数据库字段：是否已经insert到数据库
     */
    private boolean is_flushed=false;
    
    //结束rm_code_type的属性
        
        
    //开始rm_code_type的setter和getter方法
    
    /**
     * 获得主键
     * 
     * @return 主键
     */
	public String getId(){
		return id;
	}
	
    /**
     * 设置主键
     * 
     * @param id 主键
     */
	public void setId(String id){
		this.id = id;
	}
	
    /**
     * 获得任务类型编号
     * 
     * @return 任务类型编号
     */
	public String getJob_type_id(){
		return job_type_id;
	}
	
    /**
     * 设置任务类型编号
     * 
     * @param job_type_id 任务类型编号
     */
	public void setJob_type_id(String job_type_id){
		this.job_type_id = job_type_id;
	}
	
    /**
     * 获得任务类型
     * 
     * @return 任务类型
     */
	public String getJob_type_name(){
		return job_type_name;
	}
	
    /**
     * 设置任务类型
     * 
     * @param job_type_name 任务类型
     */
	public void setJob_type_name(String job_type_name){
		this.job_type_name = job_type_name;
	}
	
    /**
     * 获得任务条目编号
     * 
     * @return 任务条目编号
     */
	public String getJob_deploy_id(){
		return job_deploy_id;
	}
	
    /**
     * 设置任务条目编号
     * 
     * @param job_deploy_id 任务条目编号
     */
	public void setJob_deploy_id(String job_deploy_id){
		this.job_deploy_id = job_deploy_id;
	}
	
    /**
     * 获得任务条目
     * 
     * @return 任务条目
     */
	public String getJob_deploy_name(){
		return job_deploy_name;
	}
	
    /**
     * 设置任务条目
     * 
     * @param job_deploy_name 任务条目
     */
	public void setJob_deploy_name(String job_deploy_name){
		this.job_deploy_name = job_deploy_name;
	}
	
    /**
     * 获得开始时间
     * 
     * @return 开始时间
     */
	public Timestamp getStart_time(){
		return start_time;
	}
	
    /**
     * 设置开始时间
     * 
     * @param start_time 开始时间
     */
	public void setStart_time(Timestamp start_time){
		this.start_time = start_time;
	}
	
    /**
     * 获得结束时间
     * 
     * @return 结束时间
     */
	public Timestamp getEnd_time(){
		return end_time;
	}
	
    /**
     * 设置结束时间
     * 
     * @param end_time 结束时间
     */
	public void setEnd_time(Timestamp end_time){
		this.end_time = end_time;
	}
	
    /**
     * 获得运行时间
     * 
     * @return 运行时间
     */
	public BigDecimal getRun_time(){
		return run_time;
	}
	
    /**
     * 设置运行时间
     * 
     * @param run_time 运行时间
     */
	public void setRun_time(BigDecimal run_time){
		this.run_time = run_time;
	}
	
    /**
     * 获得运行IP
     * 
     * @return 运行IP
     */
	public String getRun_ip(){
		return run_ip;
	}
	
    /**
     * 设置运行IP
     * 
     * @param run_ip 运行IP
     */
	public void setRun_ip(String run_ip){
		this.run_ip = run_ip;
	}
	
    /**
     * 获得结果
     * 
     * @return 结果
     */
	public String getRun_result(){
		return run_result;
	}
	
    /**
     * 设置结果
     * 
     * @param run_result 结果
     */
	public void setRun_result(String run_result){
		this.run_result = run_result;
	}
	
    /**
     * 获得数据可用状态
     * 
     * @return 数据可用状态
     */
	public String getUsable_status(){
		return usable_status;
	}
	
    /**
     * 设置数据可用状态
     * 
     * @param usable_status 数据可用状态
     */
	public void setUsable_status(String usable_status){
		this.usable_status = usable_status;
	}
	
    /**
     * 获得修改日期
     * 
     * @return 修改日期
     */
	public Timestamp getModify_date(){
		return modify_date;
	}
	
    /**
     * 设置修改日期
     * 
     * @param modify_date 修改日期
     */
	public void setModify_date(Timestamp modify_date){
		this.modify_date = modify_date;
	}
	
    /**
     * 获得修改IP
     * 
     * @return 修改IP
     */
	public String getModify_ip(){
		return modify_ip;
	}
	
    /**
     * 设置修改IP
     * 
     * @param modify_ip 修改IP
     */
	public void setModify_ip(String modify_ip){
		this.modify_ip = modify_ip;
	}
	
    /**
     * 获得修改用户ID
     * 
     * @return 修改用户ID
     */
	public String getModify_user_id(){
		return modify_user_id;
	}
	
    /**
     * 设置修改用户ID
     * 
     * @param modify_user_id 修改用户ID
     */
	public void setModify_user_id(String modify_user_id){
		this.modify_user_id = modify_user_id;
	}


	public boolean is_flushed() {
		return is_flushed;
	}

	public void flushed() {
		is_flushed=true;
	}
	
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		StringBuffer message =new StringBuffer("监控任务信息：\t");
		message.append("IP ="+this.run_ip).append("\t")
			   .append("ID ="+this.id).append("\t")
			   .append("JOB_DEPLOY_NAME ="+this.job_deploy_name).append("\t")
			   .append("JOB_DEPLOY_ID ="+this.job_deploy_id).append("\t")
			   .append("JOB_TYPE_NAME ="+this.job_type_name).append("\t")
			   .append("RUN_RESULT ="+this.run_result).append("\t");
		return message.toString();
	}

    //结束rm_code_type的setter和getter方法
    
}