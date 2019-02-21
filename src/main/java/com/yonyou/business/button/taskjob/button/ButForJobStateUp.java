package com.yonyou.business.button.taskjob.button;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yonyou.business.button.taskjob.ButForJob;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.quartz.vo.JobVo;

public class ButForJobStateUp extends ButForJob{

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("jsonData"));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jb = (JSONObject) jsonArray.get(i);
			JobVo  jobVo =new JobVo(jb);
			//修改状态  启动
			QuartzManager.addJobBySystem(jobVo);
			this.updateState(dcmsDAO, "1", jobVo);
		}
		String jsonMessage = "{\"message\":\"启动成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}


	

}
