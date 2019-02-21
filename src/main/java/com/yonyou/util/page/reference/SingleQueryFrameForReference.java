package com.yonyou.util.page.reference;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * 参照生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SingleQueryFrameForReference extends SingleQueryFrameAbs {

	@Override
	protected String findSingleFrame(String id, String name, String value,
			Map<String, String> message) {
		// TODO 自动生成的方法存根
		StringBuffer html =new StringBuffer();
		String input_formart =message.get("input_formart");
		String page_type=message.get("page_type");
		input_formart=input_formart!=null?input_formart.toUpperCase():"";
		//String input_formart="REF(CD_DATASOURCE,DATASOURCE_CODE:SEARCH-LIKE-OFFICE_CODE;DATASOURCE_NAME:SEARCH-LIKE-OFFICE_NAME,0)";
		html.append("<div class='input-group'")
			.append(" >");
			
		html.append("<input type='").append(findType()).append("'").append(" class='").append(this.findClass())
				.append("' id='").append(id).append("' name='").append(id).append("'").append(" value ='").append(value).append("' readonly='readonly' />");
		
//		html.append("<span class='input-group-addon' style='cursor:pointer' onclick=\"reference_removeVal('")
//			.append(id).append("')\")><span class='glyphicon glyphicon-remove'></span></span>")
		html.append(" <span class='input-group-addon' style='cursor:pointer' onclick=\"checkReference(this,'").append(input_formart).append("','"+id+"','"+page_type+"')\" ><span class='glyphicon glyphicon-search'></span></span>");
		
		html.append(" </div>");
		return html.toString();
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
