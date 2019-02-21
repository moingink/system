package com.yonyou.util.quartz.warn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.mail.MailHelper;
/**
 * 预警基类
 */
public abstract class BaseWarn {
	protected  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected  static final SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
	protected List<Map<String, Object>> query;
	protected MailHelper mailHelper;
	protected String mailBizType="Warn";
	protected String mailTitle="新邮件";
	protected Map<String, Object> param;
	protected List<Map<String, Object>> warn_event_list=new ArrayList<Map<String, Object>>();
	public void executeBusiness(Map<String, Object> param){

		this.param=param;
		//邮件发送工具类
		mailHelper=new MailHelper();
		mailHelper.init();

		//查询预警项消息模板
		Class<? extends BaseWarn> clas = this.getClass();
		query = BaseDao.getBaseDao().query("select wp.* from warn_proj wp where wp.warn_proj_code='"+clas.getSimpleName()+"' and dr='0' ","");
		if(query.size()==0){

			Map<String, Object> warn_event=new HashMap<String, Object>();
			warn_event.put("EVENT_STATE", 0);
			warn_event.put("TIME", sdf.format(new Date()));
			warn_event.put("WARN_PROJ_ID", param.get("ID"));
			warn_event.put("EVENT_STATE", 1);
			warn_event.put("FAIL_REASON", "预警项不存在");
			warn_event_list.add(warn_event);
			if(warn_event_list.size()>0)
				BaseDao.getBaseDao().insert("WARN_EVENT", warn_event_list);
			return ;
		}
		if(query.get(0).get("WARN_PROJ_NAME")!=null)
			mailTitle=query.get(0).get("WARN_PROJ_NAME").toString();
		execute();
		if(warn_event_list.size()>0)
			BaseDao.getBaseDao().insert("WARN_EVENT", warn_event_list);
		return;
	}
	public abstract void execute();
}
