package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.yonyou.business.button.ButtonAbs;
import com.yonyou.util.BussnissException;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SQLUtil.WhereEnum;
import com.yonyou.util.sql.SqlWhereEntity;
import com.yonyou.util.workflow.entity.ReturnJsonEntity;

public class ButForAudit extends ButtonAbs   {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String app_code =request.getContextPath();
		String tenant_code =request.getParameter("tenant_code");
		tenant_code=tenant_code!=null?tenant_code:"";
		app_code=app_code.replaceAll("/","");
		JSONObject auditJson =new JSONObject();
		auditJson.put("ID", json.getString("ID"));
		this.appendData(json, request);
		//this.appendAuditOther(dataSourceCode, json, auditJson);
		ReturnJsonEntity jsonEntity =this.restAudit(app_code, dataSourceCode, tenant_code,json,TOKEN_ENTITY);
		Map<String,Object> data =new HashMap<String,Object>();
		String audit_type=request.getParameter("audit_type");
		String audit_column=request.getParameter("audit_column");
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		if(jsonEntity.isSuccess()){
			data.put(audit_column, audit_type);
			this.appendWhereByIDs(json, whereEntity);
			dcmsDAO.update(tabName, data, whereEntity);
		}else{
			if(json.containsKey(audit_column)){
				data.put(audit_column, json.getString(audit_column));
				this.appendWhereByIDs(json, whereEntity);
				dcmsDAO.update(tabName, data, whereEntity);
			}
		}
		this.ajaxWrite(jsonEntity.findJsonString(), request, response);
		return null;
	}
	
	private String findReturnMessage(String audit_type){
		String totalMessage="";
		switch(audit_type){
			
		case "1":
			totalMessage="提交成功"; break;
		case "2":
			totalMessage="提交成功"; break;
		case "3":
			totalMessage="审批成功"; break;
		case "4":
			totalMessage="审批成功"; break;
		case "7":
			totalMessage="退回成功"; break;
		}
		
		return totalMessage;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tenant_code =request.getParameter("tenant_code");
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		if(tenant_code!=null&&tenant_code.length()>0){
			dataSourceCode=dataSourceCode+"@"+tenant_code;
		}
		//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED,dataSourceCode,json, request);
		this.appendRestBusFlow(dataSourceCode, json, request);
	}
	
	private void appendWhereByIDs(JSONObject json,SqlWhereEntity whereEntity){
		
		String id = json.getString("ID");
		whereEntity.putWhere("ID", id, WhereEnum.EQUAL_INT);
	}
	
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		if(!TOKEN_ENTITY.isEmpty()){
			//制单人
			dataMap.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
			dataMap.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());
			//部门信息
			dataMap.put(IPublicBusColumn.COMPANY_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
			dataMap.put(IPublicBusColumn.COMPANY_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
			//上一环节
			dataMap.put(IPublicBusColumn.PREVIOUS_USER_ID, TOKEN_ENTITY.USER.getId());
			dataMap.put(IPublicBusColumn.PREVIOUS_USER_NAME, TOKEN_ENTITY.USER.getName());
		}
	}
	
	protected void appendRestBusFlow(String dataSourceCode,JSONObject json,HttpServletRequest request){
		if(dataSourceCode.equals("PROJECT_BUILD_MODIFY")){
			String keyWord=json.getString("keyWord");
			if(keyWord!=null&&keyWord.length()>0){
				String IS_BENCHMARK_MODIFY=json.getString("IS_BENCHMARK_MODIFY");
				if(IS_BENCHMARK_MODIFY!=null&&IS_BENCHMARK_MODIFY.length()>0){
					json.put("IS_BENCHMARK_MODIFY", "");
					json.put("BUS_ID", json.get("ID"));
					switch(keyWord){
					case "0":
						//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED,"PROJECT_BUILD_MODIFY_SAVE_KH","",json, request);
						break;
					
					case "1":
						//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED,"PROJECT_BUILD_MODIFY_SAVE_NL","",json, request);
						break;
					}
				}
			}
			
			
		}
	}
	
	private void appendAuditOther(String dataSourceCode,JSONObject json,JSONObject auditJson){
		
		switch (dataSourceCode){
			case "OPEN_APPLY" :
				if(json.containsKey("DISCOUNT_RATE")){
					auditJson.put("DISCOUNT_RATE", json.get("DISCOUNT_RATE"));
				}
			break;
			
			case "FIN_ESTIMATED_INCOME" :
				
			case "FIN_CONFIRM_INCOME":
				if(json.containsKey("BILL_NO")){
					auditJson.put("BILL_NO", json.get("BILL_NO"));
				}
			break;	
			
			case "CUMA_ACCOUNT" :
				if(json.containsKey("CUSTOMERLEVEL")){
				   auditJson.put("CUSTOMERLEVEL", json.get("CUSTOMERLEVEL"));
				}
			break;
		
		}
	}
	
	
	
}
