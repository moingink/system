package com.yonyou.util.page.textarea;

import java.util.Map;

import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * 文本域生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SingleQueryFrameForTextarea extends SingleQueryFrameAbs{
	
	@Override
	public String findPageHtml(String id,String name,String value,Map<String,String> message){
		if(value==null){
			value="";
		}
		String lablename=name;
		String lowName=name;
		String realName=name;
		String calssStr="control-label";
		String tooltipStr="data-toggle='tooltip'"; 
		
		if(name.contains("*")){
			
			realName= name.substring(43);
			//System.out.println("label:1:====="+name+":"+lowName);
			if(realName.length()>7){
				lablename=realName.substring(0, 6)+"...";	 
				calssStr="control-labelnew";
//				tooltipStr="data-toggle='tooltip'"; 
				lablename=name.substring(0, 43)+lablename;
			}
			//System.out.println("label:2:====="+name+":"+lowName);
		}else{
			realName=name;
			if(realName.length()>7){
				lablename=realName.substring(0, 6)+"...";	 
				calssStr="control-labelnew";
//				tooltipStr="data-toggle='tooltip'";
			}			
		}
		
		StringBuffer html =new StringBuffer();
		html.append("<div class='form-group'>");
		html.append("<div class='col-md-2 col-xs-2 col-sm-2' style='white-space:nowrap;text-align: right;margin-left :-5.5%'>");
		html.append("<label class='"+calssStr+"' style='line-height: 29px;'   "+tooltipStr+" title='"+realName+"' style='line-height:76px' for='"+id+"'  >")
		  	.append(lablename).append("</label>");
		html.append("</div>");
		html.append("<div class='col-md-10 col-xs-10 col-sm-10' style='margin-left :-6px;margin-top:10px' >")
		  	.append(findSingleFrame(id,name,value,message))
		  	.append("</div>")
		  	.append("</div>");
		return html.toString();
		
	}
	
	@Override
	protected String findSingleFrame(String id, String name, String value,
			Map<String, String> message) {
		
		String temp_input_html=message.get("input_html")==null?"":message.get("input_html");
		String temp_input_desc=message.get("summary")==null?"":message.get("summary");
		
		
		// TODO 自动生成的方法存根
		StringBuffer html= new StringBuffer();
		html.append("<textarea class='").append(this.findClass()).append("'")
			.append(" id='").append(id).append("' name ='").append(id).append("'")
			.append(" rows ='").append(this.findRows()).append("' data-toggle='tooltip'  title='"+temp_input_desc+"' "+temp_input_html+" >");
		
		html.append(value);
		
		html.append("</textarea>");
		
		return html.toString();
	}
	
	/**
	 * 获取样式
	 * @return
	 */
	public String findClass(){
		return " form-control";
	}
	/**
	 * 获取列长度
	 * @return
	 */
	public String findRows(){
		return "3";
	}
	
}
