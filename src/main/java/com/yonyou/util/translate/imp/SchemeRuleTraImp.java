package com.yonyou.util.translate.imp;

import java.util.Map;

import com.yonyou.util.translate.AbsFieldTranslate;

/**
 * 方案规则翻译类
 * @author moing_ink
 * <p>创建时间 ： 2016年12月27日
 * @version 1.0
 */
public class SchemeRuleTraImp extends AbsFieldTranslate {
	

	@Override
	protected boolean isTranslate() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void appendTranslateData(Map<String, Object> dataMap) {
		// TODO 自动生成的方法存根
		String rule_type="RULE_TYPE";
		dataMap.put(this.findAppendMarkCol(rule_type), this.findVRuleType(dataMap.get(rule_type)));
	}

	/**
	 * 获取规则类型翻译
	 * @param val
	 * @return
	 */
	private Object findVRuleType(Object val){
		Object returnVal="";
			if(("0").equals(val)){
				returnVal="一对一稽核";
			}else if(("1").equals(val)){
				returnVal="多对多稽核";
			}else if(("2").equals(val)){
				returnVal="一对多稽核";
			}else if(("3").equals(val)){
				returnVal="多对一稽核";
			}else {
				returnVal="数据有误，未知稽核类型";
			}
		return returnVal;
	}



	

}