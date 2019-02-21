package com.yonyou.business.button.taskjob.button;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.taskjob.ButForJob;
import com.yonyou.business.button.util.ButForInsert;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.quartz.QuartzManager;
import com.yonyou.util.quartz.vo.JobVo;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.sql.SQLUtil.WhereEnum;

public class ButForJobInsert extends ButForInsert{

	
	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("afterOnClick@@########################");
		// TODO 自动生成的方法存根
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		JobVo  jobVo =new JobVo(json);
		//修改状态  启动
		this.updateState(dcmsDAO, "1", jobVo);
		QuartzManager.addJobBySystem(jobVo);
	}
	
	protected void updateState(IBaseDao dcmsDAO,String stateVal,JobVo jobVo){
		Map<String,Object> updateDate =new HashMap<String,Object>();
		updateDate.put("STATE", stateVal);
		SqlWhereEntity sqlWhere =new SqlWhereEntity();
		sqlWhere.putWhere("JOB_NAME", jobVo.getJob_name(), WhereEnum.EQUAL_STRING);
		dcmsDAO.update("QRTZ_JOB_MESSAGE", updateDate, sqlWhere);
	}
	

}
