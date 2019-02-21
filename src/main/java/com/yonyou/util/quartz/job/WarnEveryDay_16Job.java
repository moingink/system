package com.yonyou.util.quartz.job;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonyou.util.jdbc.BaseDao;
import com.yonyou.util.quartz.QuartzManager;

public class WarnEveryDay_16Job extends QuartzJob {

	@Override
	protected boolean runJob(Map<String, Object> noticeData) {
		List<Map<String, Object>> query = BaseDao.getBaseDao().query("select w.* from warn_proj w LEFT JOIN qrtz_job_message q on q.ID=w.qrtz_job_message_id where w.dr ='0' and q.JOB_CLASS='com.yonyou.util.quartz.job.WarnEveryDay_16Job'  ", "");
		List<Map<String, Object>> warn_event_list=new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : query) {
			try {
				Class<?> forName = Class.forName("com.yonyou.util.quartz.warn."+map.get("WARN_PROJ_CODE"));
				Object newInstance = forName.newInstance();
				Method method = forName.getMethod("executeBusiness", Class.forName("java.util.Map"));
				method.invoke(newInstance, map);
			} catch (Exception e) {
				Map<String, Object> warn_event=new HashMap<String, Object>();
				warn_event.put("EVENT_STATE", 1);
				warn_event.put("FAIL_REASON", "创建预警项出错");
				warn_event.put("RECEIVE_USER", "");
				warn_event.put("TIME", sdf.format(new Date()));
				warn_event.put("WARN_PROJ_ID", map.get("ID"));
				warn_event_list.add(warn_event);
				e.printStackTrace();
			}
		}
		if(warn_event_list.size()>0)
			BaseDao.getBaseDao().insert("WARN_EVENT", warn_event_list);
		return true;
	}

	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
