package com.yonyou.business.button.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.yonyou.business.button.ButtonAbs;
import com.yonyou.business.entity.TokenEntity;
import com.yonyou.business.entity.TokenUtil;
import com.yonyou.util.BussnissException;
import com.yonyou.util.busflow.util.IBusFlowOperationType;
import com.yonyou.util.jdbc.IBaseDao;
import com.yonyou.util.sql.SqlTableUtil;

public class ButForInsert extends ButtonAbs{

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		this.appendData(json,request);
		try {
			dcmsDAO.insertByTransfrom(tabName, json);
			
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String jsonMessage = "{\"message\":\"保存成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		
		return null;
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
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String table="";
		String where="";
		switch(dataSourceCode){
			
			case "BUS_BUS_EXAMINE": 
				break;
			case "FIN_INVOICE_OPEN":
				break;
			case "PROJ_PROPOSAL_NL":
				break;
			case "BUS_Y_PROJECT_GROUP":
				break;
			case "CAPACITY_DESIGN":
				break;
			case "PROJECT_BUILD_MODIFY":
				break;
			case "BUS_WIN_BID_RESULT":
				String BUSINESS_ID=json.getString("BUSINESS_ID");
				where ="BUSINESS_ID='"+BUSINESS_ID+"'";
				table="BUS_WIN_BID_RESULT";
				this.handleTask(IBusFlowOperationType.HANDLE_TASK,dataSourceCode,this.findNoticeJsonData(table, where, dcmsDAO), request);
				break;
			case "PROJ_RELEASED":
				break;
			case "PROJ_REQUIREMENT":
				break;
			case "BUS_CONTRACT_ENTRY_IMPORT":
				break;
			case "BUS_CONTRACT_SIGN_SEAL_INFO":
				break;
			case "BUS_CONTRACT_HANDARCHIVES":
				break;
			case "CUMA_ACCOUNT_ADD_MESSAGE": //客户资料新增
				break;
			case "CUMA_ACCOUNT_UPDATE_MESSAGE": //客户资料修改
				break;
			default :
				this.handleTask(IBusFlowOperationType.HANDLE_TASK,dataSourceCode,json, request);
		
		}
		
	}
	
	@Override
	protected void appendData(Map<String, String> dataMap,
			HttpServletRequest request) {
		// TODO 自动生成的方法存根
		if(!TOKEN_ENTITY.isEmpty()){
			//制单人
			dataMap.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
			dataMap.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());
	        	
			//组织字段
			dataMap.put(IPublicBusColumn.ORGANIZATION_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
			dataMap.put(IPublicBusColumn.ORGANIZATION_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
			
			//部门信息
			dataMap.put(IPublicBusColumn.COMPANY_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
			dataMap.put(IPublicBusColumn.COMPANY_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
			dataMap.put(IPublicBusColumn.BILL_STATUS, "0");
		}
	}
	
	@Override
	public Map<String,Object> No_Common(Map<String, Object> dataMap,HttpServletRequest request) {
		// TODO 自动生成的方法存根
		    TokenEntity TOKEN_ENTITY =null;
		    TOKEN_ENTITY =TokenUtil.initTokenEntity(request);
		    
		    
			//制单人
			dataMap.put(IPublicBusColumn.CREATOR_ID, TOKEN_ENTITY.USER.getId());
			dataMap.put(IPublicBusColumn.CREATOR_NAME, TOKEN_ENTITY.USER.getName());
	        	
			//组织字段
			dataMap.put(IPublicBusColumn.ORGANIZATION_ID, TOKEN_ENTITY.COMPANY.getCompany_id());
			dataMap.put(IPublicBusColumn.ORGANIZATION_NAME, TOKEN_ENTITY.COMPANY.getCompany_name());
			
		
		return dataMap;
	}
	

}
