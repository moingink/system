package com.yonyou.util.quartz.vo;

import java.io.Serializable;
import java.util.Map;

import net.sf.json.JSONObject;

public class JobVo implements Serializable{
	
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = -8848638708131699453L;
	
	private String id ="";
	private String system_name ="";
	private String job_name="";
	private String job_class="";
	private String state;
	private String cron_timer="";
	private String system_id="";
	private String job_data="";
	private String plan_code;
	public  JobVo(){
		
	}
	
	public JobVo(JSONObject json){
		if(json!=null){
			id=json.getString("ID");
			system_name=json.getString("SYSTEM_NAME");
			job_name=json.getString("JOB_NAME");
			system_id=json.getString("SYSTEM_ID");
			job_class=json.getString("JOB_CLASS");
			cron_timer=json.getString("CRON_TIMER");
			job_data=json.getString("JOB_DATA");
			plan_code=json.getString("PLAN_CODE");
		}
	}
	  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSystem_name() {
		return system_name;
	}
	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getSystem_id() {
		return system_id;
	}
	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}
	public String getJob_class() {
		return job_class;
	}
	public void setJob_class(String job_class) {
		this.job_class = job_class;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCron_timer() {
		return cron_timer;
	}
	public void setCron_timer(String cron_timer) {
		this.cron_timer = cron_timer;
	}
	
	public String getJob_data() {
		return job_data;
	}
	public void setJob_data(String job_data) {
		this.job_data = job_data;
	}

	public String getPlan_code() {
		return plan_code;
	}

	public void setPlan_code(String plan_code) {
		this.plan_code = plan_code;
	}

	public Map<String,Object> appendMap(Map<String,Object> mapData){
		mapData.put("JOB_NAME", job_name);
		mapData.put("SYSTEM_NAME", system_name);
		mapData.put("JOB_CLASS", job_class);
		mapData.put("CRON_TIMER", cron_timer);
		mapData.put("JOB_DATA", job_data);
		mapData.put("PLAN_CODE", plan_code);
		return mapData;
	}
	  
}
