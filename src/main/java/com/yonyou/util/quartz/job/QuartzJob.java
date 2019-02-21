package com.yonyou.util.quartz.job;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.JsonUtils;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.notity.NotifyFacade;
import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.quartz.util.NoticeEnum;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public abstract class QuartzJob implements Job{
	
	@Autowired
	private IBaseDao baseDao =(BaseDao)SpringContextUtil.getBean("dcmsDAO");
	
	protected abstract boolean runJob(Map<String,Object> noticeData);
	
	private String REQUEST_CODE="000000000#000";
	private String TABLE_NAME ="QRTZ_NOTICE_MESSAGE";
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO 自动生成的方法存根
		System.out.println("############"+this.getClass()+"开始\t"+TokenUtil.findSystemDateString());
		//远程调用接口
		Map<String,Object> data =(Map<String,Object>)context.getJobDetail().getJobDataMap().get(QuartzManager.JOBDATAMAP_NOTICE_DATA);
		Map<String,Object> noticeData =this.bulidNoticeDataMessage(data);
		String plan_code =(String)data.get("PLAN_CODE");
		String is_local =(String)data.get("IS_LOCAL");
		String run_message="";
		String run_state="";
		try{
			baseDao.insert(TABLE_NAME, noticeData);
			if(this.before(noticeData)){
				if(this.runJob(noticeData)){
					run_message="成功";
					run_state ="1";
				}else{
					run_message="失败";
					run_state ="0";
				}
			}
			this.after(noticeData);
		}catch(NullPointerException e1){
			run_message="远程地址调用失败！"+e1.getMessage();
			run_state ="0";
		} catch( Exception e){
			run_message=e.getMessage();
			run_state ="0";
			System.out.println(e);
		}finally{
			this.UpdateMessage(noticeData, run_message,run_state);
			if(run_state.equals("1")&&plan_code!=null&&plan_code.length()>0){
				NotifyFacade facade =new NotifyFacade();
				facade.Notify(plan_code, noticeData);
			}
		}
		
		System.out.println("############"+this.getClass()+"结束\t"+TokenUtil.findSystemDateString());
	}
	
	
	private void UpdateMessage(Map<String,Object> noticeData,String run_message,String run_state){
		Map<String,Object> updateData =new HashMap<String,Object>();
		if(run_message!=null&&run_message.length()>0){
			updateData.put("RUN_MESSAGE", run_message);
		}
		if(run_state.equals("0")){
			updateData.put("RUN_STATE", run_state);
		}
		updateData.put("RETURN_TIME", TokenUtil.findSystemSqlTimestamp());
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		whereEntity.putWhere("ID", noticeData.get("ID").toString(), WhereEnum.EQUAL_INT);
		baseDao.update(TABLE_NAME, updateData, whereEntity);
	}
	
	protected  boolean before(Map<String,Object> noticeData){
		return true;
	}
	
	protected  void after(Map<String,Object> noticeData){
		
	}
	
	private Map<String,Object> bulidNoticeDataMessage(Map<String,Object> data){
		Map<String,Object> noticeData =new HashMap<String,Object>();
		for(NoticeEnum noticeEnum:NoticeEnum.values()){
			String column =noticeEnum.getCode().toUpperCase();
			if(data.containsKey(column)){
				noticeData.put(column, data.get(column));
			}
		}
		noticeData.put("ID", RmIdFactory.requestId(TABLE_NAME, 1)[0]);
		noticeData.put("API_URL",this.bulidApiUrl(data));
		noticeData.put("API_MESSAGE",this.bulidApiRequestForMap(noticeData,data));
		noticeData.put("SEND_TIME", TokenUtil.findSystemSqlTimestamp());
		noticeData.put("RUN_STATE", "1");
		String run_ip ="";
		try {
			run_ip=Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			run_ip="UN_KNOWN";
		}
		noticeData.put("RUN_IP", run_ip);
		return noticeData;
	}
	
	
	private String bulidApiUrl(Map<String,Object> noticeData){
		StringBuffer apiUrl=new StringBuffer();
		//http://127.0.0.1:8080/system/service/System
		apiUrl.append("http://").append(noticeData.get("SYSTEM_IP"))
								.append(":")
							    .append(noticeData.get("SYSTEM_PORT"))
							    .append("/")
							    .append(noticeData.get("SYSTEM_NAME"))
							    .append("/")
							    .append("service/System");
			
		return apiUrl.toString();
	}
	
	private String bulidApiRequestForMap(Map<String,Object> noticeData,Map<String,Object> data){
		Map<String,String> requestMap =new HashMap<String,String>();
		requestMap.put("serviceCode", REQUEST_CODE);
		requestMap.put("JOB_NAME", (String)noticeData.get("JOB_NAME"));
		requestMap.put("JOB_CLASS", (String)noticeData.get("JOB_CLASS"));
		requestMap.put("JOB_DATA", (String)data.get("JOB_DATA"));
		requestMap.put("NOTICE_ID", noticeData.get("ID").toString());
		requestMap.put("PLAN_CODE", (String)data.get("PLAN_CODE"));
		return JsonUtils.map2json(requestMap);
	}
}
