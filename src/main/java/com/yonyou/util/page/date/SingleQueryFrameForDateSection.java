package com.yonyou.util.page.date;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * 日期生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SingleQueryFrameForDateSection extends SingleQueryFrameForDate {

	@Override
	protected String findSingleFrame(String id, String name, String value,
			Map<String, String> message) {
		
		StringBuffer html =new StringBuffer();
		String id_from =id+"_FROM";
		String id_to =id+"_TO";
		html.append(super.findSingleFrame(id_from, name, value, message));
		html.append("<div  class='input-group' style='width:5%;float:left;'><label class='control-label' style='min-width:20px !important;'>&nbsp到&nbsp</label> </div>");
		html.append(super.findSingleFrame(id_to, name, value, message));
		return html.toString();
	}
	
	@Override
	public String getStyle() {
		// TODO 自动生成的方法存根
		return "width:45%;float:left";
	}
	
	@Override
	public void appendSpansForCalendar(StringBuffer html) {
		// TODO 自动生成的方法存根
	}
}
