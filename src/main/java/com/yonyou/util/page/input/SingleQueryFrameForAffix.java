package com.yonyou.util.page.input;

import java.util.Map;

import com.yonyou.util.SerialNumberUtil;
import com.yonyou.util.SerialNumberUtil.SerialType;
import com.yonyou.util.page.SingleQueryFrameAbs;

/**
 * 页面附件元素html拼装
* @ClassName SingleQueryFrameForAffix 
* @author hubc
* @date 2018年5月16日
 */
public class SingleQueryFrameForAffix extends SingleQueryFrameAbs {

	
	
	@Override
	public String findSingleFrame(String id,String name, String value,Map<String,String> message) {
		
		StringBuffer html =new StringBuffer();
		System.out.println("我的MAp:"+message.toString());
		//file-input 使用组件
//		html.append("<div class='file-loading'>")
//			.append("<input type='file' class='file'")
//			.append(" id='").append(id).append("' name='files'")
//			.append("value='").append(value).append("' ")
//			.append("multiple></div>");
		//不使用组件
		html.append("<form action='#' enctype='multipart/form-data' >")  
			.append("<div style=\"float: left;line-height: 28px;\">")
			.append("<input type='file' class='file' name='files' id='anchor_").append(id).append("'")//添加锚点id
			.append("/>")
			.append("</div>")
			.append("<div>")
			.append("<button id=\"sub\" type=\"button\" class=\"btn btn-primary\" onclick='uploadAffix(\"").append(id).append("\");' class='btn green'><span class=\"glyphicon glyphicon-cloud-upload\"></span>&nbsp;&nbsp;提交</button>")
			.append("</div>")
			//隐藏标签存值-批次号
		;
			if("".equals(value)){
				html.append("<input type='hidden' id='").append(id).append("' name='").append(id).append("' value ='").append(getBatchNo()).append("' class='fileHidden' />");
			}else{
				html.append("<input type='hidden' id='").append(id).append("' name='").append(id).append("'value ='").append(value).append("' class='fileHidden' />");
			}
			
			html.append("<input type='hidden' id='bs_code' name='bs_code' value ='").append(message.get("input_formart")).append("' class='fileHidden' />")
			.append("</form>")
			;
		return html.toString();
	}
	
	
	
	
	public String findSingleFrameByAfixx(String id,String name, String value,Map<String,String> message) {
		
		StringBuffer html =new StringBuffer();
		System.out.println("我的MAp:"+message.toString());

//		html.append("<form action='#' enctype='multipart/form-data' >")  
//			.append("<div style=\"float: left;line-height: 28px;\">")
//			.append("<input type='file' class='file' name='files' id='anchor_").append(id).append("'")//添加锚点id
//			.append("/>")
//			.append("</div>")
//			.append("<div>")
//			.append("<button id=\"sub\" type=\"button\" class=\"btn btn-primary\" onclick='uploadAffix(\"").append(id).append("\");' class='btn green'><span class=\"glyphicon glyphicon-cloud-upload\"></span>&nbsp;&nbsp;提交</button>")
//			.append("</div>")
//			//隐藏标签存值-批次号
//		;
//			if("".equals(value)){
//				html.append("<input type='hidden' id='").append(id).append("' name='").append(id).append("' value ='").append(getBatchNo()).append("' class='fileHidden' />");
//			}else{
//				html.append("<input type='hidden' id='").append(id).append("' name='").append(id).append("'value ='").append(value).append("' class='fileHidden' />");
//			}
//			
//			html.append("<input type='hidden' id='bs_code' name='bs_code' value ='").append(message.get("input_formart")).append("' class='fileHidden' />")
//			.append("</form>")
//			;
		return html.toString();
	}
	
	
	
	  /**
	   * 创建批次号
	  * @return
	   */
	  private static String getBatchNo(){
		 return SerialNumberUtil.getSerialCode(SerialType.DOC_BATCH_NO, "DOC");//23位
	  }
	  
	  public static void main(String[] args) {
		System.out.println("DOC000000000001288".length());
	}
}
