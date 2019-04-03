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
import com.yonyou.util.sql.SqlTableUtil;
import com.yonyou.util.sql.SqlWhereEntity;

public class ButForUpdate extends ButtonAbs {

	@Override
	protected Object execute(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) throws IOException, BussnissException {
		// TODO 自动生成的方法存根
		String dataSourceCode =request.getParameter("dataSourceCode");
		String tabName = findTableNameByDataSourceCode(dataSourceCode);
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		this.appendData(json,request);
		SqlWhereEntity whereEntity =new SqlWhereEntity();
		try {
			this.appendWhereByIDs(json, whereEntity);
			dcmsDAO.updateByTransfrom(tabName, json, whereEntity);
		} catch (BussnissException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		String jsonMessage = "{\"message\":\"修改成功\"}";
		this.ajaxWrite(jsonMessage, request, response);
		return null;
	}

	@Override
	protected boolean befortOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		String nodeCode =request.getParameter("dataSourceCode");
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String OPERATION =request.getParameter("OPERATION");
		OPERATION=OPERATION==null?"":OPERATION;
		String id=json.getString("ID");
		String table ="";
		if(id!=null&&id.length()>0){
			switch(nodeCode){
				case "PROJECT_BUILD_MODIFY":
					table ="PROJECT_BUILD_MODIFY";
					switch(OPERATION){
						//驳回
						case "2":
							//验证
							Map<String,Object> dataMap=this.findNoticeJsonDataById(id,table, dcmsDAO);
							dataMap.put("MODIFY_PLACE_STATE", OPERATION);
							//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED, "PROJECT_MODIFY_EXECUTE_YZ", dataMap, request);
							break;
					}
					
					break;
				case "BUS_CONTRACT_SIGN_SEAL_INFO":
					table="BUS_CONTRACT_SIGNANDSEAL";
					switch(OPERATION){
						case "驳回":
							Map<String,Object> dataMap =this.findNoticeJsonDataById(id,table, dcmsDAO);
							dataMap.put("BUS_ID", dataMap.get("PARENT_ID"));
							dataMap.put("AUDIT_STATE", "0");
							//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED, "BUS_CONTRACT_SIGN_SEAL_AUDIT", dataMap, request);
							break;
					}
					break;
				case "BUS_CONTRACT_HANDARCHIVES":
					table="BUS_CONTRACT_HANDOVER_FILE";
					switch(OPERATION){
						case "驳回":
							Map<String,Object> dataMap =this.findNoticeJsonDataById(id,table, dcmsDAO);
							dataMap.put("BUS_ID", dataMap.get("PARENT_ID"));
							dataMap.put("AUDIT_STATE", "0");
							//this.handleTask(IBusFlowOperationType.CLOSE_HANDLE_NEED, "BUS_CONTRACT_HANDARCHIV_AUDIT", dataMap, request);
							break;
					}
					break;
				
			}
		}
		return false;
	}

	@Override
	protected void afterOnClick(IBaseDao dcmsDAO, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 自动生成的方法存根
		String nodeCode =request.getParameter("dataSourceCode");
		JSONObject json = JSONObject.fromObject(request.getParameter("jsonData"));
		String OPERATION =request.getParameter("OPERATION");
		OPERATION=OPERATION==null?"":OPERATION;
		String id=json.getString("ID");
		String table ="";
		if(id!=null&&id.length()>0){
			switch(nodeCode){
				case "PROJECT_BUILD_MODIFY":
					table ="PROJECT_BUILD_MODIFY";
					switch(OPERATION){
						//指定变更人
						case "Execute":
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "PROJECT_MODIFY_EXECUTE", this.findNoticeJsonDataById(id,table, dcmsDAO), request);
							break;
						//执行
						case "Submit":
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "PROJECT_MODIFY_EXECUTE_ZX", this.findNoticeJsonDataById(id,table, dcmsDAO), request);
							break;
						//同意
						case "4":
							//验证
							Map<String,Object> dataMap=this.findNoticeJsonDataById(id,table, dcmsDAO);
							dataMap.put("MODIFY_PLACE_STATE", OPERATION);
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "PROJECT_MODIFY_EXECUTE_YZ", dataMap, request);
							break;
							//驳回
						case "2":
							//验证
							Map<String,Object> rejectMap=this.findNoticeJsonDataById(id,table, dcmsDAO);
							rejectMap.put("MODIFY_PLACE_STATE", OPERATION);
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "PROJECT_MODIFY_EXECUTE_YZ", rejectMap, request);
							break;
						default:
							String IS_BENCHMARK_MODIFY=json.getString("IS_BENCHMARK_MODIFY");
							if(IS_BENCHMARK_MODIFY!=null&&IS_BENCHMARK_MODIFY.length()>0){
								String keyWord=json.getString("keyWord");
								json.put("BUS_ID", json.getString("ID"));
								nodeCode="";
								switch(keyWord){
								case "0":
									nodeCode="PROJECT_BUILD_MODIFY_SAVE_KH";
									break;
								case "1":
									nodeCode="PROJECT_BUILD_MODIFY_SAVE_NL";
									break;
								}
								if(nodeCode.length()>0){
									this.handleTask(IBusFlowOperationType.HANDLE_TASK, nodeCode, json, request);
								}
								
							}
							break;
					}
					
					break;
				case "BUS_BUSINESS_MESSAGE":
					table="BUS_BUSINESS_MESSAGE";
					if("BUS_BUS_SCORE".equals(OPERATION)){
						this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_BUS_SCORE", this.findNoticeJsonDataById(id,table, dcmsDAO), request);
					}
					break;
				case "BUS_WIN_BID_RESULT_SCORE":
					table="BUS_BUSINESS_MESSAGE";
					if("PINGFEN".equals(OPERATION)){
						String BUSINESS_ID=request.getParameter("BUSINESS_ID");
						Map<String,Object> dataMap =new HashMap<String,Object>();
						dataMap.put("BUSINESS_ID", BUSINESS_ID);
						this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_WIN_BID_RESULT_SCORE", dataMap, request);
					}
					break;
				case "BUS_CONTRACT_SIGN_SEAL_INFO":
					table="BUS_CONTRACT_SIGNANDSEAL";
					Map<String,Object> SealdataMap =this.findNoticeJsonDataById(id,table, dcmsDAO);
					switch(OPERATION){
						case "提交":
							if(SealdataMap.get("BATCH_NUMBER")==null){
								SealdataMap.put("BATCH_NUMBER", "");
							}
							SealdataMap.put("BUS_ID", SealdataMap.get("PARENT_ID"));
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_SIGN_SEAL_INFO", SealdataMap, request);
							break;
						case "签订盖章":
							SealdataMap.put("BUS_ID", SealdataMap.get("PARENT_ID"));
							SealdataMap.put("AUDIT_STATE", "1");
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_SIGN_SEAL_AUDIT", SealdataMap, request);
							break;
						case "驳回":
							SealdataMap.put("BUS_ID", SealdataMap.get("PARENT_ID"));
							SealdataMap.put("AUDIT_STATE", "0");
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_SIGN_SEAL_AUDIT", SealdataMap, request);
							break;
					}
					break;
				case "BUS_CONTRACT_HANDARCHIVES":
					table="BUS_CONTRACT_HANDOVER_FILE";
					Map<String,Object> HivesdataMap =this.findNoticeJsonDataById(id,table, dcmsDAO);
					switch(OPERATION){
						case "提交":
							if(HivesdataMap.get("BATCH_NUMBER")==null||HivesdataMap.get("BATCH_NUMBER").equals("null")){
								HivesdataMap.put("BATCH_NUMBER", "");
							}
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_HANDARCHIVES", HivesdataMap, request);
							break;
						case "归档":
							HivesdataMap.put("BUS_ID", HivesdataMap.get("PARENT_ID"));
							HivesdataMap.put("AUDIT_STATE", "1");
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_HANDARCHIV_AUDIT", HivesdataMap, request);
							break;
						case "驳回":
							HivesdataMap.put("BUS_ID", HivesdataMap.get("PARENT_ID"));
							HivesdataMap.put("AUDIT_STATE", "0");
							this.handleTask(IBusFlowOperationType.HANDLE_TASK, "BUS_CONTRACT_HANDARCHIV_AUDIT", HivesdataMap, request);
							break;
					}
					break;
					
					
			}
		}
		
	}
	
	private void appendWhereByIDs(JSONObject json,SqlWhereEntity whereEntity){
		
			String id = json.getString("ID");
			whereEntity.putWhere("ID", id, WhereEnum.EQUAL_INT);
	}
	
	
	
	
	
}
