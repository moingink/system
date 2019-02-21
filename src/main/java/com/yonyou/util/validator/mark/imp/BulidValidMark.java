package com.yonyou.util.validator.mark.imp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.yonyou.util.validator.mark.BulidValidMarkAbs;
import com.yonyou.util.validator.type.BulidTypeEnum;
import com.yonyou.util.validator.util.BulidValidUtil;

public class BulidValidMark  extends BulidValidMarkAbs{
	
	/* 图标*/
	protected  String feedbackIcons ="valid:glyphicon glyphicon-ok,invalid:glyphicon glyphicon-remove,validating:glyphicon glyphicon-refresh";
	
	protected  String excluded =":disabled,:hidden";
	
	
	/* 非空 */
	protected  String notEmpty ="message: {FIELD_NAME} 必填不能为空!";
	/* 长度 */
	protected  String stringLength ="min:0,max:{FIELD_LENGTH},message: {FIELD_NAME}  长度超过{FIELD_LENGTH}位";
	/* 邮件*/
	protected  String emailAddress ="message: {FIELD_NAME}  邮件地址输入格式有误！";
	/* 日期*/
	protected  String date ="format: YYYY/MM/DD,message: 日期无效!";
	/* 数值*/
	protected  String digits ="message: {FIELD_NAME} 该值只能包含数字！";
	/* 小数*/
	protected  String decimal ="message: {FIELD_NAME} 该值只能包含数字！";
	/* 正则*/
	protected  String regexp="regexp: ,message: {FIELD_NAME} 输入格式不符合规则";
	/* 大小写验证*/
	protected  String stringCase="case:upper,message:{FILED_NAME} 只包含大写字符";
	/* 字段不相同 验证*/
	protected  String different="field: ,message: ";
	/* 字段相同校验*/
	protected  String identical="field: ,message: ";
	/* 远程验证*/
	//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}  
	protected  String remote ="url: , data: , message: ,delay: 2000 , type:POST ";
	
	
	protected  Map<String,Map<String,String []>> analysisType =new HashMap<String,Map<String,String []>>();
	@Override
	protected void initMarkMap() {
		// TODO 自动生成的方法存根
		this.markMap.put(this.findKey(BulidTypeEnum.HEAD, FEEDBACKICONS), feedbackIcons);
		this.markMap.put(this.findKey(BulidTypeEnum.HEAD, EXCLUDED), excluded);
		
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, NOTEMPTY), notEmpty);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, STRINGLENGTH), stringLength);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, EMAILADDRESS), emailAddress);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, DATE), date);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, DIGITS), digits);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, DECIMAL), decimal);
		
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, REGEXP), regexp);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, STRINGCASE), stringCase);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, DIFFERENT), different);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, IDENTICAL), identical);
		this.markMap.put(this.findKey(BulidTypeEnum.FILED, REMOTE), remote);
		initAnalysis();
	}
	
	protected void initAnalysis(){
		Map<String,String[]> analysis=new HashMap<String,String[]>();
		// # 代表  \
		analysis.put(REGEXP, new String[]{"^[0-9]+.?[0-9]*$"," {FIELD_NAME} 请输入数值"});
		analysisType.put(DECIMAL, analysis);
	}
	
	

	@Override
	protected String findTypeMessage(BulidTypeEnum bulidTypeEnum, String filed_type) {
		// TODO 自动生成的方法存根
		String key =this.findKey(bulidTypeEnum, filed_type);
		if(markMap.containsKey(key)){
			return markMap.get(key);
		}else{
			return "";
		}
	}
	
	@Override
	public Map<String, String[]> findAnalysisType(String filed_type,Map<String,String> dataMap) {
		// TODO 自动生成的方法存根
		if(analysisType.containsKey(filed_type)){
			Map<String, String[]> returnAnalsis =new HashMap<String,String []>();
			for(String key :analysisType.get(filed_type).keySet()){
				String [] temp =analysisType.get(filed_type).get(key);
				String [] returnString =new String[temp.length];
				int index =0;
				for(String str:temp){
					String returnStr =BulidValidUtil.replaceMarkString(str, dataMap);
					returnString[index]=returnStr;
					index++;
				}
				returnAnalsis.put(key, returnString);
			}
			return returnAnalsis;
		}else{
			return new HashMap<String,String[]>();
		}
	}

}
