package com.yonyou.util.validator.entity;

import java.util.regex.Matcher;

public class MatcherEntity {
	
	private int start;
	private int end;
	private String mark;
	
	public MatcherEntity(Matcher m){
		this.start =m.start();
		this.end =m.end();
		this.mark =m.group();
	}
	
	public MatcherEntity(int _start,int _end,String _mark){
		start=_start;
		end=_end;
		mark=_mark;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}
