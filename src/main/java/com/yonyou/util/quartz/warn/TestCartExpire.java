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
 * 测试卡到期
 *
 */
public class TestCartExpire  extends BaseWarn{
	public void execute(){

		StringBuffer mailsendfailuserid=new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 5);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.DATE, 6);
		
		//查询所有距到期5天的测试卡
		List<Map<String, Object>> list = BaseDao.getBaseDao().query("select * from bus_inside_test_card b where  b.stop_cycle>='"+sdff.format(calendar.getTime())+" 00:00:00' and b.stop_cycle<'"+sdff.format(instance.getTime())+" 00:00:00'","");
		//查询卡管理员
		List<Map<String, Object>> cardManagerList = BaseDao.getBaseDao().query("select ru.ID,ru.`NAME`,ru.EMAIL from rm_party_role pr , rm_role r, rm_user ru,warn_proj warn_proj  where  pr.dr='0'       and  r.id = pr.role_id  and ru.id = pr.owner_party_id  and warn_proj.receive_role like  concat('%',concat(r.ID,'%')) and warn_proj.warn_proj_code='"+this.getClass().getName()+"'","");
		if(cardManagerList.size()==0){
			Map<String, Object> warn_event=new HashMap<String, Object>();
			warn_event.put("EVENT_STATE", 0);
			warn_event.put("TIME", sdf.format(new Date()));
			warn_event.put("WARN_PROJ_ID", param.get("ID"));
			warn_event.put("EVENT_STATE", 1);
			warn_event.put("FAIL_REASON", "卡管理员不存在");
			for (Map<String, Object> map : list) {
				if(warn_event.get("RECEIVE_USER")!=null){
					warn_event.put("RECEIVE_USER",map.get("ID").toString()+warn_event.get("RECEIVE_USER").toString());
				}else{
					warn_event.put("RECEIVE_USER",map.get("ID").toString());
				}
			}
			
			warn_event_list.add(warn_event);
		}
		Map<String, Object> cardManager=cardManagerList.get(0);
		//组装所有客户经理和营销单元负责人的通知消息
		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			Calendar instance2 = Calendar.getInstance();
			try {
				instance2.setTime(sdf.parse(map.get("APPLY_TIME").toString()));
			} catch (ParseException e) {
			}
			String need_message = query.get(0).get("MSG_TEMPLATE").toString().replaceAll("#MONTH#", (instance2.get(Calendar.MONTH)+1)+"").replaceAll("#DAY#", instance2.get(Calendar.DAY_OF_MONTH )+"");
			if(cardManager.get("EMAIL")!=null&&cardManager.get("EMAIL").toString().contains("@")&&cardManager.get("EMAIL").toString().contains("com")){
				boolean sendMailRes = mailHelper.sendMailSuper(mailBizType, mailTitle, need_message,cardManager.get("EMAIL").toString());
				if(!sendMailRes){
					if(mailsendfailuserid.length()!=0)
						mailsendfailuserid.append(",");
					mailsendfailuserid.append(cardManager.get("ID").toString());
				}
			}
			hashMap.put("BUS_MESSAGE", need_message);
			hashMap.put("NEED_USER_NAME", cardManager.get("NAME"));
			hashMap.put("NEED_USER_ID", cardManager.get("ID"));
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
