package com.yonyou.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.util.RmPartyConstants;

import com.alibaba.fastjson.support.odps.udf.CodecCheck.A;
import com.yonyou.util.RmIdFactory;
import com.yonyou.util.busflow.util.IBusFlowOperationType;

@RestController
@RequestMapping(value = "/mainDelivery")
public class MainDeliveryController extends BaseController {
	private void ajaxWrite(String ajaxMessage,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(ajaxMessage);
		out.flush();
		out.close();
	}
	
	/**
	 * 查询主送信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectMcd") 
	@ResponseBody
	public void selectMcd(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select * from bus_main_delivery where BUS_ID='"+request.getParameter("bus_id")+"' and DATA_CODE ='"+request.getParameter("data_code")+"'";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			
			System.out.println(jsonObject);
			
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}

	/**
	 * 新增主送信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	@RequestMapping(params = "cmd=insertMain") 
	@ResponseBody
	public void insertMain(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			String dataCode =json.getString("DATA_CODE");
			String bus_id =json.getString("BUS_ID");
			List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map_addMap = new HashMap<String, Object>();
			String [] Ids = RmIdFactory.requestId(RmPartyConstants.rmParty, 1);
			String deliveryId =Ids[0];
			map_addMap.put("ID", Ids[0]);
			map_addMap.put("BUS_ID",json.get("BUS_ID"));
			map_addMap.put("THEME",json.get("THEME"));
			map_addMap.put("MAIN_ID",json.get("MAIN_ID"));
			map_addMap.put("MAIN_NAME",json.get("MAIN_NAME"));
			map_addMap.put("COPY_ID",json.get("COPY_ID"));
			map_addMap.put("COPY_NAME",json.get("COPY_NAME"));
			map_addMap.put("ENCLOSURE_CODE",json.get("ENCLOSURE_CODE"));
			map_addMap.put("DATA_CODE",json.get("DATA_CODE"));
			map_addMap.put("SEND_ID",json.get("SEND_ID"));
			map_addMap.put("SEND_PERSON",json.get("SEND_PERSON"));
			map_addMap.put("SEND_TIME", new Timestamp((new Date()).getTime()));
			map_addMap.put("SEND_STATE", "1");
			map_addMap.put("DR", "0");
			entityList.add(map_addMap);
			dcmsDAO.insert("bus_main_delivery", entityList);
			System.out.println("------------------");
			//System.out.println(jsonObject);
			jsonMessage = "{\"status\":\"success\",\"message\":\"保存成功\"}";
			this.sendNotice(dataCode, bus_id, deliveryId,map_addMap, request);
			//jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 修改主送信息
	 * @param request
	 * @param response
	 * @param jsonData
	 * @throws Exception
	 */
	
	@RequestMapping(params = "cmd=updMain") 
	@ResponseBody
	public void updMain(HttpServletRequest request,HttpServletResponse response,@RequestBody String jsonData) throws Exception {
		String jsonMessage=null;
		try {
			JSONObject json = JSONObject.fromObject(jsonData);
			dcmsDAO.updateBySql("UPDATE bus_main_delivery SET "
					+ "THEME='"+json.get("THEME")+"',MAIN_ID='"+json.get("MAIN_ID")+"'"
					+ ",MAIN_NAME='"+json.get("MAIN_NAME")+"',COPY_ID='"+json.get("COPY_ID")+"'"
					+ ",COPY_NAME='"+json.get("COPY_NAME")+"',ENCLOSURE='"+json.get("ENCLOSURE")+"' "
					+ " WHERE BUS_ID='"+json.get("BUS_ID")+"'");
			System.out.println("------------------");
			jsonMessage = "{\"status\":\"success\",\"message\":\"修改成功\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查询用户表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectUser") 
	@ResponseBody
	public void selectUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select ID,NAME from rm_user where dr =0 ";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(),entry.getValue().toString());
				}
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			
			System.out.println(jsonObject);
			
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	/**
	 * 查询部门表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectDept") 
	@ResponseBody
	public void selectDept(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String sql = "select ID,NAME from tm_company where dr =0 ";
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue().toString());
				}
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			
			System.out.println(jsonObject);
			
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	
	/**
	 * 查询附件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "cmd=selectDoc") 
	@ResponseBody
	public void selectDoc(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String jsonMessage=null;
		try {
			String fileId=request.getParameter("fileId");
			String batch_no=request.getParameter("batch_no");
			String sql = "select concat(ID,'') as ID,FILE_NAME from doc_document ";
			if(!"".equals(fileId) && fileId!=null){
				sql+=" where ID="+fileId;
			}
			if(!"".equals(batch_no) && batch_no!=null){
				sql+=" where BATCH_NO="+batch_no;
			}
			List<Map<String, Object>> list = dcmsDAO.query(sql, "");
			
			JSONArray rowdata = new JSONArray();
			for (Map<String, Object> map : list) {
				JSONObject jsonObject = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(entry.getValue()!=null&&(entry.getValue().getClass().toString().equals("class java.sql.Date")||entry.getValue().getClass().toString().equals("class java.sql.Timestamp")))
						jsonObject.put(entry.getKey(), entry.getValue().toString());
					else
						jsonObject.put(entry.getKey(), entry.getValue());
				}
				rowdata.add(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rowdata", rowdata);
			
			System.out.println(jsonObject);
			
			jsonMessage = "{\"status\":\"success\",\"body\":"+jsonObject+"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonMessage = "{\"status\":\"fail\",\"message\":\"系统出错\"}";
			this.ajaxWrite(jsonMessage, request, response);
			return;
		}
	}
	
	public void sendNotice(String dataCode,String bus_id,String deliveryId,Map<String,Object> dataMap,HttpServletRequest request){
		String where ="";
		String table =""; 
		Map<String,Object> jsonData=new HashMap<String,Object>();
		switch(dataCode){
			case "PROJ_RELEASED":
				String OPPORTUNITY_CODE=bus_id;
				table="PROJ_RELEASED";
				where ="OPPORTUNITY_CODE='"+OPPORTUNITY_CODE+"'";
				break;
			case "PROJ_RELEASED_NLXM1":
				String BID_BUSS_NO=bus_id;
				table="PROJ_RELEASED";
				where ="BID_BUSS_NO='"+BID_BUSS_NO+"'";
				//添加
				jsonData.putAll(this.findNoticeJsonData("proj_proposal", " PROJ_SOURCE_ID  in (select proj_requirement.id from proj_requirement where proj_requirement.REQUIREMENT_CODE ='"+BID_BUSS_NO+"') ", dcmsDAO));
				jsonData.put("REQUIREMENT_CODE", BID_BUSS_NO);
				break;
		}
		jsonData.putAll(this.findNoticeJsonData(table, where, dcmsDAO));
		jsonData.put("_DELIVERY_ID", deliveryId);
		jsonData.put("_THEME", getNoticeVal("THEME",dataMap));
		jsonData.put("_MAIN_ID", getNoticeVal("MAIN_ID",dataMap));
		jsonData.put("_COPY_ID", getNoticeVal("COPY_ID",dataMap));
		this.handleTask(IBusFlowOperationType.HANDLE_TASK,dataCode,jsonData, request);
	}
	
	private String getNoticeVal(String key,Map<String,Object> dataMap){
		String returnString ="('')";
		if(dataMap.containsKey(key)){
			String val =(String)dataMap.get(key);
			if(val!=null&&val.length()>0){
				returnString =val;
			}
		}
		return returnString;
	}
	
}
