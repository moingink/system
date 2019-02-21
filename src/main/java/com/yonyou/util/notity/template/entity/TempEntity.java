package com.yonyou.util.notity.template.entity;

public class TempEntity {
	
	private String head;
	
	private String main;
	
	private String end;
	
	private String message;
	
	private String type;
	
	public TempEntity(String head,String main,String end,String message){
		this.setEnd(end);
		this.setHead(head);
		this.setMain(main);
		this.setMessage(message);
	}
	

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
