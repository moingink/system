package com.yonyou.util.notity.send.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendOperationEntity {
	
	private List<Map<String,Object>> userGroupList =new ArrayList<Map<String,Object>>();
	
	private String planKeyCode;
	
	private String sendMessage;
	
	private String title;
	
	private String logId;
	
	private String type;
	
	private Map<String,Object> paramMap =new HashMap<String,Object>();
	
	public SendOperationEntity(){
		
	}
	
	public SendOperationEntity(List<Map<String,Object>> userGroupList,String planKeyCode,
			String sendMessage,String title,String logId){
		
		this.setPlanKeyCode(planKeyCode);
		this.setSendMessage(sendMessage);
		this.setTitle(title);
		this.setUserGroupList(userGroupList);
		this.setLogId(logId);
	}

	public List<Map<String, Object>> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Map<String, Object>> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public String getPlanKeyCode() {
		return planKeyCode;
	}

	public void setPlanKeyCode(String planKeyCode) {
		this.planKeyCode = planKeyCode;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	
}
