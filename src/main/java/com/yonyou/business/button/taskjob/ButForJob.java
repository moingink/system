package com.yonyou.business.button.taskjob;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.quartz.vo.JobVo;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;

public class ButForJob extends ButtonAbs{

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
		return null;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		
	}
	
	
	protected void updateState(IBaseDao dcmsDAO,String stateVal,JobVo jobVo){
		Map<String,Object> updateDate =new HashMap<String,Object>();
		updateDate.put("STATE", stateVal);
		SqlWhereEntity sqlWhere =new SqlWhereEntity();
		sqlWhere.putWhere("JOB_NAME", jobVo.getJob_name(), WhereEnum.EQUAL_STRING);
		dcmsDAO.update("QRTZ_JOB_MESSAGE", updateDate, sqlWhere);
	}

}
