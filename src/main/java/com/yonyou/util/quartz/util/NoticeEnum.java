package com.yonyou.util.quartz.util;


public enum NoticeEnum {
	
	 ID("ID","主键"),
	 JOB_NAME("JOB_NAME","任务名键"),
	 SYSTEM_NAME("SYSTEM_NAME","服务名"),
	 CRON_TIMER("CRON_TIMER","调度频率"),
	 JOB_CLASS("JOB_CLASS","实现类"),
	 API_URL("API_URL","接口地址"),
	 API_MESSAGE("API_MESSAGE","API_MESSAGE"),
	 SEND_TIME("SEND_TIME","发送时间"),
	 EXECUTE_S_TIME("EXECUTE_S_TIME","执行开始时间"),
	 EXECUTE_E_TIME("EXECUTE_E_TIME","执行结束时间"),
	 RETURN_TIME("RETURN_TIME","RETURN_TIME"),
	 RUN_STATE("RUN_STATE","运行状态"),
	 RUN_MESSAGE("RUN_MESSAGE","运行消息"),
	 RUN_IP("RUN_IP","运行消息"),
	 RUN_TIME("RUN_TIME","运行消息");
	private String code;
	private String name;
	
	NoticeEnum(String _code,String _name){
		code=_code;
		name=_name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return code;
	}
	
	
	
}
