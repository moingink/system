package com.yonyou.util.page;

import java.util.Map;

import com.yonyou.util.page.util.SingleQueryFrameUtil;

/**
 * 查询页面 html
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class PageForSelect  extends PageForHtml{

	@Override
	public int getRows(int _rows) {
		// TODO 自动生成的方法存根
		if(_rows>0){
			return _rows;
		}else{
			return 3;
		}
		
	}
	
	@Override
	protected String appendDivLabel(int row, int col) {
		// TODO 自动生成的方法存根
		StringBuffer appendDivLabel=new StringBuffer();
		if(col>this.getHiddenCol()){
			appendDivLabel.append(" ishidden='true'");
		}
		return appendDivLabel.toString();
	}
	
	@Override
	protected String appendIsHidden(int row, int col) {
		// TODO 自动生成的方法存根
		StringBuffer appendIsHidden=new StringBuffer();
		if(col>this.getHiddenCol()){
			appendIsHidden.append("display:none");
		}
		return appendIsHidden.toString();
	}
	
	protected int getHiddenCol(){
		return 100;
	}

	@Override
	protected String findId(String filed_code,String input_type) {
		// TODO 自动生成的方法存根
		return SingleQueryFrameUtil.findSearchMark(input_type)+filed_code;
	}


	@Override
	public int findMarginTopPx() {
		// TODO 自动生成的方法存根
		return 10;
	}

	@Override
	public String appendHtml(Map<String,Object> dataMap,int row,int col) {
		// TODO 自动生成的方法存根
		StringBuffer buttonHtml =new StringBuffer("<div class='col-md-12'><br/></div>");
		buttonHtml.append("<div class='col-md-8 col-xs-8 col-sm-8'></div>")
		          .append("<div class='col-md-4 col-xs-4 col-sm-4' align='right' style='display: inline;'>")
		          .append("<div id='selectButtonPage'>");
		
		if(col>this.getHiddenCol()){
			buttonHtml.append("<button type='button' class='btn btn-default'  onclick='isToggle()'>")
					  //.append("<span class='glyphicon glyphicon-move' aria-hidden='true'></span> ")
					  .append("高级条件")
					  .append("</button>");
		}
		
		buttonHtml.append("</div></div>");
		return buttonHtml.toString();
	}

	@Override
	protected String findPageType() {
		// TODO 自动生成的方法存根
		return PageUtil.PAGE_TYPE_FOR_SELECT;
	}

	@Override
	protected void appendParamMap(Map<String, String> paramMap) {
		// TODO 自动生成的方法存根
		
	}
	
	

}
