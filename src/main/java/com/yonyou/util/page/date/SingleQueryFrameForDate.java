package com.yonyou.util.page.date;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * 日期生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SingleQueryFrameForDate extends SingleQueryFrameAbs {

	@Override
	protected String findSingleFrame(String id, String name, String value,
			Map<String, String> message) {
		// TODO 自动生成的方法存根
		StringBuffer html =new StringBuffer();
		
		html.append("<div class='input-group date form_date' style="+this.getStyle()+"  data-date='").append(this.findDate_date()).append("'")
			.append(" data-date-format='").append(this.findDate_format()).append("'")
			.append(" data-link-field='").append(id).append("'")
			.append(" data-link-format='").append(this.findDate_link_format()).append("'")
			.append(" >");
			
		html.append("<input type='")
		.append(findType()).append("'")
		.append(" class='")
		.append(this.findClass())
		.append("' id='").append(id).append("' name='").append(id).append("'")
		.append(" value='").append(value).append("'   ");
	  html.append(" />");
		this.appendSpans(html);
		html.append(" </div>");
		
		return html.toString();
	}
	/**
	 * 获取data标示
	 * @return 标示
	 */
	public String findDate_date(){
		return "";
	}
	/**
	 * 获取日期格式化样式
	 * @return 样式
	 */
	public String findDate_format(){
		return "dd MM yyyy";
	}
	/**
	 * 获取日期连接格式样式
	 * @return 样式
	 */
	public String findDate_link_format(){
		return "yyyy-mm-dd";
	}
	
	public int findDataSize(){
		return 10;
	}
	
	public void appendSpans(StringBuffer html){
		this.appendSpansForRemove(html);
		this.appendSpansForCalendar(html);
	}
	
	public void appendSpansForCalendar(StringBuffer html){
		html.append(" <span class='input-group-addon'><span class='glyphicon glyphicon-calendar'></span></span>");
	}
	
	public void appendSpansForRemove(StringBuffer html){
		html.append(" <span class='input-group-addon'><span class='glyphicon glyphicon-remove'></span></span>");
	}
	
	public String getStyle(){
		return "width:99%";
	}
	
	public String findType() {
		// TODO 自动生成的方法存根
		return "TEXT";
	}

	public String findClass() {
		// TODO 自动生成的方法存根
		return "form-control";
	}

}
