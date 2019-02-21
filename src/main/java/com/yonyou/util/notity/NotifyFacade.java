package com.yonyou.util.notity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.context.Theme;

import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.SpringContextUtil;
import com.yonyou.util.notity.plan.NotifyPlanAbs;
import com.yonyou.util.notity.plan.impl.NotifyPlanImp;
import com.yonyou.util.service.IBaseService;

public class NotifyFacade {
	
	@Autowired
	private  IBaseService baseService =(IBaseService)SpringContextUtil.getBean("baseService");
	
	public  String Notify(String planCode,Map<String,Object> dataMap) {
		String error="";
		NotifyPlanAbs notify =new NotifyPlanImp(dataMap);
		try {
			this.execute(notify, planCode, dataMap);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error =e.getMessage();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			error =e.getMessage();
		}
		if(error.length()>0){
			
			this.insertError(planCode, error);
		}
		
		return error;
	}
	
	private  void execute(NotifyPlanAbs notify,String planCode,Map<String,Object> dataMap) throws IOException, BussnissException{
		baseService.executePlan(notify, planCode, dataMap);
	}
	
	private void insertError(String planCode,String error){
		Map<String,Object> dataMap =new HashMap<String,Object>();
		dataMap.put("PLAN_CODE", planCode);
		dataMap.put("ERROR_MESSAGE", error);
		dataMap.put("SEND_TIME", TokenUtil.findSystemSqlTimestamp());
		baseService.insert("NF_NOTIFY_ERROR", dataMap);
		
	}
	
}
