package com.yonyou.util.quartz.warn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.jdbc.IBaseDao;

public class BusinessMessageWarm extends BaseWarn {
   
	private IBaseDao isdao=BaseDao.getBaseDao();
	private final long mill=1000*24*60*60;
	private long day=1;
	private String sendMessage="请及时更新商机状态，您要更新的商机信息如下：商机编号：sj  商机名称：mc  营销单元：yx  客户经理：jl  商机当前阶段：jd  商机当前状态：zt";
	private String title="商机状态";
	
	private String notice="FLOW_NEED_PENDING";
	
	@Override
	public void execute() {
	  List<Map<String,Object>> lMaps=new ArrayList<Map<String,Object>>();
	  final Map<String,Object> sendMap=new HashMap<String, Object>();	
	  Date nowtime=new Date();	
	  SimpleDateFormat stime=new SimpleDateFormat("yyyy-mm-dd");	
	  String sql="select COALESCE(creator_id,'0') creator_id,COALESCE(creator_name,'0') creator_name,business_id,business_name,COALESCE(bus_dep_name,'0') bus_dep_name,customer_manager,business_tage,business_state,COALESCE(examine_time,'') examine_time from bus_business_message where business_state in (3,4,5)";
	  List<Map<String,Object>> lm=isdao.query(sql,"");
	  if(lm.size()==0){
		  
	  }
	  else{
			try {
				  
				  //满足发邮件的商机信息
				  for(Map<String,Object> map:lm){
					    
					    if(map.get("EXAMINE_TIME").toString().equals("0")){
					    	continue ;
					    }
					    else{
						day+=(nowtime.getTime()-stime.parse(map.get("EXAMINE_TIME").toString()).getTime())/mill;
					    if(day>=30){
					    	lMaps.add(map);
					    }
					    }
				  }
				  
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("商机预警：时间转换出错");
			} 
				  
				  for(Map<String,Object>send:lMaps){
				      
					 boolean pd= mailHelper.sendMailSuper("Warn",title,sendEmailMessage(send),send.get("CUSTOMER_MANAGER").toString());					  
			         
					 if(pd){
						 sendMap.put("BUS_MESSAGE",sendEmailMessage(send) );                  //业务描述
				         sendMap.put("NEED_USER_NAME", send.get("CREATOR_NAME"));             //通知人
				         sendMap.put("NEED_USER_ID", send.get("CREATOR_ID"));                 //通知人ID
				         sendMap.put("NEED_STATE", "1");                                      //状态
				         sendMap.put("NEED_START_TIME", sdf.format(new Date()));              //开始时间

				      
					    new Thread(new Runnable() {
							
							@Override
							public void run() {
                               
								try {
									 isdao.insert(notice,sendMap);
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println("商机保存待阅失败");
								}   
							  
							}
						}).start();
						 
					 }
					 else{
						 System.out.println("发送失败");
						 continue ;
						 
					 }
					 
				  }
			  	//发送邮件及保存待阅  
	  }	

	}
	
	public String sendEmailMessage(Map<String,Object>map){
		//business_id,business_name,marketing_unit,customer_manager,business_tage,business_state
		//商机编号：sj  商机名称：mc  营销单元：yx  客户经理：jl  商机当前阶段：jd  商机当前状态：zt"
		
		/*map.put("business_id", "1");
		map.put("business_name", "2");
		map.put("marketing_unit", "3");
		map.put("customer_manager", "4");
		map.put("business_tage", "5");
		map.put("business_state", "6");*/	
		sendMessage=sendMessage.replaceAll("sj",map.get("BUSINESS_ID").toString()).replaceAll("mc",map.get("BUSINESS_NAME").toString()).replaceAll("yx",map.get("MARKETING_UNIT").toString()).replaceAll("jl",map.get("CUSTOMER_MANAGER").toString()).replaceAll("jd",map.get("BUSINESS_TAGE").toString()).replaceAll("zt",map.get("BUSINESS_STATE").toString());
		return sendMessage;
	}
}



