package com.yonyou.util.quartz.warn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.iuap.persistence.vo.pub.BusinessException;
import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.mail.MailHelper;
import com.yonyou.util.notity.send.bulid.DataApi;
import com.yonyou.util.notity.send.entity.SendOperationEntity;
import com.yonyou.util.notity.send.impl.SendForEmail;
/**
 * 应标日报填写
 *
 */
public class BidsDayReportFill extends BaseWarn{
	public void execute(){

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 7);
		
		//查询所有客户经理和营销单元负责人
		List<Map<String, Object>> list = BaseDao.getBaseDao().query("select * from (select b.id as bid,bm.business_name,b.talk_b_time,bm.business_sub_company from bus_send_assessment b ,bus_business_message bm,bus_standard bs where  b.bus_message_id=bm.id and bs.business_code=b.bus_oppo_id and bs.report_type='0'  and bm.dr='0' ) y left join  (select ru.id as ruid,ru.name as runame,c.id as cid,ru.EMAIL from rm_party_role pr , rm_role r,rm_user ru,tm_company c  where  pr.dr='0'   and c.id=ru.organization_id and   r.id = pr.role_id  and ru.id = pr.owner_party_id  and r.role_code ='ybfzr'  ) x on  y.business_sub_company=x.cid where  y.talk_b_time>='"+sdf.format(new Date())+"' and y.talk_b_time<='"+sdf.format(calendar.getTime())+"'","");
		StringBuffer mailsendfailuserid=new StringBuffer();
		//组装所有客户经理和营销单元负责人的通知消息
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if(map.get("RUID")==null)
				continue;
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			String format="";
			try {
				format = sdff.format(sdf.parse(map.get("TALK_B_TIME").toString()));
			} catch (ParseException e) {
			}
			String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#BUSINAME#", map.get("BUSINESS_NAME").toString()).replaceAll("#TALK_B_TIME#", format);
			if(map.get("EMAIL")!=null&&map.get("EMAIL").toString().contains("@")&&map.get("EMAIL").toString().contains("com")){
				boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,map.get("EMAIL").toString());
				if(!sendMailRes){
					if(mailsendfailuserid.length()!=0)
						mailsendfailuserid.append(",");
					mailsendfailuserid.append(map.get("RUID").toString());
				}
			}
			hashMap.put("BUS_MESSAGE", need_message);
			hashMap.put("NEED_USER_NAME", map.get("RUNAME"));
			hashMap.put("NEED_USER_ID", map.get("RUID"));
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
					warn_event.put("RECEIVE_USER",map.get("ID").toString()+","+warn_event.get("RECEIVE_USER").toString());
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
