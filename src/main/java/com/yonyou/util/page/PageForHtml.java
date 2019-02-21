package com.yonyou.util.page;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.yonyou.util.page.util.SingleQueryFrameUtil;

/**
 * 页面html生成类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public abstract class PageForHtml extends PageAbs {
	
	protected String ISHIDDEN_SHOW_COL ="ISHIDDEN_SHOW_COL";
	@Override
	protected String bulidPageHtml(Map<String, String> pageHtmlMap,Map<String,Object> dataMap,int _rows) {
		// TODO 自动生成的方法存根
		StringBuffer html =new StringBuffer();
		Map<Integer,String> colMap =new LinkedHashMap<Integer, String>();
		int row=1;
		int col=1;
		int rows=this.getRows(_rows);
		int textAreaSize =0;
		StringBuffer colStr =new StringBuffer();
		StringBuffer attachment =new StringBuffer();
		boolean isCol =false;
		for(String key:pageHtmlMap.keySet()){
			System.out.println(pageHtmlMap.get(key));
			if(row<=rows){
				String type=key.split(",")[0];
				
				if(type.equals(SingleQueryFrameUtil.TYPE_TEXT_HIDDEN)){
					colStr.append("<div style='display:none'> ")
						  .append(pageHtmlMap.get(key))
						  .append("</div> ");
				}else if(type.equals(SingleQueryFrameUtil.TYPE_TEXTAREA)){
					if(row>1){
					   row=1;
					   colMap.put(col, bulidGroupDiv(colStr.toString()));
					   if(textAreaSize>0){
						   col=col+textAreaSize+1;
					   }else{
						   col++;
					   }
					   textAreaSize=0;
					   colStr.setLength(0);
					   isCol=false;
					}
					
					String textArea="<div class='col-md-12' "+this.appendDivLabel(row, col)+" >"+pageHtmlMap.get(key)+"</div>";
					textArea=bulidGroupDiv(textArea);
					textAreaSize++;
					colMap.put(col+textAreaSize, textArea);
				}else{
					StringBuffer colMessage =new StringBuffer();
					colMessage.append("<div class='col-md-").append(12/rows).append(" col-xs-").append(12/rows).append(" col-sm-").append(12/rows)
					  .append("'style='margin-top:").append(findMarginTopPx()).append("px;display:inline")
					  .append(this.appendIsHidden(row, col)).append("'")
					  .append(this.appendDivLabel(row, col))
					  .append(">")
					  .append(pageHtmlMap.get(key))
					  .append("</div>");
					if(type.equals(SingleQueryFrameUtil.TYPE_AFFIX)){
						attachment.append(colMessage);
					}else{
						colStr.append(colMessage);
						if(row==rows){
							isCol=true;
						}
						row++;
					}
					
				}
				
			}
			if(isCol){
			   row=1;
			   colMap.put(col, bulidGroupDiv(colStr.toString()));
			   if(textAreaSize>0){
				   col=col+textAreaSize+1;
			   }else{
				   col++;
			   }
			   textAreaSize=0;
			   colStr.setLength(0);
			   isCol=false;
			}
		}
		colMap.put(col, bulidGroupDiv(colStr.toString()));
		colMap.put(col+textAreaSize+1, bulidGroupDiv(attachment.toString()));
		for(Integer colKey:colMap.keySet()){
			html.append(colMap.get(colKey));
		}
		html.append(this.appendHtml(dataMap,row,col));
		return html.toString();
	}
	
	/**
	 * 构建分组DIV信息
	 * @param html  HTML信息
 	 * @return html信息
	 */
	@Deprecated
	protected  String bulidGroupDiv(String html){
		return html;
	}
	/**
	 * 追加html内容
	 * @return html信息
	 */
	public abstract String appendHtml(Map<String,Object> dataMap,int row,int col);
	/**
	 * 追加html内容
	 * @return html信息
	 */
	public abstract int findMarginTopPx();
	/**
	 * 获取列长度
	 * @return 列长度
	 */
	public abstract int getRows(int _row);
	
	protected String appendDivLabel(int row,int col){
		return "";
	}
	
	protected String appendIsHidden(int row,int col){
		return "";
	}
}
