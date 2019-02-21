package com.yonyou.util.page.input;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 数值输入框 生成列
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SingleQueryFrameForMoney extends SingleQueryFrameForInput {

	@Override
	public String findType() { 
		// TODO 自动生成的方法存根
		return "TEXT";
	}

	@Override
	public String findClass() {
		// TODO 自动生成的方法存根
		return "form-control";
	}
	
	@Override
	public String findValue(String value) {
		
		if(null==value || "".equals(value.trim())){
			return "";
		}
		// TODO 自动生成的方法存根
		DecimalFormat df = new DecimalFormat("#,###.######");
		return df.format(new BigDecimal(value));

	}	
	
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
			.append("' id='").append(id).append("'  name='").append(id).append("'")
			.append("  value ='").append(findValue(value)).append("' data-toggle='tooltip'  title='"+temp_input_desc+"' "+temp_input_html);
				  
				  
	    html.append(" />");
		
		return html.toString();
	}
	
}
