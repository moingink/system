package com.yonyou.util.quartz.warn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
/**
 * 一户一案 战略市场规划业务主管
 *
 */
public class CustClass  extends BaseWarn{
	public void execute(){
		StringBuffer mailsendfailuserid=new StringBuffer();
		//查询所有客户经理
		List<Map<String, Object>> list = BaseDao.getBaseDao().query("select ru.ID,ru.`NAME`,ru.EMAIL from rm_party_role pr , rm_role r, rm_user ru,warn_proj warn_proj  where  pr.dr='0'       and  r.id = pr.role_id  and ru.id = pr.owner_party_id  and warn_proj.receive_role like  concat('%',concat(r.ID,'%')) and warn_proj.warn_proj_code='"+this.getClass().getName()+"'","");
		//组装所有客户经理的通知消息
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if(map.get("EMAIL")!=null&&map.get("EMAIL").toString().contains("@")&&map.get("EMAIL").toString().contains("com")){
				boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, query.get(0).get("MSG_TEMPLATE").toString(),map.get("EMAIL").toString());
				if(!sendMailRes){
					if(mailsendfailuserid.length()!=0)
						mailsendfailuserid.append(",");
					mailsendfailuserid.append(map.get("ID").toString());
				}
			}
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("BUS_MESSAGE", query.get(0).get("MSG_TEMPLATE"));
			hashMap.put("NEED_USER_NAME", map.get("NAME"));
			hashMap.put("NEED_USER_ID", map.get("ID"));
			hashMap.put("NEED_STATE", "1");
			hashMap.put("NEED_START_TIME", sdf.format(new Date()));
			arrayList.add(hashMap);
		}
		//保存所有客户经理的通知消息
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
