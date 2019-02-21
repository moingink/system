package com.yonyou.util.page.input;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * input生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class SingleQueryFrameForInput extends SingleQueryFrameAbs{
	
	
	@Override
	public String findSingleFrame(String id,String name, String value,Map<String,String> message) {
		// TODO 自动生成的方法存根
		StringBuffer html =new StringBuffer();
		String temp_input_html=message.get("input_html")==null?"":message.get("input_html");
		String temp_input_desc=message.get("summary")==null?"":message.get("summary");
		
	
		
		html.append("<input type='")
			.append(findType()).append("'")
			.append(" class='")
			.append(this.findClass())
			.append("' id='").append(id).append("' name='").append(id).append("'")
			.append("onfocus='inputFocus(this)' onblur='inputBlur(this)' autocomplete='off' value ='").append(findValue(value)).append("' data-toggle='tooltip' placeholder='"+temp_input_desc+"'  title='"+temp_input_desc+"' "+temp_input_html);
				  
				  
	    html.append(" />");
		
		return html.toString();
	}
	
	public String findValue(String value){
		return value;
	}
	
	/**
	 * 获取input类型
	 * @return
	 */
	public abstract String findType();
	/**
	 * 获取样式
	 * @return
	 */
	public abstract String findClass();
	
	
}
