package com.yonyou.business.button.taskjob.button;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.util.ButForDelete;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.quartz.vo.JobVo;

public class ButForJobDelete extends ButForDelete{


	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			JobVo  jobVo =new JobVo(jb);
			QuartzManager.removeJob(jobVo.getJob_name());
		}
	}
	

}
