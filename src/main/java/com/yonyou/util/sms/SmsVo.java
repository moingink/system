package com.yonyou.util.sms;

public class SmsVo {
	
	//开始SmsVo的属性
	/**
     * user_name 表示: ID
     */
    private String id;
	
	/**
     * password 表示: 手机号码
     */
    private String mobileNumber;
	/**
     * full_name 表示: 姓名
	 * 数据库注释: 
     */
    private String name;
	/**
     * email 表示: 内容
	 * 数据库注释: 
     */
    private String Content;
	
    //结束rm_code_type的属性
        
        
    //开始SmsVo的setter和getter方法
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
}
