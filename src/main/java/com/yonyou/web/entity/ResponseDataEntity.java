package com.yonyou.web.entity;

import net.sf.json.JSONObject;


/**
 * 数据传输实体
 * 
 * @author yansu
 *
 */
public class ResponseDataEntity {

	final public static String Fail="Fail";//Success\Failed
	final public static String Succ="Succ";//Success\Failed
	
	private String state;
	private String body;
	private String message;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@SuppressWarnings("all")
	@Override
	public String toString() {
		return toJsonObj().toString();
	};
	
	private JSONObject toJsonObj() {
		JSONObject m=new JSONObject();
		m.put("state", state);
		m.put("message", message);
		m.put("body", body);
		return JSONObject.fromObject(m);
	};
	
	public static void main(String[] args) {
		
	}


}
