package com.yonyou.util.translate.imp;

import java.util.Map;

import com.yonyou.util.translate.AbsFieldTranslate;

/**
 *	项目变更翻译
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class ProjectModifyTraImp extends AbsFieldTranslate {
	

	@Override
	protected boolean isTranslate() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void appendTranslateData(Map<String, Object> dataMap) {
		boolean flag = dataMap.containsKey("COUNT_LENGTH");
		if(flag == false){
			//变更内容分类翻译
			String content_classify = "MODIFY_CONTENT_CLASSIFY";
			dataMap.put("V"+content_classify, this.findVRuleType(dataMap.get(content_classify)));
			//变更所处阶段翻译
			String place_state = "MODIFY_PLACE_STATE";
			dataMap.put("V"+place_state, this.findVRuleType1(dataMap.get(place_state)));
			//变更状态翻译
			String modify_state = "MODIFY_STATE";
			dataMap.put("V"+modify_state, this.findVRuleType2(dataMap.get(modify_state)));
		}
	}

	/**
	 * 变更内容分类
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object val){
		Object returnVal="";
			String valueString = "";
			String[] split = val.toString().split(",");
			for (String string : split) {
				if(("0").equals(string)){//设计
					valueString += "能力项目详细设计变更,";
	        	}else if(("1").equals(string)){//预算
	        		valueString += "项目预算变更,";
	        	}else if(("2").equals(string)){//进度
	        		valueString += "项目进度阶段计划变更,";
	        	}else if(("3").equals(string)){//范围
	        		valueString += "项目范围(业务/前评估)变更,";
	        	}else if(("4").equals(string)){//风险
	        		valueString += "项目问题风险变更,";
	        	}else if(("5").equals(string)){//项目组织架构
	        		valueString += "项目组织架构变更,";
	        	}else if(("6").equals(string)){//其他计划
	        		valueString += "其他计划,";
	        	}else {
					returnVal="数据有误，未知稽核类型";
				}
			}
			returnVal = valueString.substring(0, valueString.length()-1);
		return returnVal;
	}
	
	/**
	 * 变更所处阶段
	 * @param val
	 * @return
	 */
	private Object findVRuleType1(Object val){
		Object returnVal="";
			if(("0").equals(val)){
				returnVal="提出";
			}else if(("1").equals(val)){
				returnVal="审批";
			}else if(("2").equals(val)){
				returnVal="执行";
			}else if(("3").equals(val)){
				returnVal="验证";
			}else if(("4").equals(val)){
				returnVal="领导审批";
			}else if(("5").equals(val)){
				returnVal="完成";
			}else {
				returnVal="数据有误，未知稽核类型";
			}
		return returnVal;
	}
	
	/**
	 * 获取规则类型翻译
	 * @param val
	 * @return
	 */
	private Object findVRuleType2(Object val){
		Object returnVal="";
			if(("0").equals(val)){
				returnVal="关闭";
			}else if(("1").equals(val)){
				returnVal="启动";
			}
		return returnVal;
	}

}