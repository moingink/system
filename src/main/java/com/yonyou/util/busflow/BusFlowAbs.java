package com.yonyou.util.busflow;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yonyou.business.button.util.IPublicBusColumn;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.busflow.http.BusFlowRestClient;
import com.yonyou.util.busflow.http.api.BusFlowRestAbs;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.workflow.entity.ReturnJsonEntity;

public abstract class BusFlowAbs {
	
	public  static boolean isRunBusFlow =false;
	
	public  TokenEntity getTokenEntity(HttpServletRequest request){
		TokenEntity tokenEntity = TokenUtil.initTokenEntity(request);
		return tokenEntity;
	}
	
	public void handleTask(String type,String dataSourceCode,Map<String,Object> json ,HttpServletRequest request){
		if(isRunBusFlow){
			String tenant_code =request.getParameter("tenant_code");
			this.handleTask(type, dataSourceCode, tenant_code, json, request);
		}
		
	}
	
	public void handleTask(String type,String dataSourceCode,String tenant_code,Map<String,Object> json ,HttpServletRequest request){
		String nodeCode=dataSourceCode;
		String app_code =request.getContextPath();
		app_code=app_code.replaceAll("/","");
		String task_id =request.getParameter("TASK_ID");
		task_id=task_id==null?"":task_id;
		tenant_code=tenant_code!=null?tenant_code:"";
		if(tenant_code.length()>0){
			nodeCode =dataSourceCode+"_"+tenant_code;
		}
		//this.appendAuditOther(dataSourceCode, json, auditJson);
		Runnable tCUPD=new SendToBusFlow(type, app_code, nodeCode, task_id, json,this.getTokenEntity(request));
		new Thread(tCUPD).start();
		
	}
	
    /*public ReturnJsonEntity RestBusFlowTask(String type,String appCode,String node_code,String task_id,Map<String,Object> json,TokenEntity tokenEntity){
		
		BusFlowRestAbs restClinet = new BusFlowRestClient(appCode, node_code, task_id);
		if(!tokenEntity.isEmpty()){
			json.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
			json.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
			json.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
			json.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
		}
		switch(type){
			case IBusFlowOperationType.CLOSE_TASK:
				return restClinet.closeTask(json);
			case IBusFlowOperationType.HANDLE_TASK:
				return restClinet.handleTask(json);
			case IBusFlowOperationType.START_TASK:
				return restClinet.startTask(json);
			case IBusFlowOperationType.RUN_TASK:
				return restClinet.runTask(node_code,json);
		}
		return null;
	}*/
	
	
	class SendToBusFlow implements Runnable{
		public String type;
		public String app_code;
		public String node_code;
		public String task_id;
		public Map<String,Object> json;
		public TokenEntity tokenEntity;
		public SendToBusFlow(String type,String app_code,String node_code,String task_id,Map<String,Object> json,TokenEntity tokenEntity) {
			// TODO Auto-generated constructor stub
			this.type=type;
			this.app_code=app_code;
			this.node_code=node_code;
			this.task_id=task_id;
			this.json=json;
			this.tokenEntity=tokenEntity;
		}
	 
		@Override
		public void run() {
			//this.appendAuditOther(dataSourceCode, json, auditJson);
			RestBusFlowTask(type,app_code, node_code, task_id, json, tokenEntity);
		}
		
		
		public ReturnJsonEntity RestBusFlowTask(String type,String appCode,String node_code,String task_id,Map<String,Object> json,TokenEntity tokenEntity){
			
			BusFlowRestAbs restClinet = new BusFlowRestClient(appCode, node_code, task_id);
			if(!tokenEntity.isEmpty()){
				json.put(IPublicBusColumn.CREATOR_ID, tokenEntity.USER.getId());
				json.put(IPublicBusColumn.CREATOR_NAME, tokenEntity.USER.getName());
				json.put(IPublicBusColumn.COMPANY_ID, tokenEntity.COMPANY.getCompany_id());
				json.put(IPublicBusColumn.COMPANY_NAME, tokenEntity.COMPANY.getCompany_name());
				//上一环节ID
				json.put(IPublicBusColumn.PREVIOUS_USER_ID, tokenEntity.USER.getId());
				json.put(IPublicBusColumn.PREVIOUS_USER_NAME, tokenEntity.USER.getName());
			}
			switch(type){
				case IBusFlowOperationType.CLOSE_TASK:
					return restClinet.closeTask(json);
				case IBusFlowOperationType.HANDLE_TASK:
					return restClinet.handleTask(json);
				case IBusFlowOperationType.START_TASK:
					return restClinet.startTask(json);
				case IBusFlowOperationType.RUN_TASK:
					return restClinet.runTask(node_code,json);
				case IBusFlowOperationType.CLOSE_HANDLE_NEED:
					return restClinet.closeHandleNeed(json);
			}
			return null;
		}
		
	}
	
	public  Map<String,Object> findNoticeJsonData(String table,String where,IBaseDao dcmsDAO){
		SqlTableUtil sql =new SqlTableUtil(table,"");
		sql.appendSelFiled("*").appendWhereAnd(where);
		Map<String,Object> dataMap =dcmsDAO.find(sql);
		return dataMap;
	}
	
	public Map<String,Object> findNoticeJsonDataById(String id,String table,IBaseDao dcmsDAO){
		SqlTableUtil sqlTable =new SqlTableUtil(table,"");
		sqlTable.appendSelFiled("*").appendWhereAnd("ID ='"+id+"'");
		Map<String,Object> dataMap =dcmsDAO.find(sqlTable);
		return dataMap;
	}
	
}
