package com.yonyou.util.quartz.warn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
/**
 * 商机延期提醒
 *
 */
public class BusiMsgDelay  extends BaseWarn{
	public void execute(){

		//查询所有客户经理和营销单元负责人
		List<Map<String, Object>> list = BaseDao.getBaseDao().query("select ru.EMAIL, bus_business_message.business_name, cuma_all_message.creator_id,cuma_all_message.creator_name, tm_company.dept_leader_id,tm_company.dept_leader  from bus_business_message bus_business_message left join cuma_all_message cuma_all_message on cuma_all_message.ID= bus_business_message.cus_id LEFT JOIN tm_company tm_company on tm_company.ID=bus_business_message.business_sub_company  LEFT JOIN rm_user ru on ru.ID=cuma_all_message.creator_id where  bus_business_message.bus_have_ok_time <'"+sdf.format(new Date())+"'","");
		//查询所有领导
		List<Map<String, Object>> listLeader = BaseDao.getBaseDao().query("select ru.EMAIL,ru.ID,ru.`NAME` from rm_party_role pr , rm_role r, rm_user ru,warn_proj warn_proj  where  pr.dr='0'       and  r.id = pr.role_id  and ru.id = pr.owner_party_id  and warn_proj.receive_role like  concat('%',concat(r.ID,'%')) and warn_proj.warn_proj_code='"+this.getClass().getName()+"'","");
		StringBuffer mailsendfailuserid=new StringBuffer();
		//组装所有客户经理和营销单元负责人的通知消息
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if(map.get("CREATOR_ID")!=null){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#BUSINAME#", map.get("BUSINESS_NAME").toString());
				if(map.get("EMAIL")!=null&&map.get("EMAIL").toString().contains("@")&&map.get("EMAIL").toString().contains("com")){
					boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,map.get("EMAIL").toString());
					if(!sendMailRes){
						mailsendfailuserid.append(map.get("EMAIL").toString());
						mailsendfailuserid.append(",");
					}
				}
				hashMap.put("BUS_MESSAGE", need_message);
				hashMap.put("NEED_USER_NAME", map.get("CREATOR_NAME"));
				hashMap.put("NEED_USER_ID", map.get("CREATOR_ID"));
				hashMap.put("NEED_STATE", "1");
				hashMap.put("NEED_START_TIME", sdf.format(new Date()));
				arrayList.add(hashMap);
			}

			if(map.get("DEPT_LEADER_ID")!=null){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#BUSINAME#", map.get("BUSINESS_NAME").toString());
				if(map.get("EMAIL")!=null&&map.get("EMAIL").toString().contains("@")&&map.get("EMAIL").toString().contains("com")){
					boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,map.get("EMAIL").toString());
					if(!sendMailRes){
						mailsendfailuserid.append(map.get("EMAIL").toString());
						mailsendfailuserid.append(",");
					}
				}
				hashMap.put("BUS_MESSAGE", need_message);
				hashMap.put("NEED_USER_NAME", map.get("DEPT_LEADER"));
				hashMap.put("NEED_USER_ID", map.get("DEPT_LEADER_ID"));
				hashMap.put("NEED_STATE", "1");
				hashMap.put("NEED_START_TIME", sdf.format(new Date()));
				arrayList.add(hashMap);
			}
			for (Map<String, Object> mapLeader : listLeader) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#BUSINAME#", map.get("BUSINESS_NAME").toString());
				if(mapLeader.get("EMAIL")!=null&&mapLeader.get("EMAIL").toString().contains("@")&&mapLeader.get("EMAIL").toString().contains("com")){
					boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,mapLeader.get("EMAIL").toString());
					if(!sendMailRes){
						if(mailsendfailuserid.length()!=0)
							mailsendfailuserid.append(",");
						mailsendfailuserid.append(mapLeader.get("ID").toString());
					}
				}
				hashMap.put("BUS_MESSAGE", need_message);
				hashMap.put("NEED_USER_NAME", mapLeader.get("NAME"));
				hashMap.put("NEED_USER_ID", mapLeader.get("ID"));
				hashMap.put("NEED_STATE", "1");
				hashMap.put("NEED_START_TIME", sdf.format(new Date()));
				arrayList.add(hashMap);
			}
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
