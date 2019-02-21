package com.yonyou.util.page;

import java.util.Map;

/**
 * 页面查询框架抽象类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class SingleQueryFrameAbs {
	
	/**
	 * 获取页面html信息
	 * @param id ID
	 * @param name 名称
	 * @return 页面HTML信息
	 */
	public String findPageHtml(String id,String name){
		
		return findPageHtml(id,name,"",null);
	}
	
	/**
	 * 获取页面html信息
	 * @param id ID
	 * @param name 名称
	 * @param value 值
	 * @return 页面HTML信息
	 */
	public String findPageHtml(String id,String name,String value){
		
		return findPageHtml(id,name,value,null);
	}
	
	/**
	 * 获取页面html信息
	 * @param id ID
	 * @param name 名称
	 * @param value 值
	 * @param message 数据信息
	 * @return 页面HTML信息
	 */
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
		//关闭不换行
		//html.append("<div class='col-md-4' style='white-space:nowrap;'>");
		html.append("<div class='col-md-4 col-xs-4 col-sm-4' style='text-align: right;'>");
		html.append("<label class='"+calssStr+"' style='line-height: 29px;'   "+tooltipStr+" title='"+realName+"' for='"+id+"'>")
		  	.append(lablename).append("</label>");
		html.append("</div>");
		html.append("<div class='col-md-8 col-xs-8 col-sm-8'>")
		  	.append(findSingleFrame(id,name,value,message))
		  	.append("</div>")
		  	.append("</div>");
		return html.toString();
		
	}
	
	/**
	 * 获取页面框架信息
	 * @param id ID
	 * @param name 名称
	 * @param value 值
	 * @param message 信息
	 * @return 页面框架信息
	 */
	protected abstract String findSingleFrame(String id,String name,String value,Map<String,String> message);
	
	/**
	 * 获取页面框架HTML信息
	 * @param id ID
	 * @param name 名称
	 * @param value 值
	 * @param message 信息
	 * @return 页面框架HTML信息
	 */
	public String findSingleHtml(String id,String name,String value,Map<String,String> message){
		return findSingleFrame( id, name, value, message);
	}
	
}
