package com.yonyou.util.quartz.warn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
/**
 * 合同到期提醒
 *
 */
public class ContractExpireRemind  extends BaseWarn{
	public void execute(){

		//查询所有履行期限距离当前时间为180天之内的合同
		Calendar calendar_7 = Calendar.getInstance();
		calendar_7.add(Calendar.DATE, -7);
		Calendar calendar_15 = Calendar.getInstance();
		calendar_15.add(Calendar.DATE, -15);
		Calendar calendar_30 = Calendar.getInstance();
		calendar_30.add(Calendar.DATE, -30);
		String sql="select b.*  ,ru.EMAIL,";
		sql+="to_days(if(LOCATE('天' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('天' , b.contract_perform_time)-1) day) ,";
		sql+=" if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,LOCATE('年' , b.contract_perform_time)+1,LOCATE('月' , b.contract_perform_time)-1) month),interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) =0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('月' , b.contract_perform_time)-1) month) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) =0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="1))) ) )-to_days(now()) as days ";
		sql+="from bus_contract_admin b LEFT JOIN rm_user ru on ru.ID=b.undertake_id  where b.record_state='1' and b.dr='0' and  CURRENT_TIMESTAMP  <= if(LOCATE('天' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('天' , b.contract_perform_time)-1) day) ,";
		sql+=" if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,LOCATE('年' , b.contract_perform_time)+1,LOCATE('月' , b.contract_perform_time)-1) month),interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) =0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('月' , b.contract_perform_time)-1) month) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) =0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="1))) )";
		sql+="and date_add(CURRENT_TIMESTAMP,INTERVAL 180 DAY) >=if(LOCATE('天' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('天' , b.contract_perform_time)-1) day) ,";
		sql+=" if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,LOCATE('年' , b.contract_perform_time)+1,LOCATE('月' , b.contract_perform_time)-1) month),interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) !=0&&LOCATE('年' , b.contract_perform_time) =0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('月' , b.contract_perform_time)-1) month) ,";
		sql+="if(LOCATE('月' , b.contract_perform_time) =0&&LOCATE('年' , b.contract_perform_time) !=0,date_add(b.contract_take_eff_time, interval  SUBSTR(b.contract_perform_time,1,LOCATE('年' , b.contract_perform_time)-1) year) ,";
		sql+="1))) ) ";
		StringBuffer mailsendfailuserid=new StringBuffer();
		List<Map<String, Object>> list = BaseDao.getBaseDao().query(sql,"");
		Calendar instance2 = Calendar.getInstance();
		//组装所有客户经理和营销单元负责人的通知消息
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			try {
				instance2.setTime(sdf.parse(map.get("EX_APP_TIME").toString()));
			} catch (ParseException e) {
			}
			String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#CON_ID#", map.get("CON_ID").toString()).replaceAll("#DAYNUM#", map.get("DAYS")+"");
			if(map.get("EMAIL")!=null&&map.get("EMAIL").toString().contains("@")&&map.get("EMAIL").toString().contains("com")){
				boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,map.get("EMAIL").toString());
				if(!sendMailRes){
					if(mailsendfailuserid.length()!=0)
						mailsendfailuserid.append(",");
					mailsendfailuserid.append(map.get("UNDERTAKE_ID").toString());
				}
			}
			hashMap.put("BUS_MESSAGE", need_message);
			hashMap.put("NEED_USER_NAME", map.get("UNDERTAKE_NAME"));
			hashMap.put("NEED_USER_ID", map.get("UNDERTAKE_ID"));
			hashMap.put("NEED_STATE", "1");
			hashMap.put("NEED_START_TIME", sdf.format(new Date()));
			arrayList.add(hashMap);
		}
		//保存所有人的通知消息
		try {
			if(arrayList.size()>0)
				BaseDao.getBaseDao().insert("flow_need_pending", arrayList);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();

			Map<String, Object> warn_event=new HashMap<String, Object>();
			warn_event.put("EVENT_STATE", 0);
			warn_event.put("TIME", sdf.format(new Date()));
			warn_event.put("WARN_PROJ_ID", param.get("ID"));
			warn_event.put("EVENT_STATE", 1);
			warn_event.put("FAIL_REASON", "预警信息保存失败");
			for (Map<String, Object> map : list) {
				if(warn_event.get("RECEIVE_USER")!=null){
					warn_event.put("RECEIVE_USER",map.get("ID").toString()+warn_event.get("RECEIVE_USER").toString());
				}else{
					warn_event.put("RECEIVE_USER",map.get("ID").toString());
				}
			}
			warn_event_list.add(warn_event);
		}

		//添加发送邮件失败的事件
		if(mailsendfailuserid.length()!=0){
			Map<String, Object> warn_event=new HashMap<String, Object>();
			warn_event.put("EVENT_STATE", 0);
			warn_event.put("TIME", sdf.format(new Date()));
			warn_event.put("WARN_PROJ_ID", param.get("ID"));
			warn_event.put("EVENT_STATE", 1);
			warn_event.put("FAIL_REASON", "邮件发送失败");
			warn_event.put("RECEIVE_USER",mailsendfailuserid.toString());
			warn_event_list.add(warn_event);
		}
		if(warn_event_list.size()==0){
			Map<String, Object> warn_event=new HashMap<String, Object>();
			warn_event.put("EVENT_STATE", 0);
			warn_event.put("TIME", sdf.format(new Date()));
			warn_event.put("WARN_PROJ_ID", param.get("ID"));
			warn_event_list.add(warn_event);
		}
	}
}
